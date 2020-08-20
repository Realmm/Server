package me.realmm.spawnprotection.entities;

import me.realmm.serverscoreboard.ServerScoreboard;
import me.realmm.spawnprotection.utils.Messages;
import me.realmm.warps.entities.TeleportAttackCooldownPlayer;
import net.jamesandrew.serverscoreboard.realmlib.scoreboard.RealmScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ProtectPlayer extends TeleportAttackCooldownPlayer {

    private final Protection protection;
    private boolean isProtected;
    private final ServerScoreboard plugin;

    public ProtectPlayer(OfflinePlayer p) {
        super(p);
        this.protection = new Protection();
        this.plugin = (ServerScoreboard) Bukkit.getPluginManager().getPlugin("ServerScoreboard");
    }

    public Protection getProtection() {
        return protection;
    }

    public boolean isSetting() {
        return protection.isSetting(getPlayer());
    }

    public void setProtected(boolean prot) {
        Player p = getPlayer();
        if (prot && !this.isProtected) {
            if (plugin == null) return;
            RealmScoreboard scoreboard = plugin.getScoreboard(p);
            scoreboard.setLine(4, Messages.SCOREBOARD_PROTECTED);
            scoreboard.update(p);
        } else if (!prot && this.isProtected) {
            p.sendMessage(Messages.NOT_SPAWN_PROTECTED);
            if (plugin == null) return;
            RealmScoreboard scoreboard = plugin.getScoreboard(p);
            scoreboard.removeLine(4);
            scoreboard.update(p);
        } else if (!prot) {
            if (plugin == null) return;
            RealmScoreboard scoreboard = plugin.getScoreboard(p);
            scoreboard.removeLine(4);
            scoreboard.update(p);
        }

        this.isProtected = prot;
    }

    public boolean isProtected() {
        return isProtected;
    }

}
