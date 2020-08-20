package me.realmm.goldeconomy.commands.sub;

import me.realmm.goldeconomy.entities.BuyResponse;
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

public class BuyLimitCommand extends SubCommand {

    public BuyLimitCommand() {
        super("");
        setPlaceHolder((s, a) -> a[3]);
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;

            if (!Number.isInt(a[1]) || (Material.matchMaterial(a[2]) == null && !EconUtil.isNamedItem(a[2])) || !Number.isDouble(a[3])) {
                p.sendMessage(Messages.INCORRECT_ARGUMENTS);
                return;
            }

            int amount = Number.getInt(a[1]);
            double limit = Number.getDouble(a[3]);

            NamedItem n = EconUtil.isNamedItem(a[2]) ? EconUtil.findNamedItem(a[2]) : null;
            ItemStack i;
            String name;

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

            BuyResponse response = n == null ? Economy.buy(p, i.getType(), i.getAmount(), i.getDurability(), limit) : Economy.buy(p, n, amount, limit);

            switch(response) {
                case FULL_INVENTORY:
                    p.sendMessage(Messages.FULL_INVENTORY);
                    break;
                case NOT_ENOUGH_ITEMS:
                    p.sendMessage(new Placeholder(Messages.NOT_ENOUGH_ITEMS, response.getPattern()).toString());
                    break;
                case INCREASE_LIMIT:
                    p.sendMessage(new Placeholder(Messages.INCREASE_LIMIT, response.getPattern()).toString());
                    break;
                case INSUFFICIENT_BALANCE:
                    p.sendMessage(Messages.INSUFFICIENT_BALANCE);
                    break;
                case BOUGHT_ITEM:
                    p.sendMessage(new Placeholder(Messages.BOUGHT_ITEM, response.getPattern()).toString());
                    break;
            }
        });
    }

}
