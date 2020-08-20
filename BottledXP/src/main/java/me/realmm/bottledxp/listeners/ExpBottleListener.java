package me.realmm.bottledxp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;

public class ExpBottleListener implements Listener {

    @EventHandler
    public void on(ExpBottleEvent e) {
        e.setExperience(0);
    }

}
