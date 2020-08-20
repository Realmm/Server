package me.realmm.warps.entities;

import net.jamesandrew.realmlib.location.LocationSerializer;
import org.bukkit.Location;

public class Warp extends AbstractWarp {

    private String name;
    private final String split = "&";

    public Warp(WarpPlayer wp, String name, Location location) {
        super(wp, location);
        this.name = name;
    }

    public Warp(WarpPlayer wp, String serialized) {
        super(wp, serialized);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void deserialize(String serialized) {
        String[] a = serialized.split(split);

        if (a.length != 2) throw new IllegalArgumentException("Tried to create Warp from non-serialized string");

        this.name = a[0];
        setLocation(LocationSerializer.deserialize(a[1]));
    }

    @Override
    public String serialize() {
        return name + split + LocationSerializer.serialize(getLocation());
    }

}
