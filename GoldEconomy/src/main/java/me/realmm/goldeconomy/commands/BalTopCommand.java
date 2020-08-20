package me.realmm.goldeconomy.commands;

import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import net.jamesandrew.realmlib.command.BaseCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;

import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BalTopCommand extends BaseCommand {

    public BalTopCommand(){
        super("baltop");
        setExecution((s, a) -> {

            List<EconPlayer> list = EconUtil.getEconPlayers().stream().sorted(Comparator.comparing(EconPlayer::getBalance).reversed()).collect(Collectors.toList());

            if (list.isEmpty()) return;

            StringBuilder sb = new StringBuilder(Messages.BALTOP_TITLE);

            for (int i = 0; i < Math.min(list.size(), 10); i++) {
                EconPlayer e = list.get(i);
                sb.append("\n");
                sb.append(new Placeholder(Messages.BALTOP_LINE).setPlaceholders("position", "player", "balance").setToReplace(i + 1, e.getOfflinePlayer().getName(), e.getBalance().setScale(2, RoundingMode.HALF_UP).doubleValue()).toString());
            }

            s.sendMessage(sb.toString());
        });
    }

}
