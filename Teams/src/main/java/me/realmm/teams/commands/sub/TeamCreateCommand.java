package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

public class TeamCreateCommand extends SubCommand {

    public TeamCreateCommand() {
        super("create");
        addSubCommands(new TeamCreateNameCommand());
        setExecution((s, a) -> {

        });
    }

    private static class TeamCreateNameCommand extends SubCommand {

        private TeamCreateNameCommand() {
            super("");
            addSubCommands(new TeamCreatePasswordCommand());
            setPlaceHolder((s, a) -> a[2]);
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;

                if (TeamUtil.isInTeam(p)) {
                    p.sendMessage(Messages.IN_TEAM);
                    return;
                }

                if (TeamUtil.isTeam(getPlaceHolder())) {
                    p.sendMessage(new Placeholder(Messages.TEAM_NAME_USED).setPlaceholders("team").setToReplace(TeamUtil.getTeam(getPlaceHolder()).getName()).toString());
                    return;
                }

                TeamUtil.registerTeam(new Team(getPlaceHolder(), p));
                TeamUtil.getTeamPlayer(p).updateScoreboard();
                p.sendMessage(new Placeholder(Messages.CREATED_NO_PASS).setPlaceholders("team").setToReplace(getPlaceHolder()).toString());
            });
        }

    }

    private static class TeamCreatePasswordCommand extends SubCommand {

        private TeamCreatePasswordCommand() {
            super("");
            setPlaceHolder((s, a) -> a[3]);
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;

                if (TeamUtil.isInTeam(p)) {
                    p.sendMessage(Messages.IN_TEAM);
                    return;
                }

                if (TeamUtil.isTeam(a[2])) {
                    p.sendMessage(new Placeholder(Messages.TEAM_NAME_USED).setPlaceholders("team").setToReplace(TeamUtil.getTeam(a[2]).getName()).toString());
                    return;
                }

                Team t = new Team(a[2], p);
                t.setPassword(getPlaceHolder());
                TeamUtil.registerTeam(t);
                p.sendMessage(new Placeholder(Messages.CREATED_PASS).setPlaceholders("team", "password").setToReplace(a[2], getPlaceHolder()).toString());
            });
        }

    }

}
