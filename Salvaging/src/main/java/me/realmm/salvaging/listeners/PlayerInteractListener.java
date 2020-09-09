package me.realmm.salvaging.listeners;

import me.realmm.salvaging.utils.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        Block b = e.getClickedBlock();
        ItemStack i = p.getItemInHand();

        if (b == null || i == null || e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (i.getDurability() != 0) return;

       //Check if fully repaired
       //Check the recipe of the item that the player has in their hand and drop the appropriate amount of valuable item

        List<Recipe> recipes = Bukkit.getRecipesFor(i);

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

//        Logger.debug("size: " + items.size());
       BlockUtil.dropValidDrops(b, b.getLocation(), items);

    }

}
