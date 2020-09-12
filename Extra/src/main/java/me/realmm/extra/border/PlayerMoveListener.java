package me.realmm.extra.border;

import me.realmm.extra.util.ExtraConfigUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void on(PlayerMoveEvent e) {
        if (Math.abs(e.getTo().getX()) >= ExtraConfigUtil.MAP_SIZE || Math.abs(e.getTo().getZ()) >= ExtraConfigUtil.MAP_SIZE) {
            e.getPlayer().sendMessage(ExtraConfigUtil.END_OF_WORLD);
            e.setTo(e.getFrom());
        }
    }

}
