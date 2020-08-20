package me.realmm.teams.util;

import me.realmm.teams.database.TeamMongo;
import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.warps.entities.TeleportAttackCooldownPlayer;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.commons.database.mongo.callback.IDatabaseResultCallback;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import net.jamesandrew.realmlib.placeholder.ReplacePattern;
import org.bukkit.OfflinePlayer;

import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public final class TeamUtil {

    private static TeamMongo teamMongo;
    private static Set<Team> teams = new HashSet<>();

    private TeamUtil(){}

    public static void initMongoDB(IDatabaseResultCallback<Void> callback) {
        teamMongo = new TeamMongo(callback);
    }

    public static TeamMongo getMongoDB() {
        return teamMongo;
    }

    public static boolean isInTeam(OfflinePlayer p) {
        return teams.stream().anyMatch(t -> t.getMembers().stream().anyMatch(m -> m.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())));
    }

    public static Team getTeam(String name) {
        return teams.stream().filter(t -> t.getName().equalsIgnoreCase(name)).findFirst().orElseThrow(() -> new IllegalArgumentException("No team under the name " + name));
    }

    public static Team getTeam(OfflinePlayer p) {
        return teams.stream().filter(t -> t.getMembers().stream().anyMatch(m -> m.getOfflinePlayer().getUniqueId().equals(p.getUniqueId()))).findFirst().orElseThrow(() -> new IllegalArgumentException("Player not in team"));
    }

    public static TeamPlayer getTeamPlayer(OfflinePlayer p) {
        Team team = getTeam(p);
        return team.getMembers().stream().filter(tp -> tp.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())).findFirst().orElseThrow(() -> new IllegalArgumentException("Player not in team"));
    }

    public static boolean isTeam(String s) {
        return teams.stream().map(Team::getName).anyMatch(n -> n.equalsIgnoreCase(s));
    }

    public static boolean isRegisteredTeam(Team t) {
        return teams.contains(t);
    }

    public static void unregisterTeam(Team team) {
        teams.remove(team);
    }

    public static void registerTeam(Team team) {
        if (isTeam(team.getName())) throw new IllegalArgumentException("A team with the same name has already been registered");
        teams.add(team);
        Stream<TeleportAttackCooldownPlayer> stream = team.getMembers().stream().map(m -> WarpUtil.getAttackCooldownPlayers(m.getOfflinePlayer())).flatMap(Collection::stream);

        if (stream.noneMatch(t -> t instanceof TeamPlayer)) {
            stream.forEach(WarpUtil::registerAttackCooldownPlayer);
        }

        team.getMembers().forEach(TeamPlayer::updateScoreboard);

        teamMongo.createTeam(team, new IDatabaseResultCallback<Void>() {
            @Override
            protected void onReceived(Void value) {
                teamMongo.updateTeam(team);
            }
        });
    }

    public static void registerTeams() {
        if (!teams.isEmpty()) throw new IllegalArgumentException("Attempted to register all Teams on non-empty set");
        teamMongo.getTeams().join().forEach(TeamUtil::registerTeam);
    }

    public static Collection<Team> getTeams() {
        return Collections.unmodifiableCollection(teams);
    }

    public static String getInfo(Team t, boolean verbose) {
        StringBuilder sb = new StringBuilder();
        StringBuilder memberSb = new StringBuilder();

        t.getOnline().forEach(o -> {
            memberSb.append(new Placeholder(verbose ? Messages.INFO_MEMBER_ONLINE : Messages.INFO_MEMBER_SUB_ONLINE).setPlaceholders("player", "health").setToReplace(o.getAppropriateColor() + o.getPlayer().getName(), (o.getPlayer().getHealth() / o.getPlayer().getMaxHealth()) * 100));
            memberSb.append("\n");
        });

        t.getMembers().stream().filter(o -> !o.isOnline()).forEach(o -> {
            memberSb.append(new Placeholder(verbose ? Messages.INFO_MEMBER_OFFLINE : Messages.INFO_MEMBER_SUB_OFFLINE).setPlaceholders("player").setToReplace(o.getAppropriateColor() + o.getOfflinePlayer().getName()));
            memberSb.append("\n");
        });

        ReplacePattern pattern = verbose ?
                new ReplacePattern()
                        .setPlaceholders("team", "points", "balance", "password", "hqset", "rallyset", "ff", "online", "total", "members")
                        .setToReplace(t.getName(), t.getPoints(), t.getTotalBalance().setScale(2, RoundingMode.HALF_UP).doubleValue(),
                                t.hasPassword() ? t.getPassword() : Messages.INFO_UNSET,
                                t.hasHq() ? Messages.INFO_SET : Messages.INFO_UNSET,
                                t.hasRally() ? Messages.INFO_SET : Messages.INFO_UNSET,
                                t.getFriendlyFire() ? Messages.INFO_FF_ON : Messages.INFO_FF_OFF,
                                t.getOnline().size(),
                                t.getMembers().size(),
                                memberSb.toString().trim()) :
                new ReplacePattern()
                        .setPlaceholders("team", "points", "balance", "online", "total", "members")
                        .setToReplace(t.getName(), t.getPoints(), t.getTotalBalance().setScale(2, RoundingMode.HALF_UP).doubleValue(),
                                t.getOnline().size(),
                                t.getMembers().size(),
                                memberSb.toString().trim());

        if (verbose) {
            Messages.INFO.forEach(i -> {
                sb.append(new Placeholder(i, pattern).toString());
                sb.append("\n");
            });
        } else {
            Messages.INFO_SUB.forEach(i -> {
                sb.append(new Placeholder(i, pattern).toString());
                sb.append("\n");
            });
        }

        return sb.toString().trim();
    }


}
