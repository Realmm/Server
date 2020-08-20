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

public class TeamKickCommand extends SubCommand {

    public TeamKickCommand() {
        super("kick");
        addAlias("k");
        addSubCommands(new TeamKickNameCommand());
    }

    private static class TeamKickNameCommand extends SubCommand {

        private TeamKickNameCommand() {
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

                OfflinePlayer target = Bukkit.getOfflinePlayer(a[2]);
                if (!target.hasPlayedBefore()) {
                    p.sendMessage(new Placeholder(Messages.PLAYER_NOT_PLAYED).setPlaceholders("player").setToReplace(a[2]).toString());
                    return;
                }

                if (!TeamUtil.isInTeam(target)) {
                    p.sendMessage(new Placeholder(Messages.PLAYER_NOT_ON_TEAM).setPlaceholders("player").setToReplace(target.getName()).toString());
                    return;
                }

                if (TeamUtil.getTeam(target) != t) {
                    p.sendMessage(new Placeholder(Messages.PLAYER_NOT_ON_YOUR_TEAM).setPlaceholders("player").setToReplace(target.getName()).toString());
                    return;
                }

                TeamPlayer targetPlayer = TeamUtil.getTeamPlayer(target);

                if (target.getUniqueId().equals(p.getUniqueId())) {
                    p.sendMessage(Messages.CANNOT_KICK_SELF);
                    return;
                }

                if (targetPlayer == t.getLeader()) {
                    p.sendMessage(Messages.CANNOT_KICK_LEADER);
                    return;
                }

                t.sendMessage(new Placeholder(Messages.PLAYER_KICKED).setPlaceholders("player", "kicker").setToReplace(target.getName(), p.getName()).toString(), tp);
                p.sendMessage(new Placeholder(Messages.YOU_KICKED_PLAYER).setPlaceholders("player").setToReplace(target.getName()).toString());
                t.removeMember(targetPlayer);
            });
        }

    }

}
