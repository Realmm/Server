package me.realmm.stonereload.listeners;

import me.realmm.stonereload.entities.ReloadPlayer;
import me.realmm.stonereload.util.Messages;
import me.realmm.stonereload.util.Perm;
import me.realmm.stonereload.util.StoneReloadUtil;
import net.jamesandrew.realmlib.nbt.NBT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        ItemStack i = e.getItem();
        if (!NBT.hasTag(i, "select") || !e.getPlayer().hasPermission(Perm.USE)) return;

        Player p = e.getPlayer();
        ReloadPlayer r = StoneReloadUtil.getReloadPlayer(p);

        switch (e.getAction()) {
            case RIGHT_CLICK_BLOCK:
                r.setLocTwo(e.getClickedBlock().getLocation());
                p.sendMessage(Messages.SELECTED_TWO);
                break;
            case LEFT_CLICK_BLOCK:
                r.setLocOne(e.getClickedBlock().getLocation());
                p.sendMessage(Messages.SELECTED);
                break;
        }

        e.setCancelled(true);

    }

}
