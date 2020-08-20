package me.realmm.goldeconomy.commands.sub;

import net.jamesandrew.realmlib.command.SubCommand;

public class PriceAmountCommand extends SubCommand {

    public PriceAmountCommand() {
        super("");
        setPlaceHolder((s, a) -> a[1]);
        addSubCommands(new PriceItemCommand());
    }

}
