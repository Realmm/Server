package me.realmm.spawnprotection.listeners;

import me.realmm.spawnprotection.utils.ProtectUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        ProtectUtil.cacheProtectPlayer(e.getPlayer());
    }

}
