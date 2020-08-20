package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;

import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TeamBaltopCommand extends SubCommand {

    public TeamBaltopCommand() {
        super("baltop");
        setExecution((s, a) -> {
            List<Team> list = TeamUtil.getTeams().stream().sorted(Comparator.comparing(Team::getTotalBalance).reversed()).collect(Collectors.toList());

            if (list.isEmpty()) return;

            StringBuilder sb = new StringBuilder();
            StringBuilder lineSb = new StringBuilder();

            for (int i = 0; i < Math.min(list.size(), 10); i++) {
                Team t = list.get(i);
                lineSb.append("\n");
                lineSb.append(new Placeholder(Messages.BALTOP_LINE).setPlaceholders("position", "team", "balance").setToReplace(i + 1, t.getName(), t.getTotalBalance().setScale(2, RoundingMode.HALF_UP).doubleValue()).toString());
            }

            Messages.BALTOP.forEach(b -> {
                sb.append(new Placeholder(b).setPlaceholders("teams").setToReplace(lineSb.toString()).toString());
                sb.append("\n");
            });

            s.sendMessage(sb.toString());
        });
    }

}
