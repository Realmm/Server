package me.realmm.salvaging.blocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public abstract class ItemBlock {

    private Material block, drop;

    public ItemBlock(Material block, Material drop) {
        this.block = block;
        this.drop = drop;
    }

    public Material getBlockType() {
        return block;
    }

    public void setBlockType(Material material) {
        this.block = material;
    }

    public Material getDropType() {
        return drop;
    }

    public void setDropType(Material material) {
        this.drop = material;
    }

    /**
     * @param clickedWith The item that this block was clicked with
     * @return true if successfully dropped, otherwise false
     */
    public boolean drop(Block block, ItemStack clickedWith) {
        if (clickedWith.getDurability() != 0) return false;

       //Check if fully repaired
       //Check the recipe of the item that the player has in their hand and drop the appropriate amount of valuable item

        List<Recipe> recipes = Bukkit.getRecipesFor(clickedWith);

        //ShapedRecipe, ShapelessRecipe, FurnaceRecipe
        List<ItemStack> items = new ArrayList<>();
        for (Recipe r : recipes) {
            if (r instanceof ShapelessRecipe) {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) r;
                items = shapelessRecipe.getIngredientList();
            } else

            if (r instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) r;
                items = new ArrayList<>(shapedRecipe.getIngredientMap().values());
            } else

            if (r instanceof FurnaceRecipe) {
                FurnaceRecipe furnaceRecipe = (FurnaceRecipe) r;
                items.clear();
                items.add(furnaceRecipe.getInput());
            }
        }

        items.forEach(i -> {
            if (i == null) return;
            if (i.getType() == drop) {
                block.getWorld().dropItemNaturally(block.getLocation(), i);
            }
        });

        return true;
    }

    



}
