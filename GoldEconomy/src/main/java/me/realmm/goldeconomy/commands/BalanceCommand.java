package me.realmm.goldeconomy.commands;

import me.realmm.goldeconomy.commands.sub.BalancePlayerCommand;
import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import net.jamesandrew.realmlib.command.BaseCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

import java.math.RoundingMode;

public class BalanceCommand extends BaseCommand {

    public BalanceCommand() {
        super("balance");
        addAlias("bal");
        addSubCommands(new BalancePlayerCommand());
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            EconPlayer ep = EconUtil.getEconPlayer(p);
            p.sendMessage(new Placeholder(Messages.BALANCE).setPlaceholders("amount").setToReplace(ep.getBalance().setScale(2, RoundingMode.HALF_UP).doubleValue()).toString());
        });
    }

}
