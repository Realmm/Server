package me.realmm.bottledxp.listeners;

import net.jamesandrew.commons.number.Number;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand().getType() != Material.EXP_BOTTLE || (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
        if (!p.getItemInHand().hasItemMeta()) return;
        e.setCancelled(true);

        int exp = p.getItemInHand().getItemMeta().getLore().stream().map(ChatColor::stripColor).filter(Number::isInt).map(Number::getInt).findFirst().orElseThrow(() -> new IllegalArgumentException("No exp value"));
        p.giveExp(exp);

        if (e.getItem().getAmount() <= 1) {
            p.getInventory().setItem(p.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
        } else e.getItem().setAmount(e.getItem().getAmount() - 1);
    }

}
