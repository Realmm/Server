package me.realmm.track.utils;

import me.realmm.track.Track;
import me.realmm.track.tracker.MainTracker;
import me.realmm.track.tracker.TempTracker;
import me.realmm.track.tracker.TrackMessage;
import me.realmm.track.tracker.Tracker;
import net.jamesandrew.commons.number.Number;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public final class TrackerUtil {

    private TrackerUtil(){}

    private static final Set<Location> drops = new HashSet<>();

    public static final Material MAIN_CENTRE = getMaterial("main.centre");
    public static final Material MAIN_ARM_BLOCK = getMaterial("main.arm-block");
    public static final Material MAIN_END_BLOCK = getMaterial("main.end-block");
    public static final Material TEMP_CENTRE = getMaterial("temp.centre");
    public static final Material TEMP_ARM_BLOCK = getMaterial("temp.arm-block");
    public static final Material TEMP_END_BLOCK = getMaterial("temp.end-block");

    public static final int MAIN_DISTANCE_PER_BLOCK = getInt("main.distance-per-block");
    public static final int TEMP_DISTANCE_PER_BLOCK = getInt("temp.distance-per-block");

    public static final ChatColor COLOR_TRACKED_ALL_PLAYER = getChatColor("color-tracked-all-player");
    public static final ChatColor COLOR_NON_TRACKED_ALL_PLAYER = getChatColor("color-non-tracked-all-player");

    private static Material getMaterial(String s) {
        Object o = get(s);
        if (!(o instanceof String)) throw new IllegalArgumentException("Materials not listed as strings");
        String st = (String) o;
        if (Material.matchMaterial(st) == null) throw new IllegalArgumentException("No material under this name");
        return Material.matchMaterial(st);
    }

    private static int getInt(String s) {
        Object o = get(s);
        if (!Number.isInt(String.valueOf(o))) throw new IllegalArgumentException(s + " should be an integer");
        return Number.getInt(String.valueOf(o));
    }

    private static ChatColor getChatColor(String s) {
        String st = Track.get().getConfig().getString(s);
        try {
            return ChatColor.valueOf(st);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return ChatColor.WHITE;
    }

    private static Object get(String path) {
        return Track.get().getConfig().get("tracker." + path);
    }

    public static void trackAll(Player p) {
        Tracker tracker = checkValid(p);
        if (tracker == null) return;
        if (tracker instanceof TempTracker) {
            p.sendMessage(Messages.NOT_ON_TRACKER);
            return;
        }

        TrackMessage msg = tracker.trackAll(p);
        if (!msg.isCancelled()) p.spigot().sendMessage(msg.getMessage());
    }

    public static void trackPlayer(Player tracking, Player toTrack) {
        Tracker tracker = checkValid(tracking);
        if (tracker == null) return;

        TrackMessage msg = tracker.trackPlayer(toTrack);
        if (!msg.isCancelled()) tracking.spigot().sendMessage(msg.getMessage());
    }

    public static boolean isDrop(Item item) {
        return drops.stream()
                .anyMatch(d ->
                    d.getBlockX() == item.getLocation().getBlockX() &&
                            d.getBlockY() == item.getLocation().getBlockY() &&
                            d.getBlockZ() == item.getLocation().getBlockZ() &&
                            d.getWorld().getName().equalsIgnoreCase(item.getWorld().getName())
                );
    }

    public static void addDrop(Block b) {
        drops.add(b.getLocation());
    }

    public static void removeDrop(Location loc) {
        drops.remove(loc);
    }

    public static void clearDrops() {
        drops.clear();
    }

    private static Tracker checkValid(Player p) {
        Block below = p.getLocation().getBlock().getRelative(BlockFace.DOWN);

        if (below.getType() != TrackerUtil.MAIN_CENTRE && below.getType() != TrackerUtil.TEMP_CENTRE) {
            p.sendMessage(Messages.NOT_ON_TRACKER);
            return null;
        }

        Tracker tracker;

        if (below.getType() == TrackerUtil.MAIN_CENTRE) {
            tracker = new MainTracker(below.getLocation());
        } else tracker = new TempTracker(below.getLocation());

        if (!tracker.isValid()) {
            p.sendMessage(Messages.NOT_ON_TRACKER);
            return null;
        }
        return tracker;
    }

}
