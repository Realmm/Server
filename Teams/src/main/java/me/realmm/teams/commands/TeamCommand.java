package me.realmm.teams.commands;

import me.realmm.teams.commands.sub.*;
import me.realmm.teams.util.Messages;
import net.jamesandrew.realmlib.command.BaseCommand;

public class TeamCommand extends BaseCommand {

    public TeamCommand() {
        super("team");
        addAlias("t");
        setExecution((s, a) -> {
            String st = String.join("\n", Messages.HELP);
            s.sendMessage(st);
        });
        addSubCommands(
                new TeamChatCommand(),
                new TeamCreateCommand(),
                new TeamDemoteCommand(),
                new TeamFriendlyFireCommand(),
                new TeamHqCommand(),
                new TeamInfoCommand(),
                new TeamJoinCommand(),
                new TeamKickCommand(),
                new TeamLeaveCommand(),
                new TeamListCommand(),
                new TeamPasswordCommand(),
                new TeamPromoteCommand(),
                new TeamRallyCommand(),
                new TeamRosterCommand(),
                new TeamSetHqCommand(),
                new TeamSetRallyCommand(),
                new TeamBaltopCommand(),
                new TeamLeaderCommand()
        );
    }

}
