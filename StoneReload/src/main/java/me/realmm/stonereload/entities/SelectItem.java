package me.realmm.stonereload.entities;

import net.jamesandrew.realmlib.nbt.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SelectItem {

    private ItemStack itemStack;

    public SelectItem() {
        itemStack = new ItemStack(Material.BLAZE_ROD, 1);
        itemStack = NBT.addTag(this.itemStack, "select", "item");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Select Item");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Left click a block, then right click a block", ChatColor.GRAY + "Then type /sr set to save"));
        itemStack.setItemMeta(meta);
    }

    public ItemStack get() {
        return itemStack;
    }

}
