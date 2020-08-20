package me.realmm.disabledrops.listeners;

import me.realmm.disabledrops.utils.ConfigUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void on(EntityDeathEvent e) {
        List<ItemStack> drops = e.getDrops();

        Location loc = e.getEntity().getLocation();

        drops.stream()
                .filter(i -> ConfigUtil.getDisabledMaterials().stream().anyMatch(m -> m != i.getType()))
                .forEach(i -> loc.getWorld().dropItemNaturally(loc, i));

        e.getDrops().clear();
    }

}
