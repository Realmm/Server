package me.realmm.goldeconomy.commands.sub;

import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.entities.WithdrawResponse;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import net.jamesandrew.commons.number.Number;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

public class WithdrawAmountCommand extends SubCommand {

    public WithdrawAmountCommand() {
        super("");
        setPlaceHolder((s, a) -> a[1]);
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            EconPlayer e = EconUtil.getEconPlayer(p);
            if (!Number.isInt(a[1])) return;
            int i = Number.getInt(a[1]);

            WithdrawResponse response = e.withdraw(i);

            switch (response) {
                case SUCCESS:
                    p.sendMessage(new Placeholder(Messages.WITHDRAWN).setPlaceholders("amount").setToReplace(i).toString());
                    break;
                case INSUFFICIENT_FUNDS:
                    p.sendMessage(Messages.INSUFFICIENT_BALANCE);
                    break;
                case FULL_INVENTORY:
                    p.sendMessage(Messages.FULL_INVENTORY);
                    break;
            }
        });
    }

}
