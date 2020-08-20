package me.realmm.warps.util;

import me.realmm.spawnprotection.SpawnProtection;
import me.realmm.spawnprotection.entities.ProtectPlayer;
import me.realmm.spawnprotection.utils.ProtectUtil;
import me.realmm.warps.database.WarpMongo;
import me.realmm.warps.entities.TeleportAttackCooldownPlayer;
import me.realmm.warps.entities.TeleportResponse;
import me.realmm.warps.entities.WarpPlayer;
import net.jamesandrew.commons.database.mongo.callback.IDatabaseResultCallback;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class WarpUtil {

    private static WarpMongo warpMongo;
    private static final Set<WarpPlayer> warpPlayers = new HashSet<>();
    private static final Set<TeleportAttackCooldownPlayer> attackCooldownPlayers = new HashSet<>();

    private WarpUtil(){}

    public static void initMongoDB(IDatabaseResultCallback<Void> callback) {
        warpMongo = new WarpMongo(callback);
    }

    public static WarpMongo getMongoDB() {
        return warpMongo;
    }

    public static TeleportResponse teleport(TeleportAttackCooldownPlayer a, Location location) {
        return teleport(a, location, true);
    }

    public static TeleportResponse teleport(TeleportAttackCooldownPlayer a, Location location, boolean attackCooldown) {
        if (!a.isOnline()) return TeleportResponse.FAILED;
        SpawnProtection spawnProtection = (SpawnProtection) Bukkit.getPluginManager().getPlugin("SpawnProtection");
        if (spawnProtection != null) {
            ProtectPlayer pp = ProtectUtil.getProtectPlayer(a.getPlayer());
            if (pp.isProtected()) return TeleportResponse.INSIDE_SPAWN;
        }
        if (a.canWarp()) {
            a.getPlayer().teleport(location);
            if (attackCooldown) a.setAttackCooldown(true);
            return TeleportResponse.SUCCESS;
        } else {
            a.setWarpCooldown(location, true);
            return TeleportResponse.CLOSE_PLAYER;
        }
    }

    public static Collection<TeleportAttackCooldownPlayer> getAttackCooldownPlayers(OfflinePlayer p) {
        return attackCooldownPlayers.stream().filter(e -> e.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())).collect(Collectors.toSet());
    }

    public static void removeAttackCooldownPlayer(OfflinePlayer p, Class<? extends TeleportAttackCooldownPlayer> clazz) {
        Iterator<TeleportAttackCooldownPlayer> iter = attackCooldownPlayers.iterator();
        iter.forEachRemaining(t -> {
            if (!t.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())) return;
            if (clazz.isAssignableFrom(t.getClass())) iter.remove();
        });
    }

    public static boolean isAttackCooldownRegistered(TeleportAttackCooldownPlayer t) {
        return attackCooldownPlayers.contains(t);
    }

    public static void setAttackCooldown(boolean state, OfflinePlayer o) {
        getAttackCooldownPlayers(o).forEach(t -> t.setAttackCooldown(state));
    }

    public static void setWarpCooldown(Location loc, boolean state, OfflinePlayer o) {
        getAttackCooldownPlayers(o).forEach(t -> t.setWarpCooldown(loc, state));
    }

    public static void registerAttackCooldownPlayer(TeleportAttackCooldownPlayer attackCooldownable) {
        attackCooldownPlayers.add(attackCooldownable);
    }

    public static WarpPlayer getWarpPlayer(OfflinePlayer p) {
        return warpPlayers.stream().filter(e -> e.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())).findFirst().orElseThrow(() -> new IllegalArgumentException("Player not registered as WarpPlayer"));
    }

    public static void cacheWarpPlayer(Player p) {
        if (warpPlayers.stream().anyMatch(e -> e.getOfflinePlayer().getUniqueId().equals(p.getUniqueId()))) return;
        WarpPlayer wp = new WarpPlayer(p);
        warpPlayers.add(wp);
        attackCooldownPlayers.add(wp);
    }

    public static boolean canSetWarp(OfflinePlayer p) {
        if (!p.isOnline()) return false;
        Location l = p.getPlayer().getLocation();
        int i = 512;
        return !(Math.abs(l.getX()) < i) || !(Math.abs(l.getZ()) < i);
    }

    public static void cacheWarpPlayers() {
        if (!warpPlayers.isEmpty()) throw new IllegalArgumentException("Attempted to cache all WarpPlayers on non-empty set");
        Set<WarpPlayer> players = warpMongo.getWarpPlayers().join();
        warpPlayers.addAll(players);
        attackCooldownPlayers.addAll(players);
    }

    public static Collection<WarpPlayer> getWarpPlayers() {
        return Collections.unmodifiableCollection(warpPlayers);
    }

}
