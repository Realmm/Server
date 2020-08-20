package me.realmm.goldeconomy.listeners;

import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.util.EconUtil;
import net.jamesandrew.commons.database.mongo.callback.IDatabaseResultCallback;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        EconUtil.cacheEconPlayer(p);
        EconUtil.getMongoDB().createShop(p, new IDatabaseResultCallback<Void>() {
            @Override
            protected void onReceived(Void value) {
                EconPlayer ep = EconUtil.getEconPlayer(p);
                ep.updateScoreboard();
            }
        });
    }

}
