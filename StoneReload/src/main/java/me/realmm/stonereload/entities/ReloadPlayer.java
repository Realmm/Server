package me.realmm.stonereload.entities;

import me.realmm.serverscoreboard.ServerScoreboard;
import me.realmm.stonereload.util.Messages;
import me.realmm.stonereload.util.StoneReloadUtil;
import net.jamesandrew.commons.number.Number;
import net.jamesandrew.realmlib.location.BlockChanger;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import net.jamesandrew.serverscoreboard.realmlib.scoreboard.RealmScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ReloadPlayer {

    private final OfflinePlayer p;
    private Location loc1, loc2;
    private final ServerScoreboard plugin;

    public ReloadPlayer(OfflinePlayer p) {
        this.p = p;
        this.plugin = (ServerScoreboard) Bukkit.getPluginManager().getPlugin("ServerScoreboard");
    }

    public boolean isOnline() {
        return p.isOnline();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(p.getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer() {
        return p;
    }

    public boolean hasSetLocOne() {
        return loc1 != null;
    }

    public boolean hasSetLocTwo() {
        return loc2 != null;
    }

    public void setLocOne(Location loc) {
        this.loc1 = loc;
    }

    public void setLocTwo(Location loc) {
        this.loc2 = loc;
    }

    public Location getLocOne() {
        return loc1;
    }

    public Location getLocTwo() {
        return loc2;
    }

    public void updateScoreboard() {
        if (!isOnline() || plugin == null) return;
        RealmScoreboard scoreboard = plugin.getScoreboard(p);
        BlockChanger changer = StoneReloadUtil.getChanger();
        if (StoneReloadUtil.isReloading() && changer != null && changer.isStarted()) {
            double d = Number.round(changer.getPercentageLeft(), 2);
            scoreboard.setBlankLine(8);
            scoreboard.setLine(13, new Placeholder(Messages.SCOREBOARD).setPlaceholders("percent").setToReplace(d).toString());
        } else scoreboard.removeLines(8, 13);
        scoreboard.update(getPlayer());
    }

}
