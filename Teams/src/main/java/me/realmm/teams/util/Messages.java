package me.realmm.teams.util;

import me.realmm.teams.Teams;
import net.jamesandrew.realmlib.lang.Lang;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Messages {

    public static final String TEAM_SCOREBOARD = get("team-scoreboard");

    public static final String IN_TEAM = get("in-team");
    public static final String NOT_IN_TEAM = get("not-in-team");
    public static final String NOT_TEAM = get("not-team");
    public static final String INCORRECT_PASSWORD = get("incorrect-password");
    public static final String TEAM_HAS_PASSWORD = get("team-has-password");
    public static final String JOINED_TEAM = get("joined-team");
    public static final String PLAYER_JOINED_TEAM = get("player-joined-team");
    public static final String CANNOT_LEAVE_LEADER = get("cannot-leave-leader");
    public static final String PLAYER_LEFT_TEAM = get("player-left-team");
    public static final String LEFT_TEAM = get("left-team");
    public static final String PASSWORD_CHANGED = get("password-changed");
    public static final String CHANGED_PASSWORD = get("changed-password");
    public static final String REMOVED_PASSWORD = get("removed-password");
    public static final String PASSWORD_REMOVED = get("password-removed");
    public static final String ALREADY_PROMOTED = get("already-promoted");
    public static final String PLAYER_PROMOTED = get("player-promoted");
    public static final String PROMOTED_PLAYER = get("promoted-player");
    public static final String NOT_PROMOTED = get("not-promoted");
    public static final String NOT_LEADER = get("not-leader");
    public static final String ALREADY_LEADER = get("already-leader");
    public static final String GIVEN_LEADER = get("given-leader");
    public static final String LEADER_GIVEN = get("leader-given");
    public static final String CANNOT_KICK_SELF = get("cannot-kick-self");
    public static final String CANNOT_KICK_LEADER = get("cannot-kick-leader");
    public static final String YOU_KICKED_PLAYER = get("you-kicked-player");
    public static final String PLAYER_KICKED = get("player-kicked");
    public static final String DEMOTED_PLAYER_SENDER = get("demoted-player-sender");
    public static final String DEMOTED_PLAYER_RECEIVER = get("demoted-player-receiver");
    public static final String DEMOTED_PLAYER_TEAM = get("demoted-player-team");
    public static final String CANNOT_DEMOTE_LEADER = get("cannot-demote-leader");
    public static final String PLAYER_NOT_DEMOTED = get("player-not-demoted");
    public static final String PLAYER_NOT_PLAYED = get("player-not-played");
    public static final String PLAYER_NOT_ON_TEAM = get("player-not-on-team");
    public static final String PLAYER_NOT_ON_YOUR_TEAM = get("player-not-on-your-team");
    public static final String DISBANDED = get("disbanded");
    public static final String TEAM_NAME_USED = get("team-name-used");
    public static final String CREATED_NO_PASS = get("created-no-pass");
    public static final String CREATED_PASS = get("created-pass");
    public static final String FF_ON = get("ff-on");
    public static final String FF_OFF = get("ff-off");
    public static final String CHAT_ON = get("chat-on");
    public static final String CHAT_OFF = get("chat-off");
    public static final String CHAT = get("chat");
    public static final String TEAM_SET_HQ = get("team-set-hq");
    public static final String SET_HQ = get("set-hq");
    public static final String TEAM_SET_RALLY = get("team-set-rally");
    public static final String SET_RALLY = get("set-rally");
    public static final String HQ_NOT_SET = get("hq-not-set");
    public static final String RALLY_NOT_SET = get("rally-not-set");
    public static final String LOG_OFF = get("log-off");
    public static final String LOG_IN = get("log-in");
    public static final String DIED = get("died");

    public static final ChatColor LEADER = getColor("leader");
    public static final ChatColor PROMOTED = getColor("promoted");
    public static final ChatColor NORMAL = getColor("normal");

    public static final String INFO_UNSET = get("info-unset");
    public static final String INFO_SET = get("info-set");
    public static final String INFO_FF_ON = get("info-ff-on");
    public static final String INFO_FF_OFF = get("info-ff-off");
    public static final String INFO_MEMBER_ONLINE = get("info-member-online");
    public static final String INFO_MEMBER_OFFLINE = get("info-member-offline");
    public static final String INFO_MEMBER_SUB_ONLINE = get("info-member-sub-online");
    public static final String INFO_MEMBER_SUB_OFFLINE = get("info-member-sub-offline");
    public static final List<String> INFO = getList("info");
    public static final List<String> INFO_SUB = getList("info-sub");

    public static final String BALTOP_LINE = get("baltop-line");
    public static final List<String> BALTOP = getList("baltop");

    public static final String LIST_LINE = get("list-line");
    public static final List<String> LIST = getList("list");

    public static final List<String> HELP = getList("help");

    private static ChatColor getColor(String path) {
        String s = JavaPlugin.getPlugin(Teams.class).getMessages().getConfig().getString(path);
        if (s.split("&").length != 2 || s.split("&")[1].length() != 1) throw new IllegalArgumentException("Fix color formatting in messages.yml");
        return ChatColor.getByChar(s.split("&")[1]);
    }

    private static List<String> getList(String path) {
        List<String> list = JavaPlugin.getPlugin(Teams.class).getMessages().getConfig().getStringList(path);
        list.replaceAll(Lang::color);
        return list;
    }

    private static String get(String path) {
        return Lang.color(JavaPlugin.getPlugin(Teams.class).getMessages().getConfig().getString(path));
    }

}
