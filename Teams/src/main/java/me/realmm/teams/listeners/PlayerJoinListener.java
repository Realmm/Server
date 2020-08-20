package me.realmm.teams.listeners;

import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!TeamUtil.isInTeam(p)) return;
        TeamPlayer tp = TeamUtil.getTeamPlayer(p);
        Team t = tp.getTeam();

        tp.updateScoreboard();
        t.sendMessage(new Placeholder(Messages.LOG_IN).setPlaceholders("player").setToReplace(p.getName()).toString(), tp);
    }

}
