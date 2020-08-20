package me.realmm.goldeconomy.commands.sub;

import net.jamesandrew.realmlib.command.SubCommand;

public class SellAmountCommand extends SubCommand {

    public SellAmountCommand() {
        super("");
        setPlaceHolder((s, a) -> a[1]);
        addSubCommands(new SellItemCommand());
    }

}
