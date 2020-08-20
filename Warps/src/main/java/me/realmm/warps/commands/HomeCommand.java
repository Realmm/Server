package me.realmm.warps.commands;

import me.realmm.warps.entities.TeleportResponse;
import me.realmm.warps.entities.WarpPlayer;
import me.realmm.warps.util.Messages;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.realmlib.command.BaseCommand;
import org.bukkit.entity.Player;

public class HomeCommand extends BaseCommand {

    public HomeCommand() {
        super("home");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            WarpPlayer wp = WarpUtil.getWarpPlayer(p);

            if (!wp.hasHome()) {
                p.sendMessage(Messages.HOME_NOT_SET);
                return;
            }

            TeleportResponse response = wp.getHome().teleport();

            switch (response) {
                case SUCCESS:
                    p.sendMessage(Messages.ATTACK_COOLDOWN);
                    break;
                case INSIDE_SPAWN:
                    p.sendMessage(Messages.WARP_SPAWN);
                    break;
                case CLOSE_PLAYER:
                    p.sendMessage(Messages.NEARBY);
                    wp.setWarpCooldown(wp.getHome().getLocation(), true);
                    break;
            }

        });
    }

}
