package me.realmm.spawnprotection.utils;

import me.realmm.spawnprotection.SpawnProtection;
import me.realmm.spawnprotection.entities.ProtectPlayer;
import me.realmm.spawnprotection.entities.Protection;
import me.realmm.warps.util.WarpUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public final class ProtectUtil {

    private static Protection protection;
    private static final Set<ProtectPlayer> protectPlayers = new HashSet<>();

    private ProtectUtil(){}

    public static void cacheProtectPlayers() {
        Bukkit.getOnlinePlayers().forEach(ProtectUtil::cacheProtectPlayer);
    }

    public static void cacheProtectPlayer(Player p) {
        if (protectPlayers.stream().noneMatch(pl -> pl.getOfflinePlayer().getUniqueId().equals(p.getUniqueId()))) {
            ProtectPlayer pp = new ProtectPlayer(p);
            WarpUtil.registerAttackCooldownPlayer(pp);
            protectPlayers.add(pp);
        }
    }

    public static ProtectPlayer getProtectPlayer(Player p) {
        return protectPlayers.stream().filter(pp -> pp.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())).findFirst().orElseThrow(() -> new IllegalArgumentException("Player not cached as ProtectPlayer"));
    }

    public static void cachePointsFromSave() {
        FileConfiguration c = SpawnProtection.get().getConfig();
        Location one = (Location) c.get("data.point-1");
        Location two = (Location) c.get("data.point-2");
        if (one != null && two != null) {
            protection = new Protection(one, two);
            return;
        }
        protection = new Protection();
    }

    public static void setProtection(Protection protection) {
        ProtectUtil.protection = protection;
    }

    public static Protection getProtection() {
        return protection;
    }

}
