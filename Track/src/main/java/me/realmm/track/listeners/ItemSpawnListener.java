package me.realmm.track.listeners;

import me.realmm.track.utils.TrackerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemSpawnListener implements Listener {

    @EventHandler
    public void on(ItemSpawnEvent e) {
        if (TrackerUtil.isDrop(e.getEntity())) e.getEntity().remove();
    }

}
