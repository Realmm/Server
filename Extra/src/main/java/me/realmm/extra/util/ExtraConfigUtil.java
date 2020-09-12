package me.realmm.extra.util;

import me.realmm.extra.Extra;
import net.jamesandrew.realmlib.lang.Lang;

public final class ExtraConfigUtil {

    private ExtraConfigUtil(){}

    public static final int MAP_SIZE = getInt("map-size");
    public static final String END_OF_WORLD = get("end-of-world");

    public static final int CHUNK_LOAD_AMOUNT = getInt("chunk-load-amount");
    public static final int CHUNK_UPDATE_INTERVAL = getInt("chunk-update-interval");
    public static final String WORLD_NAME = get("world-name");

    private static String get(String path) {
        return Lang.color(Extra.get().getConfig().getString(path));
    }

    private static int getInt(String path) {
        return Extra.get().getConfig().getInt(path);
    }

}
