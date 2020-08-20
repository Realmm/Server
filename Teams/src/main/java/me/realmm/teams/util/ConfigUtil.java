package me.realmm.teams.util;

import me.realmm.teams.Teams;

public final class ConfigUtil {

    private ConfigUtil(){}

    public static final String MONGO_HOST = get("mongodb.host");
    public static final String MONGO_DATABASE = get("mongodb.database");
    public static final int MONGO_PORT = getInt("mongodb.port");

    private static String get(String path) {
        return Teams.get().getConfig().getString(path);
    }

    private static int getInt(String path) {
        return Teams.get().getConfig().getInt(path);
    }

}
