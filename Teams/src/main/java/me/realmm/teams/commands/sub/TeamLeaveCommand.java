package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

public class TeamLeaveCommand extends SubCommand {

    public TeamLeaveCommand() {
        super("leave");
        addAlias("l");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;

            if (!TeamUtil.isInTeam(p)) {
                p.sendMessage(Messages.NOT_IN_TEAM);
                return;
            }

            TeamPlayer tp = TeamUtil.getTeamPlayer(p);
            Team t = tp.getTeam();

            if (t.getMembers().size() == 1) {
                t.sendMessage(new Placeholder(Messages.DISBANDED).setPlaceholders("team").setToReplace(t.getName()).toString());
                t.disband();
                return;
            }

            if (tp.isLeader()) {
                p.sendMessage(Messages.CANNOT_LEAVE_LEADER);
                return;
            }

            t.removeMember(tp);
            p.sendMessage(new Placeholder(Messages.LEFT_TEAM).setPlaceholders("team").setToReplace(t.getName()).toString());
            t.sendMessage(new Placeholder(Messages.PLAYER_LEFT_TEAM).setPlaceholders("player").setToReplace(p.getName()).toString());
        });
    }

}
