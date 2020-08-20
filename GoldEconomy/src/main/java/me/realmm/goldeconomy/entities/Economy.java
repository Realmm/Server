package me.realmm.goldeconomy.entities;

import me.realmm.goldeconomy.util.EconUtil;
import net.jamesandrew.realmlib.inventory.InventoryUtil;
import net.jamesandrew.realmlib.placeholder.ReplacePattern;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Economy {

    public static boolean isOnMarket(Material material) {
        Material[] illegal = {
                Material.DIAMOND_SWORD,
                Material.GOLD_SWORD,
                Material.IRON_SWORD,
                Material.STONE_SWORD,
                Material.WOOD_SWORD,
                Material.DIAMOND_PICKAXE,
                Material.GOLD_PICKAXE,
                Material.IRON_PICKAXE,
                Material.STONE_PICKAXE,
                Material.WOOD_PICKAXE,
                Material.DIAMOND_AXE,
                Material.GOLD_AXE,
                Material.IRON_AXE,
                Material.STONE_AXE,
                Material.WOOD_AXE,
                Material.DIAMOND_SPADE,
                Material.GOLD_SPADE,
                Material.IRON_SPADE,
                Material.STONE_SPADE,
                Material.WOOD_SPADE,
                Material.SHEARS,
                Material.DIAMOND_HOE,
                Material.GOLD_HOE,
                Material.IRON_HOE,
                Material.STONE_HOE,
                Material.WOOD_HOE,
                Material.CHAINMAIL_HELMET,
                Material.DIAMOND_HELMET,
                Material.GOLD_HELMET,
                Material.IRON_HELMET,
                Material.LEATHER_HELMET,
                Material.CHAINMAIL_CHESTPLATE,
                Material.DIAMOND_CHESTPLATE,
                Material.GOLD_CHESTPLATE,
                Material.IRON_CHESTPLATE,
                Material.LEATHER_CHESTPLATE,
                Material.CHAINMAIL_LEGGINGS,
                Material.DIAMOND_LEGGINGS,
                Material.GOLD_LEGGINGS,
                Material.IRON_LEGGINGS,
                Material.LEATHER_LEGGINGS,
                Material.CHAINMAIL_BOOTS,
                Material.DIAMOND_BOOTS,
                Material.GOLD_BOOTS,
                Material.IRON_BOOTS,
                Material.LEATHER_BOOTS,
                Material.ENDER_PEARL,
                Material.MONSTER_EGG,
                Material.MONSTER_EGGS,
                Material.BOW,
                Material.FISHING_ROD
        };
        return Arrays.stream(illegal).noneMatch(m -> m == material);
    }

    public static BuyResponse buy(Player purchaser, Material material, int amount, short data, double limit, String customName) {
        EconPlayer ep = EconUtil.getEconPlayer(purchaser);

        if (!InventoryUtil.canAdd(purchaser, material, amount, false)) {
            return BuyResponse.FULL_INVENTORY;
        }

        Set<Item> allItems = new HashSet<>();

        for (EconPlayer e : EconUtil.getEconPlayers()) {
            allItems.addAll(e.getItems());
        }

        List<Item> items = allItems.stream().sorted(Comparator.comparing(Item::getIndividualCost)).filter(i -> i.getType() == material && i.getData() == data).collect(Collectors.toList());

        Set<EconMapper> mapper = new HashSet<>();

        BigDecimal toSpend = BigDecimal.ZERO;
        int amountOfItems = 0;

        for (Item item : items) {
            for (int x = 0; x < item.getAmount(); x++) {
                BigDecimal toCheck = toSpend.setScale(18, RoundingMode.DOWN);
                if (item.getIndividualCost().add(toCheck).compareTo(BigDecimal.valueOf(limit)) > 0 || amountOfItems >= amount) {
                    toSpend = toCheck;
                    break;
                }

                Predicate<? super EconMapper> predicate = em -> em.getEconPlayer().getOfflinePlayer().getUniqueId().equals(item.getEconPlayer().getOfflinePlayer().getUniqueId());
                if (mapper.stream().anyMatch(predicate)) {
                    EconMapper map = mapper.stream().filter(predicate).findFirst().orElseThrow(() -> new IllegalArgumentException("No EconPlayer in EconMapper"));
                    item.setName(customName);
                    map.addItem(item);
                } else {
                    EconMapper map = new EconMapper(item.getEconPlayer());
                    item.setName(customName);
                    map.addItem(item);
                    mapper.add(map);
                }

                toSpend = toSpend.add(item.getIndividualCost());
                amountOfItems++;
            }
        }

        toSpend = toSpend.setScale(18, RoundingMode.DOWN);

        int itemsInStock = 0;

        for (Item item : items) {
            if (item.getType() != material) continue;
            itemsInStock += item.getAmount();
        }

        String name = customName == null || customName.equals("") ? WordUtils.capitalize(material.name().toLowerCase().replace("_", " ")) : customName;

        if (itemsInStock < amount) {
            return BuyResponse.NOT_ENOUGH_ITEMS.setPattern(new ReplacePattern().setPlaceholders("amount", "item", "amountLeft").setToReplace(amount, name, itemsInStock));
        }

        BigDecimal toSpendShow = EconUtil.adjustDecimal(toSpend);

        if (toSpend.compareTo(BigDecimal.valueOf(limit)) > 0) {
            return BuyResponse.INCREASE_LIMIT.setPattern(new ReplacePattern().setPlaceholders("amount", "item", "cost", "limit").setToReplace(amount, name, toSpendShow.doubleValue(), limit));
        }

        if (amountOfItems != amount) {
            PriceResponse response = getPrice(material, data, amount);
            if (response == PriceResponse.FAILED) throw new IllegalArgumentException("Failed price response");
            return BuyResponse.INCREASE_LIMIT.setPattern(new ReplacePattern().setPlaceholders("amount", "item", "cost", "limit").setToReplace(amount, name, response.getPrice().setScale(2, RoundingMode.HALF_UP).doubleValue(), limit));
        }

        if (toSpend.compareTo(ep.getBalance()) > 0) {
            return BuyResponse.INSUFFICIENT_BALANCE;
        }

        mapper.forEach(map -> map.getEconPlayer().buyFrom(ep, map.getItems()));

        ep.updateScoreboard();
        EconUtil.getMongoDB().updatePlayer(ep);
        return BuyResponse.BOUGHT_ITEM.setPattern(new ReplacePattern().setPlaceholders("amount", "item", "cost")
                .setToReplace(amount, name, toSpendShow.doubleValue()));
    }

    public static BuyResponse buy(Player purchaser, NamedItem n, int amount, double limit) {
        ItemStack i = n.toItemStack(amount);
        return buy(purchaser, i.getType(), i.getAmount(), i.getDurability(), limit, n.getFormattedName());
    }

    public static BuyResponse buy(Player purchaser, Material material, int amount, short data, double limit) {
        return buy(purchaser, material, amount, data, limit, null);
    }

    public static PriceResponse getPrice(Material material, short data, int amount) {
        Set<Item> allItems = new HashSet<>();

        for (EconPlayer e : EconUtil.getEconPlayers()) {
            allItems.addAll(e.getItems());
        }

        List<Item> items =  allItems.stream().sorted(Comparator.comparing(Item::getIndividualCost)).filter(i -> i.getType() == material && i.getData() == data).collect(Collectors.toList());

        BigDecimal price = BigDecimal.ZERO;
        int count = 0;

        outer:
        for (Item item : items) {
            for (int i = 0; i < item.getAmount(); i++) {
                if (amount == 0 || count >= amount) break outer;
                price = price.add(item.getIndividualCost());
                count++;
            }
        }

        return price.compareTo(BigDecimal.ZERO) == 0 || getAmountInStock(material, data) < amount ? PriceResponse.FAILED : PriceResponse.SUCCESS.setPrice(price.setScale(18, RoundingMode.HALF_EVEN));
    }

    public static int getAmountInStock(Material material, short data) {
        Set<Item> allItems = new HashSet<>();

        for (EconPlayer e : EconUtil.getEconPlayers()) {
            allItems.addAll(e.getItems());
        }

        List<Item> items =  allItems.stream().sorted(Comparator.comparing(Item::getIndividualCost)).filter(i -> i.getType() == material && i.getData() == data).collect(Collectors.toList());

        int count = 0;

        for (Item item : items) {
            count += item.getAmount();
        }

        return count;
    }

    private static class EconMapper {
        private final EconPlayer ep;
        private final Map<Item, Integer> items = new HashMap<>();

        EconMapper(EconPlayer ep) {
            this.ep = ep;
        }

        void addItem(Item item) {
            if (items.containsKey(item)) {
                int i = items.get(item);
                i++;
                items.put(item, i);
            } else {
                items.put(item, 1);
            }
        }

        Map<Item, Integer> getItems() {
            return items;
        }

        EconPlayer getEconPlayer() {
            return ep;
        }
    }

}
