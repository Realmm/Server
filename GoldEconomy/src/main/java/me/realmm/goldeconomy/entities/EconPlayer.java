package me.realmm.goldeconomy.entities;

import me.realmm.goldeconomy.database.EconMongo;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import me.realmm.serverscoreboard.ServerScoreboard;
import net.jamesandrew.realmlib.inventory.InventoryUtil;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import net.jamesandrew.serverscoreboard.realmlib.scoreboard.RealmScoreboard;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class EconPlayer {

    private final OfflinePlayer o;
    private BigDecimal balance = BigDecimal.ZERO;
    private final EconMongo econMongo;
    private final Set<Item> items = new HashSet<>();
    private final RealmScoreboard scoreboard;

    public EconPlayer(OfflinePlayer p) {
        this.o = p;
        this.econMongo = EconUtil.getMongoDB();
        ServerScoreboard plugin = (ServerScoreboard) Bukkit.getPluginManager().getPlugin("ServerScoreboard");
        if (plugin == null) {
            scoreboard = null;
            return;
        }

        scoreboard = plugin.getScoreboard(p);
    }

    public OfflinePlayer getOfflinePlayer() {
        return o;
    }

    public boolean isOnline() {
        return o.isOnline();
    }

    public Player getPlayer() {
        if (!isOnline()) throw new IllegalArgumentException("Tried to get player while player was offline");
        return Bukkit.getPlayer(o.getUniqueId());
    }

    public WithdrawResponse withdraw(int amount) {
        if (!isOnline()) return WithdrawResponse.OFFLINE;
        ItemStack i = new ItemStack(Material.GOLD_INGOT, amount);
        Player p = getPlayer();
        if (!InventoryUtil.canAdd(p, i.getType(), amount)) return WithdrawResponse.FULL_INVENTORY;
        boolean shouldAdd = balance.subtract(BigDecimal.valueOf(amount)).compareTo(BigDecimal.ZERO) >= 0;
        if (shouldAdd) p.getInventory().addItem(i);
        if (!shouldAdd) return WithdrawResponse.INSUFFICIENT_FUNDS;
        balance = shouldAdd ? balance.subtract(BigDecimal.valueOf(amount)) : balance;
        econMongo.updatePlayer(this);
        updateScoreboard();
        return WithdrawResponse.SUCCESS;
    }

    public boolean deposit(int amount) {
        if (!isOnline()) return false;
        ItemStack i = new ItemStack(Material.GOLD_INGOT, amount);
        Player p = getPlayer();
        if (!p.getInventory().containsAtLeast(i, amount)) return false;
        p.getInventory().removeItem(i);
        balance = balance.add(BigDecimal.valueOf(amount));
        econMongo.updatePlayer(this);
        updateScoreboard();
        return true;
    }

    public boolean hasItems(Map<Item, Integer> items) {
        boolean[] hasItems = {false};

        items.forEach((item, amount) -> {
            if (hasItems[0]) return;
            boolean hasAmount = this.items.stream().filter(i -> i == item).anyMatch(i -> amount <= i.getAmount());
            boolean hasPair = this.items.stream().anyMatch(i -> i == item);
            hasItems[0] = hasPair && hasAmount;
        });

        return hasItems[0];
    }

    /**
     * Purchases items from the player
     * Make sure you check this player has these items before attempting to purchase
     * @param purchaser The player purchasing
     * @param items An item or collation of items that this player DOES already have
     * @return true if successful, otherwise false
     */
    public boolean buyFrom(EconPlayer purchaser, Map<Item, Integer> items) {
        if (!purchaser.isOnline()) throw new IllegalArgumentException("Purchaser not online");
        if (!hasItems(items)) throw new IllegalArgumentException("Player does not have one or more of these items, or does not have a certain amount of a certain item");

        Player p = getPlayer();

        int amountToPurchase = items.entrySet().stream().flatMapToInt(e -> IntStream.of(e.getValue())).sum();

        Set<CostAmountMap> mapper = new HashSet<>();

        items.forEach((item, amount) -> {
            BigDecimal cost = item.getIndividualCost().multiply(BigDecimal.valueOf(amount));
            Predicate<? super CostAmountMap> predicate = c -> c.getType() == item.getType() && c.getData() == item.getData();

            if (mapper.stream().anyMatch(predicate)) {
                CostAmountMap map = mapper.stream().filter(predicate).findFirst().orElseThrow(() -> new IllegalArgumentException("No CostAmountMap"));
                map.setAmount(map.getAmount() + amount);
                map.setCost(map.getCost().add(cost));
            } else {
                CostAmountMap map = new CostAmountMap(item.getType(), amount, item.getData(), cost, item.getName());
                mapper.add(map);
            }
        });

        BigDecimal costOfPurchase = mapper.stream().map(CostAmountMap::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (costOfPurchase.compareTo(purchaser.getBalance()) > 0) {
            if (!purchaser.isOnline()) return false;
            purchaser.getPlayer().sendMessage(Messages.INSUFFICIENT_BALANCE);
            return false;
        }

        List<ItemStack> toGive = new ArrayList<>();

        mapper.forEach(c -> {
            ItemStack give = new ItemStack(c.getType(), c.getAmount(), c.getData());
            toGive.add(give);
        });

        if (!InventoryUtil.canAdd(purchaser.getPlayer(), toGive, false)) {
            purchaser.getPlayer().sendMessage(Messages.FULL_INVENTORY);
            return false;
        }

        toGive.forEach(i -> {
            purchaser.getPlayer().getInventory().addItem(i);
        });

        Iterator<Map.Entry<Item, Integer>> iter = items.entrySet().iterator();

        iter.forEachRemaining(e -> {
            Item toChange = this.items.stream().filter(i -> i.isSimilar(e.getKey())).findFirst().orElseThrow(() -> new IllegalArgumentException("No items to subtract"));
            if (toChange.getAmount() - e.getValue() <= 0) {
                this.items.remove(toChange);
            } else {
                toChange.setAmount(toChange.getAmount() - e.getValue());
            }
        });

        balance = balance.add(costOfPurchase);
        purchaser.setBalance(purchaser.getBalance().subtract(costOfPurchase));

        if (isOnline()) {
            mapper.forEach(c ->
                p.sendMessage(new Placeholder(Messages.SOLD_ITEM)
                        .setPlaceholders("amount", "item", "cost")
                        .setToReplace(amountToPurchase, c.hasCustomName() ? c.getCustomName() : WordUtils.capitalize(c.getType().name().toLowerCase().replace("_", " ")), EconUtil.adjustDecimal(c.getCost()).doubleValue()).toString())
            );
        }

        econMongo.updatePlayer(this);
        updateScoreboard();
        return true;
    }

    public void updateScoreboard() {
        if (isOnline() && scoreboard != null) {
            scoreboard.setLine(3, new Placeholder(Messages.SCOREBOARD_BALANCE).setPlaceholders("balance").setToReplace(balance.setScale(2, RoundingMode.HALF_UP).doubleValue()).toString());
            scoreboard.update(getPlayer());
        }
    }

    public boolean sell(ItemStack i, double cost) {
        if (!isOnline()) return false;
        Player p = getPlayer();
        if (!InventoryUtil.containsAtLeast(p.getInventory(), i, i.getAmount())) return false;
        p.getInventory().removeItem(i);

        Item item = new Item(this, i.getType(), i.getAmount(), i.getDurability(), cost);
        items.add(item);
        econMongo.updatePlayer(this);
        return true;
    }

    public void setItems(Set<Item> items) {
        this.items.addAll(items);
    }

    public Collection<Item> getItems() {
        return Collections.unmodifiableCollection(items);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    private static class CostAmountMap {

        private final Material material;
        private BigDecimal cost;
        private int amount;
        private String customName;
        private final short data;

        CostAmountMap(Material material, int amount, short data, BigDecimal cost) {
            this.material = material;
            this.amount = amount;
            this.cost = cost;
            this.data = data;
        }

        CostAmountMap(Material material, int amount, short data, BigDecimal cost, String customName) {
            this(material, amount, data, cost);
            this.customName = customName;
        }

        public boolean hasCustomName() {
            return customName != null && !customName.equals("");
        }

        public String getCustomName() {
            return customName;
        }

        Material getType() {
            return material;
        }

        BigDecimal getCost() {
            return cost;
        }

        int getAmount() {
            return amount;
        }

        void setAmount(int amount) {
            this.amount = amount;
        }

        void setCost(BigDecimal cost) {
            this.cost = cost;
        }

        short getData() {
            return this.data;
        }

    }

}
