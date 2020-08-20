package me.realmm.track.tracker;

import org.bukkit.block.BlockFace;

public enum TrackerSide {

    NORTH, EAST, SOUTH, WEST;

    public BlockFace toBlockFace() {
        switch (this) {
            case NORTH:
                return BlockFace.NORTH;
            case EAST:
                return BlockFace.EAST;
            case WEST:
                return BlockFace.WEST;
            case SOUTH:
                return BlockFace.SOUTH;
        }
        throw new IllegalArgumentException("Illegal BlockFace");
    }

    public static TrackerSide fromBlockFace(BlockFace face) {
        switch (face) {
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
        }
        throw new IllegalArgumentException("Illegal BlockFace");
    }

}
