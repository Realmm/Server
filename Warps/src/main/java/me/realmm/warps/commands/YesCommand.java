package me.realmm.warps.commands;

import me.realmm.warps.entities.Warp;
import me.realmm.warps.entities.WarpPlayer;
import me.realmm.warps.util.Messages;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.realmlib.command.BaseCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;

public class YesCommand extends BaseCommand {

    public YesCommand() {
        super("yes");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            WarpPlayer wp = WarpUtil.getWarpPlayer(p);

            if (!wp.isOverwriting()) return;

            Warp warp = wp.getOverwriting().overwrite();
            p.sendMessage(new Placeholder(Messages.OVERWRITTEN_WARP).setPlaceholders("warp").setToReplace(warp.getName()).toString());
        });
    }

}
