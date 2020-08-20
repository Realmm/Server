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

public class TeamInfoCommand extends SubCommand {

    public TeamInfoCommand() {
        super("info");
        addAlias("i");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;

            if (!TeamUtil.isInTeam(p)) {
                p.sendMessage(Messages.NOT_IN_TEAM);
                return;
            }

            TeamPlayer tp = TeamUtil.getTeamPlayer(p);
            Team t = tp.getTeam();

            p.sendMessage(TeamUtil.getInfo(t, true));
        });
        addSubCommands(new TeamInfoNameCommand());
    }

    private static class TeamInfoNameCommand extends SubCommand {

        public TeamInfoNameCommand() {
            super((s, a) -> a[2]);
            setExecution((s, a) -> {
                OfflinePlayer target = Bukkit.getOfflinePlayer(getPlaceHolder());
                if (!target.hasPlayedBefore()) {
                    s.sendMessage(new Placeholder(Messages.PLAYER_NOT_PLAYED).setPlaceholders("player").setToReplace(getPlaceHolder()).toString());
                    return;
                }

                if (!TeamUtil.isInTeam(target)) {
                    s.sendMessage(new Placeholder(Messages.PLAYER_NOT_ON_TEAM).setPlaceholders("player").setToReplace(target.getName()).toString());
                    return;
                }

                TeamPlayer targetPlayer = TeamUtil.getTeamPlayer(target);
                Team t = targetPlayer.getTeam();

                s.sendMessage(TeamUtil.getInfo(t, false));
            });
        }

    }

}
