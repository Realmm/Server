package me.realmm.extra.helpcommand;

import me.realmm.extra.Extra;
import net.jamesandrew.realmlib.command.BaseCommand;
import net.jamesandrew.realmlib.lang.Lang;

import java.util.List;

public class HelpCommand extends BaseCommand {

    public HelpCommand() {
        super("help");
        setExecution((s, a) -> {
            List<String> list = Extra.get().getConfig().getStringList("help");
            StringBuilder b = new StringBuilder();
            list.forEach(l -> {
                b.append(Lang.color(l));
                b.append("\n");
            });
            s.sendMessage(b.toString().trim());
        });
    }

}
