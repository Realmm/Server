package me.realmm.spawnprotection.commands;

import me.realmm.spawnprotection.entities.ProtectPlayer;
import me.realmm.spawnprotection.utils.Messages;
import me.realmm.spawnprotection.utils.Perm;
import me.realmm.spawnprotection.utils.ProtectUtil;
import net.jamesandrew.realmlib.command.BaseCommand;
import org.bukkit.entity.Player;

public class ProtectCommand extends BaseCommand {

    public ProtectCommand() {
        super("protect");
        setPermission(Perm.PROTECT_USE);
        setPermissionMessage(Messages.NO_PERMISSION);
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;

            Player p = (Player) s;

            ProtectPlayer pp = ProtectUtil.getProtectPlayer(p);

            if (!pp.isSetting()) {
                pp.getProtection().setPointOne(p);
                p.sendMessage(Messages.SET_POINT_ONE);
            } else {
                pp.getProtection().setPointTwo(p);
                pp.getProtection().save();
                p.sendMessage(Messages.SET_SPAWN_PROTECT);
            }

        });
    }


}
