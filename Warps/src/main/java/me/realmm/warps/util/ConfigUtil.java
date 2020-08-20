package me.realmm.warps.util;

import me.realmm.warps.Warps;

public final class ConfigUtil {

    private ConfigUtil(){}

    public static final String MONGO_HOST = get("mongodb.host");
    public static final String MONGO_DATABASE = get("mongodb.database");
    public static final int MONGO_PORT = getInt("mongodb.port");

    public static final int OVERWRITING_DURATION = getInt("overwriting-duration");
    public static final int ATTACK_COOLDOWN = getInt("attack-cooldown");
    public static final int WARP_DISTANCE = getInt("warp-distance");
    public static final int WARP_COOLDOWN = getInt("warp-cooldown");

    public static int getWarpAmount(String path) {
        return Warps.get().getConfig().getInt("warps." + path);
    }

    public static boolean isWarpRank(String path) {
        return Warps.get().getConfig().get("warps." + path) != null;
    }

    private static String get(String path) {
        return Warps.get().getConfig().getString(path);
    }

    private static int getInt(String path) {
        return Warps.get().getConfig().getInt(path);
    }

}
