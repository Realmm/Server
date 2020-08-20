package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

public class TeamPasswordCommand extends SubCommand {

    public TeamPasswordCommand() {
        super("password");
        addAlias("pass");
        addSubCommands(new TeamPasswordNameCommand());
    }

    private static class TeamPasswordNameCommand extends SubCommand {

        private TeamPasswordNameCommand() {
            super((s, a) -> a[2]);
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;

                if (!TeamUtil.isInTeam(p)) {
                    p.sendMessage(Messages.NOT_IN_TEAM);
                    return;
                }

                TeamPlayer tp = TeamUtil.getTeamPlayer(p);
                Team t = tp.getTeam();

                if (!tp.isPromoted()) {
                    p.sendMessage(Messages.NOT_PROMOTED);
                    return;
                }

                if (getPlaceHolder().equalsIgnoreCase("none")) {
                    t.setPassword(null);
                    p.sendMessage(Messages.PASSWORD_REMOVED);
                    t.sendMessage(new Placeholder(Messages.REMOVED_PASSWORD).setPlaceholders("player").setToReplace(p.getName()).toString(), tp);
                    return;
                }

                t.setPassword(getPlaceHolder());
                p.sendMessage(new Placeholder(Messages.CHANGED_PASSWORD).setPlaceholders("password").setToReplace(t.getPassword()).toString());
                t.sendMessage(new Placeholder(Messages.PASSWORD_CHANGED).setPlaceholders("player", "password").setToReplace(p.getName(), t.getPassword()).toString(), tp);

            });
        }

    }

}
