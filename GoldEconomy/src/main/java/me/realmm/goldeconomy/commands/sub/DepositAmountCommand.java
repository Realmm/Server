package me.realmm.goldeconomy.commands.sub;

import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import net.jamesandrew.commons.number.Number;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

public class DepositAmountCommand extends SubCommand {

    public DepositAmountCommand() {
        super("");
        setPlaceHolder((s, a) -> a[1]);
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            EconPlayer e = EconUtil.getEconPlayer(p);
            if (!Number.isInt(a[1])) return;
            int i = Number.getInt(a[1]);
            if (e.deposit(i)) {
                p.sendMessage(new Placeholder(Messages.DEPOSITED).setPlaceholders("amount").setToReplace(i).toString());
            } else {
                p.sendMessage(new Placeholder(Messages.NOT_ENOUGH_GOLD_INVENTORY).setPlaceholders("amount").setToReplace(i).toString());
            }

        });
    }

}
