package me.realmm.warps.entities;

import net.jamesandrew.realmlib.location.LocationSerializer;
import org.bukkit.Location;

public class Home extends AbstractWarp {

    public Home(WarpPlayer wp, Location location) {
        super(wp, location);
    }

    public Home(WarpPlayer wp, String serialized) {
        super(wp, serialized);
    }

    @Override
    protected void deserialize(String serialized) {
        if (!LocationSerializer.isFormatted(serialized)) throw new IllegalArgumentException("Tried to create Home from non-serialized string");
        setLocation(LocationSerializer.deserialize(serialized));
    }

    @Override
    public String serialize() {
        return LocationSerializer.serialize(getLocation());
    }

}
