package me.realmm.teams.entities;

import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.teams.database.TeamMongo;
import me.realmm.teams.util.TeamUtil;
import me.realmm.warps.util.WarpUtil;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Team {

    private final String name;
    private String password;
    private TeamPlayer leader;
    private final Set<TeamPlayer> members = new HashSet<>(), promoted = new HashSet<>(), toggled = new HashSet<>();
    private final TeamMongo mongo = TeamUtil.getMongoDB();
    private int points;
    private boolean ff;
    private Location hq, rally;

    public Team(String name, OfflinePlayer leader) {
        this.name = name;

        this.leader = new TeamPlayer(leader, this);
        WarpUtil.registerAttackCooldownPlayer(this.leader);
        this.members.add(this.leader);

        this.promoted.add(this.leader);
    }

    public void disband() {
        TeamUtil.unregisterTeam(this);
        mongo.deleteTeam(this, null);
        members.forEach(m -> {
            m.removeTeam();
            m.updateScoreboard();
            WarpUtil.removeAttackCooldownPlayer(m.getOfflinePlayer(), m.getClass());
        });
    }

    public Set<TeamPlayer> getOnline() {
        return members.stream().filter(TeamPlayer::isOnline).collect(Collectors.toSet());
    }

    public BigDecimal getTotalBalance() {
        return members.stream().map(TeamPlayer::getOfflinePlayer).map(EconUtil::getEconPlayer).map(EconPlayer::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void sendMessage(String message, TeamPlayer... excluding) {
        members.stream().filter(TeamPlayer::isOnline).filter(t -> Arrays.stream(excluding).noneMatch(t::equals)).forEach(t -> t.getPlayer().sendMessage(message));
    }

    public void setFriendlyFire(boolean state) {
        if (this.ff == state) return;
        this.ff = state;
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public boolean getFriendlyFire() {
        return ff;
    }

    public void setToggled(TeamPlayer tp, boolean state) {
        if (!isMember(tp)) throw new IllegalArgumentException("Unable to toggle player not in team");
        if (state) {
            toggled.add(tp);
        } else toggled.remove(tp);
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public boolean isToggled(TeamPlayer tp) {
        return toggled.contains(tp);
    }

    public Collection<TeamPlayer> getToggled() {
        return Collections.unmodifiableCollection(toggled);
    }

    public void setPromoted(TeamPlayer tp, boolean state) {
        if (!isMember(tp)) throw new IllegalArgumentException("Unable to promote player not in team");
        if (state) {
            promoted.add(tp);
        } else promoted.remove(tp);
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public boolean isPromoted(TeamPlayer tp) {
        return promoted.contains(tp) || leader == tp;
    }

    public Collection<TeamPlayer> getPromoted() {
        return Collections.unmodifiableCollection(promoted);
    }

    public boolean hasPassword() {
        return password != null && !password.equals("");
    }

    public void setPassword(String password) {
        this.password = password;
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int i) {
        this.points = i;
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public TeamPlayer getLeader() {
        return leader;
    }

    public void setLeader(TeamPlayer t) {
        if (!members.contains(t)) throw new IllegalArgumentException("Player not in team, unable to set as leader");
        if (this.leader == t) return;
        this.leader.setPromoted(true);
        this.leader = t;
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public TeamPlayer getMember(OfflinePlayer o) {
        return members.stream().filter(m -> m.getOfflinePlayer().getUniqueId().equals(o.getUniqueId())).findFirst().orElseThrow(() -> new IllegalArgumentException("Player not in team"));
    }

    public void addMember(OfflinePlayer o) {
        if (members.stream().anyMatch(m -> m.getOfflinePlayer().getUniqueId().equals(o.getUniqueId()))) throw new IllegalArgumentException("Player already in team");
        TeamPlayer tp = new TeamPlayer(o, this);
        if (TeamUtil.isRegisteredTeam(this)) WarpUtil.registerAttackCooldownPlayer(tp);
        members.add(tp);
        if (TeamUtil.isRegisteredTeam(this)) tp.updateScoreboard();
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public void removeMember(TeamPlayer t) {
        if (t == leader) throw new IllegalArgumentException("Cannot remove leader");
        members.remove(t);
        promoted.remove(t);
        toggled.remove(t);
        WarpUtil.removeAttackCooldownPlayer(t.getOfflinePlayer(), t.getClass());
        t.removeTeam();
        if (TeamUtil.isRegisteredTeam(this)) t.updateScoreboard();
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public boolean isMember(TeamPlayer t) {
        return members.contains(t);
    }

    public Collection<TeamPlayer> getMembers() {
        return Collections.unmodifiableCollection(members);
    }

    public void setHq(Location location) {
        this.hq = location;
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public void setRally(Location location) {
        this.rally = location;
        if (TeamUtil.isRegisteredTeam(this)) mongo.updateTeam(this);
    }

    public Location getHq() {
        return hq;
    }

    public Location getRally() {
        return rally;
    }

    public boolean hasHq() {
        return hq != null;
    }

    public boolean hasRally() {
        return rally != null;
    }



}
