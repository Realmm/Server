package me.realmm.spawnprotection.utils;

import me.realmm.spawnprotection.SpawnProtection;
import net.jamesandrew.realmlib.lang.Lang;

public final class Messages {

    public static final String NO_PERMISSION = get("no-permission");
    public static final String SET_SPAWN_PROTECT = get("set-spawn-protect");
    public static final String SET_POINT_ONE = get("set-point-one");
    public static final String NOT_SPAWN_PROTECTED = get("not-spawn-protected");
    public static final String SCOREBOARD_PROTECTED = get("scoreboard-protected");
    public static final String SCOREBOARD_UNPROTECTED = get("scoreboard-unprotected");

    private Messages(){}

    private static String get(String path) {
        return Lang.color(SpawnProtection.get().getConfig().getString("messages." + path));
    }

}
