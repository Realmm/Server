package me.realmm.combatlog.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class CombatPlayer {

    private final OfflinePlayer o;
    private Villager villager;

    public CombatPlayer(OfflinePlayer p, Location l) {
        this.o = p;
        spawn(l);
    }

    private void spawn(Location l) {
        Villager v = (Villager) l.getWorld().spawnEntity(l, EntityType.VILLAGER);
        v.setCustomName(o.getName());
        this.villager = v;
    }

    public Villager getVillager() {
        return villager;
    }

}
