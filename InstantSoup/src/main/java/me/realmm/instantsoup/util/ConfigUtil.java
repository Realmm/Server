package me.realmm.instantsoup.util;

import me.realmm.instantsoup.InstantSoup;

public final class ConfigUtil {

    public static final int HEAL_AMOUNT = get("heal-amount");
    public static final int FEED_AMOUNT = get("feed-amount");

    private ConfigUtil(){}

    private static int get(String path) {
        return InstantSoup.get().getConfig().getInt(path);
    }

}
