package me.realmm.warps.util;

import me.realmm.warps.Warps;
import net.jamesandrew.realmlib.lang.Lang;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Messages {

    private Messages(){}

    public static final String SCOREBOARD_COOLDOWN = get("scoreboard-cooldown");
    public static final String SCOREBOARD_TELEPORTING = get("scoreboard-teleporting");

    public static final String NO_WARP_FOUND = get("no-warp-found");
    public static final String NEARBY = get("nearby");
    public static final String ATTACK_COOLDOWN = get("attack-cooldown");
    public static final String WARP_CANCELLED = get("warp-cancelled");
    public static final String WARP_SPAWN = get("warp-spawn");
    public static final String SET_WARP_SPAWN = get("set-warp-spawn");
    public static final String SET_WARP = get("set-warp");
    public static final String DELETE_WARP = get("delete-warp");
    public static final String WARP_NOT_FOUND = get("warp-not-found");
    public static final String OVERWRITE_OUT_OF_TIME = get("overwrite-out-of-time");
    public static final String OVERWRITTEN_WARP = get("overwritten-warp");
    public static final String CANCELLED_OVERWRITE = get("cancelled-overwrite");
    public static final String MAXIMUM_WARPS = get("maximum-warps");
    public static final String HOME_SET = get("home-set");
    public static final String HOME_NOT_SET = get("home-not-set");

    public static final String OVERWRITE_WARP = get("overwrite-warp");
    public static final String YES = get("yes-text");
    public static final String YES_HOVER = get("yes-hover");
    public static final String NO = get("no-text");
    public static final String NO_HOVER = get("no-hover");

    public static final String WARP_LIST_TITLE = get("warp-list-title");
    public static final String WARP_LIST_SEPARATOR = get("warp-list-separator");
    public static final String WARP_LIST_HOVER = get("warp-list-hover");
    public static final ChatColor WARP_LIST_BRACKET_COLOR = getColor("warp-list-bracket-color");
    public static final ChatColor WARP_LIST_WARP_COLOR = getColor("warp-list-warp-color");

    public static final String WARP_HELP = get("warp-help");
    public static final List<String> HELP_LIST = getList("help-list");

    private static String get(String s) {
        String st = JavaPlugin.getPlugin(Warps.class).getMessages().getConfig().getString(s);
        return Lang.color(st);
    }

    private static List<String> getList(String s) {
        return JavaPlugin.getPlugin(Warps.class).getMessages().getConfig().getStringList(s).stream().map(Lang::color).collect(Collectors.toList());
    }

    private static ChatColor getColor(String s) {
        String color = JavaPlugin.getPlugin(Warps.class).getMessages().getConfig().getString(s);
        return Arrays.stream(ChatColor.values()).filter(c -> color.equalsIgnoreCase(c.name())).findFirst().orElseThrow(() -> new IllegalArgumentException("Illegal ChatColor name in messages.yml"));
    }

}
