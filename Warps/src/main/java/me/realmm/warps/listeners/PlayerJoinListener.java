package me.realmm.warps.listeners;

import me.realmm.warps.util.WarpUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        WarpUtil.cacheWarpPlayer(p);
        WarpUtil.getMongoDB().createWarpCollection(p, null);
    }

}
