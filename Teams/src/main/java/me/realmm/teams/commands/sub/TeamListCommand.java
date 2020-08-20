package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;

import java.util.List;
import java.util.stream.Collectors;

public class TeamListCommand extends SubCommand {

    public TeamListCommand() {
        super("list");
        setExecution((s, a) -> {
            List<Team> list = TeamUtil.getTeams().stream().sorted((o, oo) -> oo.getOnline().size() - o.getOnline().size()).collect(Collectors.toList());

            if (list.isEmpty()) return;

            StringBuilder sb = new StringBuilder();
            StringBuilder lineSb = new StringBuilder();

            for (int i = 0; i < Math.min(list.size(), 5); i++) {
                Team t = list.get(i);
                lineSb.append("\n");
                String st = new Placeholder(Messages.LIST_LINE).setPlaceholders("online", "total", "team").setToReplace(t.getOnline().size(), t.getMembers().size(), t.getName()).toString();
                lineSb.append(st);
            }

            Messages.LIST.forEach(b -> {
                String st = new Placeholder(b).setPlaceholders("teams").setToReplace(lineSb.toString()).toString();
                sb.append(st);
                sb.append("\n");
            });

            s.sendMessage(sb.toString());
        });
    }

}
