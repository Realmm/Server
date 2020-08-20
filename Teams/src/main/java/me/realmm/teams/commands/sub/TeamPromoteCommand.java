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

public class TeamPromoteCommand extends SubCommand {

    public TeamPromoteCommand() {
        super("promote");
        addAlias("p");
        addSubCommands(new TeamPromoteNameCommand());
    }

    private static class TeamPromoteNameCommand extends SubCommand {

        private TeamPromoteNameCommand() {
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

                if (targetPlayer.isPromoted()) {
                    p.sendMessage(new Placeholder(Messages.ALREADY_PROMOTED).setPlaceholders("player").setToReplace(target.getName()).toString());
                    return;
                }

                t.setPromoted(targetPlayer, true);
                p.sendMessage(new Placeholder(Messages.PROMOTED_PLAYER).setPlaceholders("player").setToReplace(target.getName()).toString());
                t.sendMessage(new Placeholder(Messages.PLAYER_PROMOTED).setPlaceholders("player", "promoter").setToReplace(target.getName(), p.getName()).toString(), tp);

            });
        }

    }

}
