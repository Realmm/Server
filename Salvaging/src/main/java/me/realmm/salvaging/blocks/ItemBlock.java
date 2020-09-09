package me.realmm.salvaging.blocks;

import org.bukkit.Material;

public abstract class ItemBlock {

    private Material block, drop;

    public ItemBlock(Material block, Material drop) {
        this.block = block;
        this.drop = drop;
    }

    public Material getBlockType() {
        return block;
    }

    public Material setBlockType(Material material) {
        this.block = material;
    }

    public Material getDropType() {
        return drop;
    }

    public void setDropType(Material material) {
        this.drop = material;
    }

    



}
