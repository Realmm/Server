package me.realmm.warps.commands;

import me.realmm.warps.entities.Home;
import me.realmm.warps.entities.WarpPlayer;
import me.realmm.warps.util.Messages;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.realmlib.command.BaseCommand;
import org.bukkit.entity.Player;

public class SetHomeCommand extends BaseCommand {

    public SetHomeCommand() {
        super("sethome");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            WarpPlayer wp = WarpUtil.getWarpPlayer(p);

            if (!wp.canSetWarp()) {
                p.sendMessage(Messages.SET_WARP_SPAWN);
                return;
            }

            wp.setHome(new Home(wp, p.getLocation()));
            p.sendMessage(Messages.HOME_SET);
        });
    }

}
