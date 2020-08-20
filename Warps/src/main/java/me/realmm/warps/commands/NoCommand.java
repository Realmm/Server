package me.realmm.warps.commands;

import me.realmm.warps.entities.WarpPlayer;
import me.realmm.warps.util.Messages;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.realmlib.command.BaseCommand;
import org.bukkit.entity.Player;

public class NoCommand extends BaseCommand {

    public NoCommand() {
        super("no");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            WarpPlayer wp = WarpUtil.getWarpPlayer(p);

            if (!wp.isOverwriting()) return;

            wp.stopOverwriting();
            p.sendMessage(Messages.CANCELLED_OVERWRITE);
        });
    }

}
