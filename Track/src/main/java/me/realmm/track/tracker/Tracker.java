package me.realmm.track.tracker;

import me.realmm.track.events.TrackEvent;
import me.realmm.track.utils.Messages;
import me.realmm.track.utils.TrackerUtil;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import net.jamesandrew.realmlib.register.Register;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Tracker {

    private final Material main, arms, end;
    private final Collection<TrackerArm> trackerArms;
    private final Location location;

    public Tracker(Location location, Material main, Material arms, Material end) {
        this.main = main;
        this.arms = arms;
        this.end = end;
        this.location = location;

        TrackerArm[] armsArray = new TrackerArm[]{
                new TrackerArm(this, location, TrackerSide.NORTH),
                new TrackerArm(this, location, TrackerSide.EAST),
                new TrackerArm(this, location, TrackerSide.SOUTH),
                new TrackerArm(this, location, TrackerSide.WEST)
        };

        this.trackerArms = Arrays.stream(armsArray).filter(TrackerArm::isValid).collect(Collectors.toList());

        if (location.getBlock().getType() != main) throw new IllegalArgumentException("Illegal location for tracker, must be centre block");

        if (trackerArms.size() > 4) throw new IllegalArgumentException("More than 4 arms, there are " + trackerArms.size() + " arms on this tracker");

        if (main == arms || main == end || arms == end) throw new IllegalArgumentException("Materials cannot be the same, must be different materials to design a tracker");
    }

    public Material getMainBlockType() {
        return main;
    }

    public Material getArmsBlockType() {
        return arms;
    }

    public Material getEndBlockType() {
        return end;
    }

    public Collection<TrackerArm> getArms() {
        return trackerArms;
    }

    public boolean isValid() {
        return trackerArms.size() > 0 && trackerArms.stream().allMatch(TrackerArm::isValid);
    }

    public abstract int getDistanceMultiplier();

    public TrackMessage trackAll(Player sender) {
        final ComponentBuilder c = new ComponentBuilder("");

        final int[] t = {0};
        trackerArms.forEach(arm -> {
            final int[] i = {0};
            TrackResult result = arm.track();
            c.event((ClickEvent) null);
            c.event((HoverEvent) null);

            c.append(new Placeholder(Messages.TRACK_ALL_RESULT)
                    .setPlaceholders("direction", "distance")
                    .setToReplace(arm.getSide().name().toUpperCase(), result.getDistance())
                    .toString()
            );

            final boolean[] added = {false};

            result.getTrackedPlayers(sender).forEach(p -> {
                added[0] = true;
                c.append(p.getName());
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/track " + p.getName()));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(new Placeholder(Messages.CLICK_TO_TRACK).setPlaceholders("player").setToReplace(p.getDisplayName()).toString())));
                if (trackerArms.stream().anyMatch(tArm -> tArm != arm && tArm.track().getTrackedPlayers(sender).contains(p))) c.color(ChatColor.getByChar(TrackerUtil.COLOR_TRACKED_ALL_PLAYER.getChar()));
                if (i[0] != result.getTrackedPlayers(sender).size() - 1) {
                    c.append(", ");
                    c.color(ChatColor.getByChar(TrackerUtil.COLOR_NON_TRACKED_ALL_PLAYER.getChar()));
                }
                i[0]++;

            });

            if (!added[0]) {
                c.append("None");
                c.color(ChatColor.getByChar(TrackerUtil.COLOR_NON_TRACKED_ALL_PLAYER.getChar()));
            }

            if (t[0] != trackerArms.size() - 1) {
                c.append("\n");
                c.color(ChatColor.getByChar(TrackerUtil.COLOR_NON_TRACKED_ALL_PLAYER.getChar()));
            }

            t[0]++;
        });

        onTrack();
        TrackEvent e = new TrackEvent(this);
        Register.callEvent(e);

        return new TrackMessage(this, c.create(), e.isCancelled());
    }

    public TrackMessage trackPlayer(Player p) {
        StringBuilder sb = new StringBuilder();

        final int[] i = {0};
        trackerArms.forEach(arm -> {
            TrackResult result = arm.track();
            Set<Player> players = result.getTrackedPlayers();
            if (players.contains(p)) {
                sb.append(new Placeholder(Messages.TRACK_RESULT_FOUND).setPlaceholders("player", "distance", "direction").setToReplace(p.getDisplayName(), result.getDistance(), arm.getSide().name().toLowerCase()));
            } else sb.append(new Placeholder(Messages.TRACK_RESULT_NOT_FOUND).setPlaceholders("player", "distance", "direction").setToReplace(p.getDisplayName(), result.getDistance(), arm.getSide().name().toLowerCase()));
            if (i[0] != trackerArms.size() - 1) sb.append("\n");
            i[0]++;
        });

        onTrack();
        TrackEvent e = new TrackEvent(this);
        Register.callEvent(e);

        return new TrackMessage(this, new ComponentBuilder(sb.toString()).create(), e.isCancelled());
    }

    abstract void onTrack();

    public Location getLocation() {
        return location;
    }

}
