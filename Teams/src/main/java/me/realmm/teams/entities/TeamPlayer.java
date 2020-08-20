package me.realmm.teams.entities;

import me.realmm.serverscoreboard.ServerScoreboard;
import me.realmm.teams.util.Messages;
import me.realmm.warps.entities.TeleportAttackCooldownPlayer;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import net.jamesandrew.serverscoreboard.realmlib.scoreboard.RealmScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class TeamPlayer extends TeleportAttackCooldownPlayer {

    private final ServerScoreboard plugin;
    private Team t;

    public TeamPlayer(OfflinePlayer o, Team team) {
        super(o);
        this.plugin = (ServerScoreboard) Bukkit.getPluginManager().getPlugin("ServerScoreboard");
        this.t = team;
    }

    public ChatColor getAppropriateColor() {
        return isLeader() ? Messages.LEADER : isPromoted() ? Messages.PROMOTED : Messages.NORMAL;
    }

    public void removeTeam() {
        t = null;
    }

    public Team getTeam() {
        return t;
    }

    public void setLeader() {
        t.setLeader(this);
    }

    public boolean isLeader() {
        return t.getLeader().getOfflinePlayer().getUniqueId().equals(getOfflinePlayer().getUniqueId());
    }

    public void setPromoted(boolean state) {
        t.setPromoted(this, state);
    }

    public boolean isPromoted() {
        return t.isPromoted(this);
    }

    public void setToggled(boolean toggled) {
        t.setToggled(this, toggled);
    }

    public boolean isToggled() {
        return t.isToggled(this);
    }

    public boolean equals(TeamPlayer tp) {
        return getOfflinePlayer().getUniqueId().equals(tp.getOfflinePlayer().getUniqueId());
    }

    public void updateScoreboard() {
        if (plugin == null || !isOnline()) return;
        RealmScoreboard scoreboard = plugin.getScoreboard(getOfflinePlayer());
        if (t == null) {
            scoreboard.removeLine(2);
        } else scoreboard.setLine(2, new Placeholder(Messages.TEAM_SCOREBOARD).setPlaceholders("team").setToReplace(t.getName()).toString());
        scoreboard.update(getPlayer());
    }

}
