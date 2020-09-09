package me.realmm.salvaging.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.realmm.salvaging.blocks.ItemBlock;
import me.realmm.salvaging.utils.BlockUtil;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        Block b = e.getClickedBlock();
        ItemStack i = p.getItemInHand();

        if (b == null || i == null || e.getAction() != Action.RIGHT_CLICK_BLOCK || !BlockUtil.isItemBlock(b.getType())) return;

        ItemBlock itemBlock = BlockUtil.asItemBlock(b.getType());
        itemBlock.drop(b, i);
        if (i.getAmount() > 1) {
            i.setAmount(i.getAmount() - 1);
        } else p.setItemInHand(null);
    }

}
