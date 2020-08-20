package me.realmm.mobcapture.utils;

import me.realmm.mobcapture.MobCapture;

public final class ConfigUtil {

    public static final int DISTANCE_TO_CAPTURE = get("distance-to-capture");
    public static final int GUARANTEED_CAPTURE_LEVEL = get("guaranteed-capture-level");
    public static final int LOWEST_CAPTURE_LEVEL = get("lowest-capture-level");
    public static final int PERCENT_SUCCESS = get("percent-success");

    private ConfigUtil(){}

    private static int get(String path) {
        return MobCapture.get().getConfig().getInt(path);
    }

}
