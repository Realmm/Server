package me.realmm.bottledxp;

import me.realmm.bottledxp.listeners.InventoryClickListener;
import me.realmm.bottledxp.listeners.ExpBottleListener;
import me.realmm.bottledxp.listeners.PlayerInteractListener;
import me.realmm.bottledxp.listeners.PrepareItemCraftListener;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.register.Register;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.stream.Stream;

public class BottledXP extends RealmLib {

    @Override
    protected void onStart() {
        registerListeners();
        saveDefaultConfig();

        ItemStack expBottle = new ItemStack(Material.EXP_BOTTLE, 1);
        ShapelessRecipe r = new ShapelessRecipe(expBottle);
        r.addIngredient(1, Material.GLASS_BOTTLE);
        Bukkit.getServer().addRecipe(r);
    }

    @Override
    protected void onEnd() {

    }

    private void registerListeners() {
        Stream.of(
                new InventoryClickListener(),
                new ExpBottleListener(),
                new PlayerInteractListener(),
                new PrepareItemCraftListener()
        ).forEach(l -> Register.listener(l, this));
    }

}
