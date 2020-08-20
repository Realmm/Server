package me.realmm.spawnprotection.listeners;

import me.realmm.spawnprotection.entities.ProtectPlayer;
import me.realmm.spawnprotection.utils.ProtectUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {

    @EventHandler
    public void on(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();

        ProtectPlayer pp = ProtectUtil.getProtectPlayer(p);

        if (pp.isProtected()) e.setCancelled(true);
    }

}
