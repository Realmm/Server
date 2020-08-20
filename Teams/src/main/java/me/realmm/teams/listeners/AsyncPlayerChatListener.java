package me.realmm.teams.listeners;

import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.Messages;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void on(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!TeamUtil.isInTeam(p)) return;

        TeamPlayer tp = TeamUtil.getTeamPlayer(p);

        if (!tp.isToggled()) return;
        e.setCancelled(true);

        tp.getTeam().sendMessage(new Placeholder(Messages.CHAT).setPlaceholders("player", "message", "team").setToReplace(tp.getAppropriateColor() + p.getName(), e.getMessage(), tp.getTeam().getName()).toString());
    }

}
