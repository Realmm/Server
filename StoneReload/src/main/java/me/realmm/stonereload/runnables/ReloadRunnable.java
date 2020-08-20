package me.realmm.stonereload.runnables;

import me.realmm.stonereload.StoneReload;
import me.realmm.stonereload.util.StoneReloadUtil;
import net.jamesandrew.realmlib.location.BlockChanger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadRunnable extends BukkitRunnable {

    @Override
    public void run() {
        BlockChanger changer = StoneReloadUtil.getChanger();
        if (changer == null || changer.isStarted() || StoneReloadUtil.isInitialising()) return;

        boolean hasCuboids = StoneReloadUtil.getCuboids().size() > 0;

        if (hasCuboids) {
            Bukkit.broadcastMessage("Loading stone");
            changer.start();
            new ReloadScoreboardRunnable().runTaskTimer(StoneReload.get(), 0L, 1L);
        }
    }

}
