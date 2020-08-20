package me.realmm.warps.runnables;

import me.realmm.warps.entities.TeleportAttackCooldownPlayer;
import me.realmm.warps.util.Messages;

public class TeleportAttackCooldownRunnable extends ScoreboardCountdownRunnable {

    public TeleportAttackCooldownRunnable(TeleportAttackCooldownPlayer a, int cooldown, double interval) {
        super(a.getPlayer(), Messages.SCOREBOARD_COOLDOWN, () -> a.setAttackCooldown(false), cooldown, interval);
    }
}
