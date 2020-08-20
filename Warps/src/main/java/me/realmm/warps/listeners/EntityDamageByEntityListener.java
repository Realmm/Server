package me.realmm.warps.listeners;

import me.realmm.warps.entities.TeleportAttackCooldownPlayer;
import me.realmm.warps.util.WarpUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Collection;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player p = (Player) e.getDamager();
        Collection<TeleportAttackCooldownPlayer> c = WarpUtil.getAttackCooldownPlayers(p);

        if (c.stream().anyMatch(TeleportAttackCooldownPlayer::isOnAttackCooldown)) e.setCancelled(true);
    }

}
