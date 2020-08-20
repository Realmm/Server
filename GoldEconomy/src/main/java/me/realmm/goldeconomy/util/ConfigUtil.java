package me.realmm.goldeconomy.util;

import me.realmm.goldeconomy.GoldEconomy;

public final class ConfigUtil {

    private ConfigUtil(){}

    public static final String MONGO_HOST = get("mongodb.host");
    public static final String MONGO_DATABASE = get("mongodb.database");
    public static final int MONGO_PORT = getInt("mongodb.port");

    private static String get(String path) {
        return GoldEconomy.get().getConfig().getString(path);
    }

    private static int getInt(String path) {
        return GoldEconomy.get().getConfig().getInt(path);
    }

}
