package me.realmm.spawnprotection.entities;

import me.realmm.spawnprotection.SpawnProtection;
import me.realmm.spawnprotection.utils.ProtectUtil;
import net.jamesandrew.realmlib.shapes.Cuboid;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Protection {

    private Location point1, point2;
    private final Set<UUID> setting = new HashSet<>();

    public Protection(Location point1, Location point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Protection() {}

    public boolean isInside(Player p) {
        if (!isEnabled() || setting.size() > 0) return false;
        Location loc = p.getLocation();
        return new Cuboid(point1, point2).contains(loc);
    }

    public void setPointOne(Player p) {
        this.point1 = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
        this.point2 = null;
        setting.add(p.getUniqueId());
    }

    public void setPointTwo(Player p) {
        if (!setting.contains(p.getUniqueId())) throw new IllegalArgumentException("Player must set first point before setting second point");
        this.point2 = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
        setting.remove(p.getUniqueId());
    }

    public void save() {
        SpawnProtection.get().getConfig().set("data.point-1", point1);
        SpawnProtection.get().getConfig().set("data.point-2", point2);
        SpawnProtection.get().saveConfig();
        SpawnProtection.get().reloadConfig();
        ProtectUtil.setProtection(this);
    }

    boolean isSetting(Player p) {
        return setting.contains(p.getUniqueId());
    }

    public boolean isEnabled() {
        return point1 != null && point2 != null;
    }

}
