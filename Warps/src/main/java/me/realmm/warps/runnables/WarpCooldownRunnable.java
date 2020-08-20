package me.realmm.warps.runnables;

import me.realmm.warps.entities.TeleportAttackCooldownPlayer;
import me.realmm.warps.util.Messages;
import org.bukkit.Location;

public class WarpCooldownRunnable extends ScoreboardCountdownRunnable {

    public WarpCooldownRunnable(Location location, TeleportAttackCooldownPlayer a, int cooldown, double interval, CountdownExecutable executable, boolean performAttackCooldown, String message) {
        super(a.getPlayer(), Messages.SCOREBOARD_TELEPORTING, () -> {
            if (executable != null) executable.execute();
            a.setWarpCooldown(null, false);
            if (a.isOnline()) {
                a.getPlayer().teleport(location);
                if (performAttackCooldown) a.setAttackCooldown(true);
                if (performAttackCooldown) a.getPlayer().sendMessage(message == null ? Messages.ATTACK_COOLDOWN : message);
            }
        }, cooldown, interval);
    }

}
