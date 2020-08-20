package me.realmm.serverscoreboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        ServerScoreboard sb = JavaPlugin.getPlugin(ServerScoreboard.class);

        if (!sb.hasScoreboard(p)) sb.setScoreboard(p);
        sb.getScoreboard(p).update(p);
    }

}
