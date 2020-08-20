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

public class TeamLeaderCommand extends SubCommand {

    public TeamLeaderCommand() {
        super("leader");
        addSubCommands(new TeamLeaderNameCommand());
    }

    private static class TeamLeaderNameCommand extends SubCommand {

        private TeamLeaderNameCommand() {
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

                if (!tp.isLeader()) {
                    p.sendMessage(Messages.NOT_LEADER);
                    return;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(getPlaceHolder());
                if (!target.hasPlayedBefore()) {
                    p.sendMessage(new Placeholder(Messages.PLAYER_NOT_PLAYED).setPlaceholders("player").setToReplace(getPlaceHolder()).toString());
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
                    p.sendMessage(Messages.ALREADY_LEADER);
                    return;
                }

                targetPlayer.setLeader();
                t.sendMessage(new Placeholder(Messages.LEADER_GIVEN).setPlaceholders("player", "leader").setToReplace(p.getName(), target.getName()).toString(), tp);
                p.sendMessage(new Placeholder(Messages.GIVEN_LEADER).setPlaceholders("player").setToReplace(target.getName()).toString());
            });
        }

    }

}
