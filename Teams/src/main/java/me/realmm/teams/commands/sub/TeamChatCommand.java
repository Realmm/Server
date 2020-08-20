package me.realmm.teams.commands.sub;

import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import org.bukkit.entity.Player;

public class TeamChatCommand extends SubCommand {

    public TeamChatCommand() {
        super("chat");
        addAlias("c");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;

            if (!TeamUtil.isInTeam(p)) {
                p.sendMessage(Messages.NOT_IN_TEAM);
                return;
            }

            TeamPlayer tp = TeamUtil.getTeamPlayer(p);

            if (tp.isToggled()) {
                tp.setToggled(false);
                p.sendMessage(Messages.CHAT_OFF);
            } else {
                tp.setToggled(true);
                p.sendMessage(Messages.CHAT_ON);
            }

        });
    }

}
