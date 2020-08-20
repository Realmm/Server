package me.realmm.stonereload.util;

import me.realmm.stonereload.StoneReload;

public final class ConfigUtil {

    private ConfigUtil(){}

    public static final int RELOAD = getInt("reload");
    public static final int BLOCK_UPDATE_INTERVAL = getInt("block-update-interval");
    public static final int BLOCK_CHANGE_AMOUNT = getInt("block-change-amount");
    public static final int STONE_Y = getInt("stone-y");
    public static final String WORLD_NAME = get("world-name");

    private static String get(String path) {
        return StoneReload.get().getConfig().getString(path);
    }

    private static int getInt(String path) {
        return StoneReload.get().getConfig().getInt(path);
    }

}
