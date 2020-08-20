package me.realmm.bottledxp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void on(InventoryClickEvent e) {
        if (e.getView() == null) return;
        if (!(e.getView().getPlayer() instanceof Player)) return;

        Player p = (Player) e.getView().getPlayer();
        ItemStack i = e.getCurrentItem();

        if (i == null) return;
        if (i.getType() != Material.EXP_BOTTLE || e.getSlotType() != InventoryType.SlotType.RESULT) return;

        p.setExp(0.0F);
        p.setLevel(0);
        p.setTotalExperience(0);
    }

}
