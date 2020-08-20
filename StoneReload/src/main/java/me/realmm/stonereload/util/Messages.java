package me.realmm.stonereload.util;

import me.realmm.stonereload.StoneReload;
import net.jamesandrew.realmlib.lang.Lang;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Messages {

    private Messages(){}

    public static final String SCOREBOARD = get("scoreboard");
    public static final String INCORRECT_PERMISSIONS = get("incorrect-permissions");
    public static final String FULL_INVENTORY = get("full-inventory");
    public static final String GIVEN_ITEM = get("given-item");
    public static final String SELECTED = get("selected");
    public static final String SELECTED_TWO = get("selected-two");
    public static final String ADDED = get("added");
    public static final String SELECT_AREA = get("select-area");
    public static final String LIST_TITLE = get("list-title");
    public static final String LIST_LINE = get("list-line");
    public static final String NONE_SAVED = get("none-saved");
    public static final String INVALID_ID = get("invalid-id");
    public static final String DELETED = get("deleted");

    public static final List<String> HELP = getList("help");

    private static List<String> getList(String path) {
        List<String> list = JavaPlugin.getPlugin(StoneReload.class).getMessages().getConfig().getStringList(path);
        list.replaceAll(Lang::color);
        return list;
    }

    private static String get(String path) {
        return Lang.color(JavaPlugin.getPlugin(StoneReload.class).getMessages().getConfig().getString(path));
    }

}
