package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import org.bukkit.entity.Player;

public class TeamFriendlyFireCommand extends SubCommand {

    public TeamFriendlyFireCommand() {
        super("ff");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;

            if (!TeamUtil.isInTeam(p)) {
                p.sendMessage(Messages.NOT_IN_TEAM);
                return;
            }

            TeamPlayer tp = TeamUtil.getTeamPlayer(p);
            Team t = tp.getTeam();

            if (t.getFriendlyFire()) {
                t.setFriendlyFire(false);
                t.sendMessage(Messages.FF_OFF);
            } else {
                t.setFriendlyFire(true);
                t.sendMessage(Messages.FF_ON);
            }

        });
        addSubCommands(new TeamFriendlyFireOnCommand(), new TeamFriendlyFireOffCommand());
    }

    private static class TeamFriendlyFireOnCommand extends SubCommand {

        private TeamFriendlyFireOnCommand() {
            super("on");
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;

                if (!TeamUtil.isInTeam(p)) {
                    p.sendMessage(Messages.NOT_IN_TEAM);
                    return;
                }

                TeamPlayer tp = TeamUtil.getTeamPlayer(p);
                Team t = tp.getTeam();

                t.setFriendlyFire(true);
                t.sendMessage(Messages.FF_ON);
            });
        }

    }

    private static class TeamFriendlyFireOffCommand extends SubCommand {

        private TeamFriendlyFireOffCommand() {
            super("off");
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;

                if (!TeamUtil.isInTeam(p)) {
                    p.sendMessage(Messages.NOT_IN_TEAM);
                    return;
                }

                TeamPlayer tp = TeamUtil.getTeamPlayer(p);
                Team t = tp.getTeam();

                t.setFriendlyFire(false);
                t.sendMessage(Messages.FF_OFF);
            });
        }

    }

}
