package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import me.realmm.warps.entities.TeleportResponse;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import org.bukkit.entity.Player;

public class TeamRallyCommand extends SubCommand {

    public TeamRallyCommand() {
        super("rally");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;

            if (!TeamUtil.isInTeam(p)) {
                p.sendMessage(Messages.NOT_IN_TEAM);
                return;
            }

            TeamPlayer tp = TeamUtil.getTeamPlayer(p);
            Team t = tp.getTeam();

            if (t.hasRally()) {
                TeleportResponse response = WarpUtil.teleport(tp, t.getRally());

                switch (response) {
                    case SUCCESS:
                        p.sendMessage(me.realmm.warps.util.Messages.ATTACK_COOLDOWN);
                        break;
                    case INSIDE_SPAWN:
                        p.sendMessage(me.realmm.warps.util.Messages.WARP_SPAWN);
                        break;
                    case CLOSE_PLAYER:
                        p.sendMessage(me.realmm.warps.util.Messages.NEARBY);
                        tp.setWarpCooldown(t.getRally(), true);
                        break;
                }
            } else {
                p.sendMessage(Messages.RALLY_NOT_SET);
            }

        });
    }

}
