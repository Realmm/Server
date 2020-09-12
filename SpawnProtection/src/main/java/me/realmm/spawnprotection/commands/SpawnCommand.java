package me.realmm.spawnprotection.commands;

import me.realmm.extra.Extra;
import me.realmm.extra.util.ExtraConfigUtil;
import me.realmm.spawnprotection.entities.ProtectPlayer;
import me.realmm.spawnprotection.utils.ProtectUtil;
import me.realmm.warps.entities.TeleportResponse;
import me.realmm.warps.util.Messages;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.realmlib.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

    public SpawnCommand() {
        super("spawn");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            ProtectPlayer pp = ProtectUtil.getProtectPlayer(p);
            Location l = Bukkit.getWorld(ExtraConfigUtil.WORLD_NAME).getSpawnLocation().add(0.5, 0, 0.5);
            TeleportResponse response = WarpUtil.teleport(pp, l, false);

            switch (response) {
                case SUCCESS:
                    pp.setProtected(true);
                    WarpUtil.setAttackCooldown(false, pp.getOfflinePlayer());
                    break;
                case INSIDE_SPAWN:
                    p.sendMessage(Messages.WARP_SPAWN);
                    break;
                case CLOSE_PLAYER:
                    p.sendMessage(Messages.NEARBY);
                    pp.setWarpCooldown(l, true, () -> pp.setProtected(true), false, null);
                    WarpUtil.setAttackCooldown(false, pp.getOfflinePlayer());
                    break;
            }
        });
    }

}
