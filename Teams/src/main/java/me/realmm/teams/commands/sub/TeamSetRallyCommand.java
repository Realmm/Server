package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

public class TeamSetRallyCommand extends SubCommand {

    public TeamSetRallyCommand() {
        super("setrally");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;

            if (!TeamUtil.isInTeam(p)) {
                p.sendMessage(Messages.NOT_IN_TEAM);
                return;
            }

            TeamPlayer tp = TeamUtil.getTeamPlayer(p);
            Team t = tp.getTeam();

            if (!WarpUtil.canSetWarp(p)) {
                p.sendMessage(me.realmm.warps.util.Messages.SET_WARP_SPAWN);
                return;
            }

            if (!tp.isPromoted()) {
                p.sendMessage(Messages.NOT_PROMOTED);
                return;
            }

            t.setRally(p.getLocation());
            p.sendMessage(Messages.SET_RALLY);
            t.sendMessage(new Placeholder(Messages.TEAM_SET_RALLY).setPlaceholders("player").setToReplace(p.getName()).toString(), tp);
        });
    }

}
