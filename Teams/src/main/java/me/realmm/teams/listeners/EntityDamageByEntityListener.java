package me.realmm.teams.listeners;

import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.TeamUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player hit = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        if (!TeamUtil.isInTeam(hit) || !TeamUtil.isInTeam(damager)) return;
        TeamPlayer hitTp = TeamUtil.getTeamPlayer(hit);
        TeamPlayer damagerTp = TeamUtil.getTeamPlayer(damager);
        if (hitTp.getTeam() != damagerTp.getTeam()) return;
        Team t = hitTp.getTeam();
        if (t.getFriendlyFire()) return;

        e.setCancelled(true);
    }

}
