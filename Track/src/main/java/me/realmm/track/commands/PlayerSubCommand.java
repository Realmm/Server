package me.realmm.track.commands;

import me.realmm.track.utils.Messages;
import me.realmm.track.utils.Perm;
import me.realmm.track.utils.TrackerUtil;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerSubCommand extends SubCommand {

    public PlayerSubCommand() {
        super("");
        addPermission(Perm.TRACK);
        setPermissionMessage(Messages.NO_PERMISSION);
        setPlaceHolder((sender, args) -> args[1]);
        setExecution((sender, args) -> {
            if (!(sender instanceof Player)) return;
            Player p = (Player) sender;
            OfflinePlayer toTrack = Bukkit.getOfflinePlayer(args[1]);
            if (!toTrack.hasPlayedBefore()) {
                p.sendMessage(new Placeholder(Messages.PLAYER_NOT_PLAYED).setPlaceholders("player").setToReplace(args[1]).toString());
                return;
            }
            if (!toTrack.isOnline()) {
                p.sendMessage(new Placeholder(Messages.PLAYER_NOT_ONLINE).setPlaceholders("player").setToReplace(toTrack.getName()).toString());
                return;
            }

            TrackerUtil.trackPlayer(p, (Player) toTrack);
        });
    }

}
