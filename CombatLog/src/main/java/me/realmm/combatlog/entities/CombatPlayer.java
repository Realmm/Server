package me.realmm.combatlog.entities;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CombatPlayer {

    private final OfflinePlayer o;

    public CombatPlayer(OfflinePlayer p) {
        this.o = p;
    }

    public boolean isOnline() {
        return o.isOnline();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(o.getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer() {
        return o;
    }

}
