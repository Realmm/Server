package me.realmm.goldeconomy.commands.sub;

import me.realmm.goldeconomy.entities.Economy;
import me.realmm.goldeconomy.entities.NamedItem;
import me.realmm.goldeconomy.entities.PriceResponse;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import net.jamesandrew.commons.number.Number;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceItemCommand extends SubCommand {

    public PriceItemCommand() {
        super("");
        setPlaceHolder((s, a) -> a[2]);
        setExecution((s, a) -> {
            if (!Number.isInt(a[1]) || (Material.matchMaterial(a[2]) == null && !EconUtil.isNamedItem(a[2]))) {
                s.sendMessage(Messages.INCORRECT_ARGUMENTS);
                return;
            }
            int amount = Number.getInt(a[1]);

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
                s.sendMessage(new Placeholder(Messages.ILLEGAL_MARKET_ITEM).setPlaceholders("item").setToReplace(name).toString());
                return;
            }

            PriceResponse priceResponse = Economy.getPrice(i.getType(), i.getDurability(), amount);

            if (priceResponse == PriceResponse.FAILED) {
                s.sendMessage(new Placeholder(Messages.NOT_ENOUGH_ITEMS).setPlaceholders("amount", "item", "amountLeft").setToReplace(amount, name, Economy.getAmountInStock(i.getType(), i.getDurability())).toString());
            } else {
                BigDecimal decimal = priceResponse.getPrice();
                decimal = decimal.setScale(4, RoundingMode.HALF_UP);
                s.sendMessage(new Placeholder(Messages.PRICE).setPlaceholders("amount", "item", "cost").setToReplace(amount, name, decimal.doubleValue()).toString());
            }
        });
    }

}
