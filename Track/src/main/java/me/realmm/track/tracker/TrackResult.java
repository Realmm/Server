package me.realmm.track.tracker;

import net.jamesandrew.commons.number.Number;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrackResult {

    private final TrackerArm arm;
    private final List<Block> blocks;

    public TrackResult(TrackerArm arm, List<Block> blocks) {
        this.arm = arm;
        this.blocks = blocks;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public boolean isValid() {
        return getEndBlock() != null;
    }

    public Block getEndBlock() {
        return blocks.stream().filter(b -> b.getType() == arm.getTracker().getEndBlockType()).findFirst().orElse(null);
    }

    public int getDistance() {
        return getBlocks().size() * arm.getTracker().getDistanceMultiplier();
    }

    public Set<Player> getTrackedPlayers(Player... toExclude) {
        Set<Player> players = new HashSet<>();
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (Arrays.stream(toExclude).anyMatch(e -> e == p)) return;
            Location l = arm.getTracker().getLocation();
            switch (arm.getSide()) {
                case EAST:
                    if (Number.isBetween(l.clone().add(getDistance(), 0, 0).getBlockX(), l.getBlockX(), p.getLocation().getBlockX(), true)) players.add(p);
                    break;
                case WEST:
                    if (Number.isBetween(l.clone().add(-getDistance(), 0, 0).getBlockX(), l.getBlockX(), p.getLocation().getBlockX(), true)) players.add(p);
                    break;
                case NORTH:
                    if (Number.isBetween(l.clone().add(0, 0, -getDistance()).getBlockZ(), l.getBlockZ(), p.getLocation().getBlockZ(), true)) players.add(p);
                    break;
                case SOUTH:
                    if (Number.isBetween(l.clone().add(0, 0, getDistance()).getBlockZ(), l.getBlockZ(), p.getLocation().getBlockZ(), true)) players.add(p);
                    break;
            }
        });
        return players;
    }

    public boolean hasTrackedPlayers(Player p) {
        return getTrackedPlayers(p).size() > 0;
    }

}
