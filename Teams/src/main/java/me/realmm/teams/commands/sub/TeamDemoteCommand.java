package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TeamDemoteCommand extends SubCommand {

    public TeamDemoteCommand() {
        super("demote");
        addSubCommands(new TeamDemoteNameCommand());
    }

    private static class TeamDemoteNameCommand extends SubCommand {

        private TeamDemoteNameCommand() {
            super((s, a) -> a[2]);
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;

                if (!TeamUtil.isInTeam(p)) {
                    p.sendMessage(Messages.NOT_IN_TEAM);
                    return;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(a[2]);
                if (!target.hasPlayedBefore()) {
                    p.sendMessage(new Placeholder(Messages.PLAYER_NOT_PLAYED).setPlaceholders("player").setToReplace(a[2]).toString());
                    return;
                }

                TeamPlayer tp = TeamUtil.getTeamPlayer(p);
                Team t = tp.getTeam();

                if (!TeamUtil.isInTeam(target)) {
                    p.sendMessage(new Placeholder(Messages.PLAYER_NOT_ON_TEAM).setPlaceholders("player").setToReplace(target.getName()).toString());
                    return;
                }

                if (TeamUtil.getTeam(target) != t) {
                    p.sendMessage(new Placeholder(Messages.PLAYER_NOT_ON_YOUR_TEAM).setPlaceholders("player").setToReplace(target.getName()).toString());
                    return;
                }

                if (tp.isLeader()) {
                    p.sendMessage(Messages.CANNOT_DEMOTE_LEADER);
                    return;
                }

                if (!tp.isPromoted()) {
                    p.sendMessage(Messages.NOT_PROMOTED);
                    return;
                }

                TeamPlayer targetPlayer = TeamUtil.getTeamPlayer(target);

                if (targetPlayer.isPromoted()) {
                    targetPlayer.setPromoted(false);
                    p.sendMessage(new Placeholder(Messages.DEMOTED_PLAYER_SENDER).setPlaceholders("player").setToReplace(target.getName()).toString());
                    if (target.isOnline()) {
                        Player pTarget = (Player) target;
                        pTarget.sendMessage(new Placeholder(Messages.DEMOTED_PLAYER_RECEIVER).setPlaceholders("player").setToReplace(p.getName()).toString());
                    }
                    t.sendMessage(new Placeholder(Messages.DEMOTED_PLAYER_TEAM).setPlaceholders("demoted", "player").setToReplace(target.getName(), p.getName()).toString(), tp, TeamUtil.getTeamPlayer(target));
                } else {
                    p.sendMessage(new Placeholder(Messages.PLAYER_NOT_DEMOTED).setPlaceholders("player").setToReplace(target.getName()).toString());
                }

            });

        }

    }

}
