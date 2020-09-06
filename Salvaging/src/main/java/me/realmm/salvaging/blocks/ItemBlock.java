package me.realmm.salvaging.blocks;

import org.bukkit.Material;

public abstract class ItemBlock {

    private Material material;

    public ItemBlock(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial() {
        this.material = material;
    }



}
