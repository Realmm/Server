package me.realmm.track.tracker;

import me.realmm.track.utils.TrackerUtil;
import org.bukkit.Location;

public class MainTracker extends Tracker {

    public MainTracker(Location loc) {
        super(loc, TrackerUtil.MAIN_CENTRE, TrackerUtil.MAIN_ARM_BLOCK, TrackerUtil.MAIN_END_BLOCK);
    }

    @Override
    public int getDistanceMultiplier() {
        return TrackerUtil.MAIN_DISTANCE_PER_BLOCK;
    }

    @Override
    void onTrack() {

    }
}
