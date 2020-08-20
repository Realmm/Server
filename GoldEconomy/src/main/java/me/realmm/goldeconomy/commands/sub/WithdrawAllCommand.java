package me.realmm.goldeconomy.commands.sub;

import me.realmm.goldeconomy.entities.EconPlayer;
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
            if (e.withdraw(amount)) {
                p.sendMessage(new Placeholder(Messages.WITHDRAWN).setPlaceholders("amount").setToReplace(amount).toString());
            } else {
                p.sendMessage(Messages.FULL_INVENTORY);
            }
        });
    }

}
