package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;

public class TeamRosterCommand extends SubCommand {

    public TeamRosterCommand() {
        super("roster");
        addAlias("r");
        addSubCommands(new TeamRosterNameCommand());
    }

    private static class TeamRosterNameCommand extends SubCommand {

        private TeamRosterNameCommand() {
            super((s, a) -> a[2]);
            setExecution((s, a) -> {
                if (!TeamUtil.isTeam(getPlaceHolder())) {
                    s.sendMessage(new Placeholder(Messages.NOT_TEAM).setPlaceholders("team").setToReplace(getPlaceHolder()).toString());
                    return;
                }

                Team t = TeamUtil.getTeam(getPlaceHolder());
                s.sendMessage(TeamUtil.getInfo(t, false));
            });
        }

    }

}
