package me.realmm.track.runnables;

import me.realmm.track.utils.TrackerUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class RemoveDropsRunnable extends BukkitRunnable {
    @Override
    public void run() {
        TrackerUtil.clearDrops();
    }
}
