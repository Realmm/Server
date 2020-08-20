package me.realmm.mobcapture.utils;

import me.realmm.mobcapture.MobCapture;
import net.jamesandrew.realmlib.lang.Lang;
import org.bukkit.plugin.java.JavaPlugin;

public final class Messages {

    public static String SUCCESS = get("success");
    public static String FAILURE = get("failure");
    public static String LOW_LEVEL = get("low-level");
    public static String NOT_FAR_ENOUGH = get("not-far-enough");

    private Messages(){}

    private static String get(String path) {
        return Lang.color(JavaPlugin.getPlugin(MobCapture.class).getMessages().getConfig().getString(path));
    }

}
