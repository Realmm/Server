package me.realmm.warps.runnables;

import me.realmm.serverscoreboard.ServerScoreboard;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import net.jamesandrew.serverscoreboard.realmlib.scoreboard.RealmScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.math.RoundingMode;

abstract class ScoreboardCountdownRunnable extends BukkitRunnable {

    private final CountdownExecutable e;
    private final Player p;
    private final int cooldown;
    private final String message;
    private final double interval;
    private final ServerScoreboard serverScoreboard;
    private double count;

    public ScoreboardCountdownRunnable(Player p, String message, CountdownExecutable e, int cooldown, double interval) {
        this.p = p;
        this.e = e;
        this.message = message;
        this.cooldown = cooldown;
        this.interval = interval;
        this.serverScoreboard = (ServerScoreboard) Bukkit.getPluginManager().getPlugin("ServerScoreboard");
        updateScoreboard(true);
    }

    @Override
    public void run() {
        if (count >= cooldown) {
            e.execute();
            updateScoreboard(false);
            return;
        }
        updateScoreboard(true);
        count += (interval / 20);
    }

    public void updateScoreboard(boolean toKeep) {
        if (serverScoreboard != null && p != null) {
            RealmScoreboard scoreboard = serverScoreboard.getScoreboard(p);
            if (toKeep) {
                scoreboard.setBlankLine(8);
                scoreboard.setLine(12, new Placeholder(message).setPlaceholders("time").setToReplace(BigDecimal.valueOf(cooldown - count).setScale(1, RoundingMode.HALF_UP).doubleValue()).toString());
            } else scoreboard.removeLines(8, 12);

            scoreboard.update(p);
        }
    }

}
