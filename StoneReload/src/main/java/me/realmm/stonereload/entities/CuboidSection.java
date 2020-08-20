package me.realmm.stonereload.entities;

import net.jamesandrew.realmlib.location.LocationSerializer;
import net.jamesandrew.realmlib.shapes.Cuboid;
import org.bukkit.Location;

public class CuboidSection extends Cuboid {

    private final String split = "#";

    public CuboidSection(Location l1, Location l2) {
        super(l1, l2);
    }

    public CuboidSection(String serialized) {
        String[] split = serialized.split(this.split);
        if (split.length != 2) throw new IllegalArgumentException("Illegal serialized string for CuboidSection");

        setLocationOne(LocationSerializer.deserialize(split[0]));
        setLocationTwo(LocationSerializer.deserialize(split[1]));
    }

    public String serialize() {
        return String.join(split, LocationSerializer.serialize(getLocationOne()), LocationSerializer.serialize(getLocationTwo()));
    }

}
