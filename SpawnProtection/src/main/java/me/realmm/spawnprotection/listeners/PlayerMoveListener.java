package me.realmm.spawnprotection.listeners;

import me.realmm.spawnprotection.entities.ProtectPlayer;
import me.realmm.spawnprotection.entities.Protection;
import me.realmm.spawnprotection.utils.ProtectUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void on(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ProtectPlayer pp = ProtectUtil.getProtectPlayer(p);
        Protection prot = ProtectUtil.getProtection();
        if (!prot.isEnabled()) return;
        if (!prot.isInside(p)) pp.setProtected(false);
    }

}
