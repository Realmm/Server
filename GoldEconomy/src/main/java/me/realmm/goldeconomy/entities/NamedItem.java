package me.realmm.goldeconomy.entities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NamedItem {

    private final String s, formatted;
    private final Material material;
    private final short data;

    public NamedItem(String alias, String formatted, Material material, short data) {
        this.s = alias;
        this.formatted = formatted;
        this.material = material;
        this.data = data;
    }

    public NamedItem(String alias, String formatted, Material material) {
        this(alias, formatted, material, (short) 0);
    }

    public String getAlias() {
        return s;
    }

    public ItemStack toItemStack(int amount) {
        return new ItemStack(material, amount, data);
    }

    public String getFormattedName() {
        return formatted;
    }

}
