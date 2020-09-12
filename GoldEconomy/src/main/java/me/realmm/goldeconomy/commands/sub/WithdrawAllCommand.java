package me.realmm.goldeconomy.commands.sub;

import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.entities.WithdrawResponse;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

public class WithdrawAllCommand extends SubCommand {

    public WithdrawAllCommand() {
        super("all");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            EconPlayer e = EconUtil.getEconPlayer(p);

            int amount = (int) Math.floor(e.getBalance().doubleValue());

            WithdrawResponse response = e.withdraw(amount);

            switch (response) {
                case SUCCESS:
                    p.sendMessage(new Placeholder(Messages.WITHDRAWN).setPlaceholders("amount").setToReplace(amount).toString());
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
