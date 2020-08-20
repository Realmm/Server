package me.realmm.warps.runnables;

import me.realmm.warps.entities.Warp;
import me.realmm.warps.entities.WarpPlayer;
import me.realmm.warps.util.Messages;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class OverwriteRunnable extends BukkitRunnable {

    private final WarpPlayer wp;
    private final Warp warp;
    private final Location location;

    public OverwriteRunnable(WarpPlayer wp, Location newLoc, Warp warp) {
        this.wp = wp;
        if (!wp.hasWarp(warp.getName())) throw new IllegalArgumentException("Player not not have this warp");
        this.warp = warp;
        this.location = newLoc;
    }

    @Override
    public void run() {
        wp.stopOverwriting();

        if (!wp.isOnline()) return;
        wp.getPlayer().sendMessage(new Placeholder(Messages.OVERWRITE_OUT_OF_TIME).setPlaceholders("warp").setToReplace(warp.getName()).toString());
    }

    public Warp overwrite() {
        wp.findWarp(warp.getName()).setLocation(location);
        wp.stopOverwriting();
        return warp;
    }

}
