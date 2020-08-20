package me.realmm.stonereload.listeners;

import me.realmm.stonereload.entities.ReloadPlayer;
import me.realmm.stonereload.util.StoneReloadUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        StoneReloadUtil.cacheReloadPlayer(new ReloadPlayer(e.getPlayer()));
        ReloadPlayer r = StoneReloadUtil.getReloadPlayer(e.getPlayer());
        r.updateScoreboard();
    }

}
