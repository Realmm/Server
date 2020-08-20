package me.realmm.bottledxp.listeners;

import me.realmm.bottledxp.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PrepareItemCraftListener implements Listener {

    @EventHandler
    public void on(PrepareItemCraftEvent e) {
        if (!(e.getView().getPlayer() instanceof Player)) return;
        Player p = (Player) e.getView().getPlayer();
        if (e.getInventory().getResult().getType() != Material.EXP_BOTTLE) return;

        if (p.getExp() == 0) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            return;
        }

        ItemStack expBottle = new ItemStack(Material.EXP_BOTTLE, 1);
        List<String> lore = new ArrayList<>();
        ItemMeta bottleMeta = expBottle.getItemMeta();
        bottleMeta.setDisplayName(Messages.TITLE);
        lore.add(Messages.LORE);
        lore.add(Messages.XP_COLOR + "" + p.getTotalExperience());
        bottleMeta.setLore(lore);
        expBottle.setItemMeta(bottleMeta);
        e.getInventory().setResult(expBottle);
    }

}
