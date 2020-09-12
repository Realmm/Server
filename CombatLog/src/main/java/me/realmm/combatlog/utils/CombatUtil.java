package me.realmm.combatlog.utils;

import me.realmm.combatlog.CombatLog;
import me.realmm.combatlog.entities.CombatPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class CombatUtil {

    private CombatUtil(){}

    private static Set<OfflinePlayer> clearInventoryPlayers = new HashSet<>();

    public static void registerToClear(OfflinePlayer o) {
        clearInventoryPlayers.add(o);
    }

    public static boolean isRegisteredToClear(OfflinePlayer o) {
        return clearInventoryPlayers.contains(o);
    }

}
