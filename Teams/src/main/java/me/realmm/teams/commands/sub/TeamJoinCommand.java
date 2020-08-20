package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

public class TeamJoinCommand extends SubCommand {

    public TeamJoinCommand() {
        super("join");
        addAlias("j");
        addSubCommands(new TeamJoinNameCommand());
    }

    private static class TeamJoinNameCommand extends SubCommand {

        private TeamJoinNameCommand() {
            super((sender, args) -> args[2]);
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;

                if (TeamUtil.isInTeam(p)) {
                    p.sendMessage(Messages.IN_TEAM);
                    return;
                }

                if (!TeamUtil.isTeam(getPlaceHolder())) {
                    p.sendMessage(new Placeholder(Messages.NOT_TEAM).setPlaceholders("team").setToReplace(getPlaceHolder()).toString());
                    return;
                }

                Team t = TeamUtil.getTeam(getPlaceHolder());

                if (t.hasPassword()) {
                    p.sendMessage(new Placeholder(Messages.TEAM_HAS_PASSWORD).setPlaceholders("team").setToReplace(t.getName()).toString());
                    return;
                }

                sendJoinMessagesAndJoin(t, p);

            });
            addSubCommands(new TeamJoinNamePasswordCommand());
        }

    }

    private static class TeamJoinNamePasswordCommand extends SubCommand {

        private TeamJoinNamePasswordCommand() {
            super((sender, args) -> args[3]);
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;

                if (TeamUtil.isInTeam(p)) {
                    p.sendMessage(Messages.IN_TEAM);
                    return;
                }

                if (!TeamUtil.isTeam(a[2])) {
                    p.sendMessage(new Placeholder(Messages.NOT_TEAM).setPlaceholders("team").setToReplace(a[2]).toString());
                    return;
                }

                Team t = TeamUtil.getTeam(a[2]);

                if (t.hasPassword()) {
                    if (t.getPassword().equalsIgnoreCase(getPlaceHolder())) {
                        sendJoinMessagesAndJoin(t, p);
                    } else {
                        p.sendMessage(Messages.INCORRECT_PASSWORD);
                    }
                    return;
                }

                sendJoinMessagesAndJoin(t, p);

            });
        }

    }

    private static void sendJoinMessagesAndJoin(Team t, Player p) {
        t.sendMessage(new Placeholder(Messages.PLAYER_JOINED_TEAM).setPlaceholders("player").setToReplace(p.getName()).toString());
        t.addMember(p);
        p.sendMessage(new Placeholder(Messages.JOINED_TEAM).setPlaceholders("team").setToReplace(t.getName()).toString());
    }

}
