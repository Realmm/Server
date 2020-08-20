package me.realmm.warps.listeners;

import me.realmm.warps.entities.TeleportAttackCooldownPlayer;
import me.realmm.warps.util.Messages;
import me.realmm.warps.util.WarpUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void on(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        if (WarpUtil.getAttackCooldownPlayers(p).stream().anyMatch(TeleportAttackCooldownPlayer::isOnWarpCooldown)) {
            WarpUtil.getAttackCooldownPlayers(p).forEach(t -> t.setWarpCooldown(null, false));
            p.sendMessage(Messages.WARP_CANCELLED);
        }
    }

}
