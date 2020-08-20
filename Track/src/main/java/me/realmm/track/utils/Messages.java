package me.realmm.track.utils;

import me.realmm.track.Track;
import net.jamesandrew.realmlib.lang.Lang;
import org.bukkit.plugin.java.JavaPlugin;

public final class Messages {

    private Messages(){}

    public static final String NO_PERMISSION = get("no-permission");
    public static final String NOT_ON_TRACKER = get("not-on-tracker");
    public static final String TRACK_ALL_RESULT = get("track-all-result");
    public static final String TRACK_RESULT_FOUND = get("track-result-found");
    public static final String TRACK_RESULT_NOT_FOUND = get("track-result-not-found");
    public static final String PLAYER_NOT_PLAYED = get("player-not-played");
    public static final String PLAYER_NOT_ONLINE = get("player-not-online");
    public static final String CLICK_TO_TRACK = get("click-to-track");

    private static String get(String s) {
        Track t = JavaPlugin.getPlugin(Track.class);
        return Lang.color(t.getMessages().getConfig().getString(s));
    }

}
