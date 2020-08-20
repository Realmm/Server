package me.realmm.spawnprotection.listeners;

import me.realmm.spawnprotection.entities.ProtectPlayer;
import me.realmm.spawnprotection.utils.ProtectUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        ProtectPlayer pp = ProtectUtil.getProtectPlayer(p);
        ProtectPlayer dp = ProtectUtil.getProtectPlayer(damager);

        if (!pp.isProtected() && dp.isProtected()) dp.setProtected(false);

        if (pp.isProtected()) e.setCancelled(true);
    }

}
