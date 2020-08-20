package me.realmm.bottledxp.utils;

import me.realmm.bottledxp.BottledXP;
import net.jamesandrew.realmlib.lang.Lang;
import org.bukkit.ChatColor;

public class Messages {

    public static final String TITLE = get("title");
    public static final String LORE = get("lore");
    public static final ChatColor XP_COLOR = ChatColor.valueOf(get("experience-color").toUpperCase());

    private static String get(String path) {
        return Lang.color(BottledXP.get().getConfig().getString(path));
    }

}
