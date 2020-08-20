package me.realmm.track.commands;

import me.realmm.track.utils.Messages;
import me.realmm.track.utils.Perm;
import me.realmm.track.utils.TrackerUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import org.bukkit.entity.Player;

public class AllSubCommand extends SubCommand {

    public AllSubCommand() {
        super("all");
        addPermission(Perm.TRACK);
        setPermissionMessage(Messages.NO_PERMISSION);
        setExecution((sender, args) -> {
            if (!(sender instanceof Player)) return;
            Player p = (Player) sender;
            TrackerUtil.trackAll(p);
        });
    }

}
