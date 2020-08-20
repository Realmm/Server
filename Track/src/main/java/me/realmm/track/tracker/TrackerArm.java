package me.realmm.track.tracker;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackerArm {

    private final Location start;
    private final TrackerSide side;
    private final Tracker tracker;
    private final List<Block> blocks;

    public TrackerArm(Tracker tracker, Location start, TrackerSide side) {
        this.start = start;
        this.side = side;
        this.tracker = tracker;
        this.blocks = new ArrayList<>();

        BlockFace face = side.toBlockFace();

        if (start.getBlock().getType() != tracker.getMainBlockType()) throw new IllegalArgumentException("Starting location is not of type " + tracker.getMainBlockType());

        Block b = start.getBlock();

        while(b.getRelative(face).getType() == tracker.getArmsBlockType() || b.getRelative(face).getType() == tracker.getEndBlockType()) {
            b = b.getRelative(face);
            if (b.getType() == tracker.getEndBlockType()) {
                blocks.add(b);
                break;
            }
            blocks.add(b);
        }

    }

    public boolean isValid() {
        return blocks.size() > 0 && blocks.get(blocks.size() - 1).getType() == tracker.getEndBlockType();
    }

    public List<Block> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public TrackResult track() {
        return new TrackResult(this, blocks);
    }

    public Tracker getTracker() {
        return tracker;
    }

    public TrackerSide getSide() {
        return side;
    }

}
