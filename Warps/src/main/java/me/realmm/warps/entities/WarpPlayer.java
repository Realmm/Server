package me.realmm.warps.entities;

import me.realmm.warps.Warps;
import me.realmm.warps.database.WarpMongo;
import me.realmm.warps.runnables.OverwriteRunnable;
import me.realmm.warps.util.ConfigUtil;
import me.realmm.warps.util.WarpUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

public class WarpPlayer extends TeleportAttackCooldownPlayer {

    private Set<Warp> warps = new HashSet<>();
    private final WarpMongo warpMongo;
    private OverwriteRunnable runnable;
    private Home home;

    public WarpPlayer(OfflinePlayer p) {
        super(p);
        this.warpMongo = WarpUtil.getMongoDB();
    }

    public boolean canSetWarp() {
        return WarpUtil.canSetWarp(getOfflinePlayer());
    }

    public void addWarp(Warp warp) {
        if (hasWarp(warp.getName())) throw new IllegalArgumentException("Player already has warp " + warp.getName());
        warps.add(warp);
        warpMongo.updatePlayer(this);
    }

    public void removeWarp(Warp warp) {
        warps.remove(warp);
        warpMongo.updatePlayer(this);
    }

    public int getMaxWarps() {
        if (!isOnline()) throw new IllegalArgumentException("Cannot get max warps of offline player");
        return getPlayer().getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).filter(perm -> {
            String[] a = perm.split("warps");
            if (a.length != 2) return false;
            return ConfigUtil.isWarpRank(a[1]);
        }).max(Comparator.comparing(ConfigUtil::getWarpAmount)).map(ConfigUtil::getWarpAmount).orElse(5);
    }

    public Collection<Warp> getWarps() {
        return Collections.unmodifiableCollection(warps);
    }

    public boolean hasWarp(String name) {
        return getWarps().stream().anyMatch(w -> w.getName().equalsIgnoreCase(name));
    }

    public Warp findWarp(String name) {
        return getWarps().stream().filter(w -> w.getName().equalsIgnoreCase(name)).findFirst().orElseThrow(() -> new IllegalArgumentException("No warp found"));
    }

    public boolean hasHome() {
        return home != null;
    }

    public void setHome(Home home) {
        this.home = home;
        warpMongo.updatePlayer(this);
    }

    public Home getHome() {
        return home;
    }

    public void setWarps(Set<Warp> warps) {
        this.warps = warps;
        warpMongo.updatePlayer(this);
    }

    public boolean isOverwriting() {
        return runnable != null;
    }

    public void beginOverwriting(Location location, Warp warp) {
        if (runnable != null) runnable.cancel();
        runnable = new OverwriteRunnable(this, location, warp);
        runnable.runTaskLater(Warps.get(), ConfigUtil.OVERWRITING_DURATION * 20);
    }

    public void stopOverwriting() {
        if (runnable != null) runnable.cancel();
        runnable = null;
    }

    public OverwriteRunnable getOverwriting() {
        return runnable;
    }

    public boolean isOnline() {
        return getOfflinePlayer().isOnline();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getOfflinePlayer().getUniqueId());
    }

}
