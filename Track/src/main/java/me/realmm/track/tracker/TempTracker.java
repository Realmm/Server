package me.realmm.track.tracker;

import me.realmm.track.Track;
import me.realmm.track.runnables.RemoveDropsRunnable;
import me.realmm.track.utils.TrackerUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class TempTracker extends Tracker {

    public TempTracker(Location loc) {
        super(loc, TrackerUtil.TEMP_CENTRE, TrackerUtil.TEMP_ARM_BLOCK, TrackerUtil.TEMP_END_BLOCK);
    }

    @Override
    public int getDistanceMultiplier() {
        return TrackerUtil.TEMP_DISTANCE_PER_BLOCK;
    }

    @Override
    void onTrack() {
        Block main = getLocation().getBlock();
        TrackerUtil.addDrop(main);
        main.breakNaturally();

        getArms().forEach(arm -> arm.getBlocks().forEach(b -> {
            TrackerUtil.addDrop(b);
            b.breakNaturally();
        }));

        new RemoveDropsRunnable().runTaskLater(Track.get(), 5);
    }
}
