package me.realmm.warps.entities;

import me.realmm.warps.util.WarpUtil;
import org.bukkit.Location;

public abstract class AbstractWarp implements Warpable {

    private final WarpPlayer wp;
    private Location location;

    public AbstractWarp(WarpPlayer wp, Location location) {
        this.wp = wp;
        this.location = location;
    }

    public AbstractWarp(WarpPlayer wp, String serialized) {
        this.wp = wp;
        deserialize(serialized);
    }

    public WarpPlayer getWarpPlayer() {
        return wp;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    protected abstract void deserialize(String serialized);

    public abstract String serialize();

    @Override
    public TeleportResponse teleport() {
        return WarpUtil.teleport(wp, location);
    }

}
