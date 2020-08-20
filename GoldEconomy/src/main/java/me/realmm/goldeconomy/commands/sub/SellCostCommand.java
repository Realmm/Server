package me.realmm.goldeconomy.commands.sub;

import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.entities.Economy;
import me.realmm.goldeconomy.entities.NamedItem;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import net.jamesandrew.commons.number.Number;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SellCostCommand extends SubCommand {

    public SellCostCommand() {
        super("");
        setPlaceHolder((s, a) -> a[3]);
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            EconPlayer ep = EconUtil.getEconPlayer(p);
            if (!Number.isInt(a[1]) || (Material.matchMaterial(a[2]) == null && !EconUtil.isNamedItem(a[2])) || !Number.isDouble(a[3])) {
                p.sendMessage(Messages.INCORRECT_ARGUMENTS);
                return;
            }
            int amount = Number.getInt(a[1]);
            double cost = Number.getDouble(a[3]);

            ItemStack i;
            String name;
            NamedItem n = EconUtil.isNamedItem(a[2]) ? EconUtil.findNamedItem(a[2]) : null;

            if (n == null) {
                i = new ItemStack(Material.matchMaterial(a[2]), amount);
                name = WordUtils.capitalize(i.getType().name().toLowerCase().replace("_", " "));
            } else {
                i = n.toItemStack(amount);
                name = n.getFormattedName();
            }

            if (!Economy.isOnMarket(i.getType())) {
                p.sendMessage(new Placeholder(Messages.ILLEGAL_MARKET_ITEM).setPlaceholders("item").setToReplace(name).toString());
                return;
            }

            if (!ep.sell(i, cost)) {
                Map<ItemStack, Integer> map = new HashMap<>();

                for (ItemStack item : p.getInventory().getContents()) {
                    if (item == null) continue;
                    for (int x = 0; x < item.getAmount(); x++) {
                        if (map.entrySet().stream().anyMatch(e -> e.getKey().isSimilar(item))) {
                            ItemStack itemStack = map.keySet().stream().filter(it -> it.isSimilar(item)).findFirst().orElseThrow(() -> new IllegalArgumentException("No similar ItemStacks"));
                            map.put(itemStack, map.get(itemStack) + 1);
                        } else {
                            map.put(item, 1);
                        }
                    }
                }

                ItemStack itemStack = map.keySet().stream().filter(it -> it.isSimilar(i)).findFirst().orElse(null);
                int inv = itemStack == null ? 0 : map.get(itemStack);

                p.sendMessage(new Placeholder(Messages.NOT_ENOUGH_SELL).setPlaceholders("amountEntered", "item", "amount").setToReplace(amount, name, inv).toString());
                return;
            }
            p.sendMessage(new Placeholder(Messages.PUT_ITEM_ON_MARKET).setPlaceholders("amount", "item", "cost").setToReplace(amount, name, cost).toString());
        });
    }

}
