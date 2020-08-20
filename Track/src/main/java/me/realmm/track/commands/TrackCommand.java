package me.realmm.track.commands;

import me.realmm.track.utils.Messages;
import me.realmm.track.utils.Perm;
import me.realmm.track.utils.TrackerUtil;
import net.jamesandrew.realmlib.command.BaseCommand;
import org.bukkit.entity.Player;

public class TrackCommand extends BaseCommand {
    public TrackCommand(String command) {
        super(command);
        setPermission(Perm.TRACK);
        setPermissionMessage(Messages.NO_PERMISSION);
        setExecution((sender, args) -> {
            if (!(sender instanceof Player)) return;
            Player p = (Player) sender;
            TrackerUtil.trackAll(p);
        });
        addSubCommands(new AllSubCommand(), new PlayerSubCommand());
    }

}
