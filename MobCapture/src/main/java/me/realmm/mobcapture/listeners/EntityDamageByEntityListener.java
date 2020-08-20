package me.realmm.mobcapture.listeners;

import me.realmm.mobcapture.utils.ConfigUtil;
import me.realmm.mobcapture.utils.Messages;
import net.jamesandrew.commons.number.Number;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;

import static org.bukkit.entity.EntityType.*;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() != EntityType.EGG) return;
        Projectile egg = (Projectile) e.getDamager();
        ProjectileSource projectileSource = egg.getShooter();
        if (!(projectileSource instanceof Player)) return;

        Player p = (Player) projectileSource;
        Entity mob = e.getEntity();

        if (!(p.getLocation().distance(mob.getLocation()) >= ConfigUtil.DISTANCE_TO_CAPTURE)) {
            p.sendMessage(Messages.NOT_FAR_ENOUGH);
            return;
        }

        EntityType[] types = {MUSHROOM_COW, COW, CHICKEN, SQUID, VILLAGER, PIG, BLAZE, CAVE_SPIDER, CREEPER, GHAST, MAGMA_CUBE, SILVERFISH, SKELETON, SLIME, SPIDER, WITCH};

        if (Arrays.stream(types).noneMatch(type -> type == mob.getType())) return;

        evaluate(p, mob);
    }

    private void evaluate(Player p, Entity mob) {
        if (p.getLevel() >= ConfigUtil.GUARANTEED_CAPTURE_LEVEL) {
            setExpToZero(p);
            dropEgg(mob);
            p.sendMessage(Messages.SUCCESS);
            return;
        }

        if (p.getLevel() >= ConfigUtil.LOWEST_CAPTURE_LEVEL) {
            setExpToZero(p);
            if (Number.getRandom(1, 100) <= ConfigUtil.PERCENT_SUCCESS) {
                dropEgg(mob);
                p.sendMessage(Messages.SUCCESS);
                return;
            }
            p.sendMessage(Messages.FAILURE);
            return;
        }
        p.sendMessage(Messages.LOW_LEVEL);
    }

    private void setExpToZero(Player p) {
        p.setLevel(0);
        p.setExp(0);
    }

    private void dropEgg(Entity mob) {
        short data = mob.getType().getTypeId();
        mob.getWorld().dropItem(mob.getLocation(), new ItemStack(Material.MONSTER_EGG, 1, data));
        mob.remove();
    }

}
