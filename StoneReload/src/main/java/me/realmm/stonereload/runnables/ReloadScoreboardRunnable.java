package me.realmm.stonereload.runnables;

import me.realmm.stonereload.entities.ReloadPlayer;
import me.realmm.stonereload.util.StoneReloadUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadScoreboardRunnable extends BukkitRunnable {
    @Override
    public void run() {
        if (!StoneReloadUtil.isReloading() || !StoneReloadUtil.hasChanger() || !StoneReloadUtil.getChanger().isStarted()) {
            cancel();
            return;
        }
        StoneReloadUtil.getReloadPlayers().forEach(ReloadPlayer::updateScoreboard);
    }
}
