package me.realmm.instantsoup.listeners;

import me.realmm.instantsoup.util.ConfigUtil;
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

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getPlayer().getItemInHand() == null) return;

        ItemStack i = e.getPlayer().getItemInHand();

        if (i.getType() != Material.MUSHROOM_SOUP) return;

        double toHeal = getRemaining(p.getHealth(), ConfigUtil.HEAL_AMOUNT);

        if (toHeal == 0) {
            int toFeed = getRemaining(p.getFoodLevel(), ConfigUtil.FEED_AMOUNT);
            if (toFeed == 0) return;
            p.setFoodLevel(p.getFoodLevel() + toFeed);
        } else {
            p.setHealth(p.getHealth() + toHeal);
        }

        i.setType(Material.BOWL);
    }

    private int getRemaining(double current, double amount) {
        return (int) (current >= 20 ? 0 : current + (amount * 2) > 20 ? 20 - current : amount * 2);
    }

}
