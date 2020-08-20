package me.realmm.toggleping.commands;

import me.realmm.toggleping.utils.ToggleUtil;
import net.jamesandrew.realmlib.command.BaseCommand;
import org.bukkit.entity.Player;

public class TogglePingCommand extends BaseCommand {

    public TogglePingCommand() {
        super("toggleping");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            ToggleUtil.toggle(p, !ToggleUtil.isToggled(p));
        });
    }

}
