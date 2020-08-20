package me.realmm.warps.entities;

import me.realmm.teams.Teams;
import me.realmm.teams.entities.Team;
import me.realmm.teams.util.TeamUtil;
import me.realmm.warps.Warps;
import me.realmm.warps.runnables.CountdownExecutable;
import me.realmm.warps.runnables.TeleportAttackCooldownRunnable;
import me.realmm.warps.runnables.WarpCooldownRunnable;
import me.realmm.warps.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class TeleportAttackCooldownPlayer {

    private final OfflinePlayer o;
    private TeleportAttackCooldownRunnable cooldown;
    private WarpCooldownRunnable warpCooldown;
    private final Teams teams;

    public TeleportAttackCooldownPlayer(OfflinePlayer o) {
        this.o = o;
        this.teams = (Teams) Bukkit.getPluginManager().getPlugin("Teams");
    }

    public OfflinePlayer getOfflinePlayer() {
        return o;
    }

    public boolean isOnAttackCooldown() {
        return cooldown != null;
    }

    public void setAttackCooldown(boolean state) {
        if (state) {
            if (cooldown != null) cooldown.cancel();
            int interval = 2;
            cooldown = new TeleportAttackCooldownRunnable(this, ConfigUtil.ATTACK_COOLDOWN, interval);
            cooldown.runTaskTimer(Warps.get(), 0, interval);
        } else {
            if (cooldown != null) {
                cooldown.cancel();
                cooldown.updateScoreboard(false);
            }
            cooldown = null;
        }
    }

    public boolean isOnline() {
        return o.isOnline();
    }

    public boolean isOnWarpCooldown() {
        return warpCooldown != null;
    }

    public void setWarpCooldown(Location location, boolean state) {
        setWarpCooldown(location, state, null, true, null);
    }

    public void setWarpCooldown(Location location, boolean state, CountdownExecutable executable, boolean performAttackCooldown, String message) {
        if (state) {
            if (warpCooldown != null) warpCooldown.cancel();
            int interval = 2;
            warpCooldown = new WarpCooldownRunnable(location,this, ConfigUtil.WARP_COOLDOWN, interval, executable, performAttackCooldown, message);
            warpCooldown.runTaskTimer(Warps.get(), 0, interval);
        } else {
            if (warpCooldown != null) {
                warpCooldown.cancel();
                warpCooldown.updateScoreboard(false);
            }
            warpCooldown = null;
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(o.getUniqueId());
    }

    public boolean canWarp() {
        if (!o.isOnline()) return false;
        Player p = getPlayer();
        List<Entity> list = p.getNearbyEntities(ConfigUtil.WARP_DISTANCE, ConfigUtil.WARP_DISTANCE, ConfigUtil.WARP_DISTANCE);

        Set<Player> nearby = list.stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .filter(pl -> {
                    if (teams != null) {
                        if (!TeamUtil.isInTeam(pl) || !TeamUtil.isInTeam(o)) return true;
                        Team t = TeamUtil.getTeam(pl);
                        Team owned = TeamUtil.getTeam(o);
                        return owned != t;
                    }
                    return true;
                }).collect(Collectors.toSet());
        return nearby.size() == 0;
    }

}
