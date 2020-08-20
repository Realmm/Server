package me.realmm.goldeconomy.commands.sub;

import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.RoundingMode;

public class BalancePlayerCommand extends SubCommand {

    public BalancePlayerCommand() {
        super("");
        setPlaceHolder((s, a) -> a[1]);
        setExecution((s, a) -> {
            OfflinePlayer o = Bukkit.getOfflinePlayer(a[1]);
            if (!o.hasPlayedBefore()) return;
            EconPlayer ep = EconUtil.getEconPlayer(o);
            s.sendMessage(new Placeholder(Messages.BALANCE_PLAYER).setPlaceholders("player", "amount").setToReplace(ep.getOfflinePlayer().getName(), ep.getBalance().setScale(4, RoundingMode.HALF_UP).doubleValue()).toString());
        });
    }

}
