package me.realmm.combatlog.utils;

import me.realmm.combatlog.CombatLog;
import me.realmm.combatlog.entities.CombatPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class CombatUtil {

    private CombatUtil(){}

    private static Set<CombatPlayer> combatPlayers = new HashSet<>();

    public static CombatPlayer getCombatPlayer(OfflinePlayer p) {
        return combatPlayers.stream().filter(e -> e.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())).findFirst().orElseThrow(() -> new IllegalArgumentException("Player not registered as WarpPlayer"));
    }

    public static void cacheCombatPlayer(OfflinePlayer p) {
        if (combatPlayers.stream().anyMatch(e -> e.getOfflinePlayer().getUniqueId().equals(p.getUniqueId()))) return;
        CombatPlayer c = new CombatPlayer(p);
        combatPlayers.add(c);
    }

    public static void cacheCombatPlayers() {
        if (!combatPlayers.isEmpty()) throw new IllegalArgumentException("Attempted to cache all CombatPlayers on non-empty set");
        List<String> clearInventory = JavaPlugin.getPlugin(CombatLog.class).getData().getConfig().getStringList("data");
        Set<CombatPlayer> players = warpMongo.getWarpPlayers().join();
        players.forEach(c -> cacheCombatPlayer(c.getOfflinePlayer()));
    }

    public static Collection<CombatPlayer> getCombatPlayers() {
        return Collections.unmodifiableCollection(combatPlayers);
    }

}
