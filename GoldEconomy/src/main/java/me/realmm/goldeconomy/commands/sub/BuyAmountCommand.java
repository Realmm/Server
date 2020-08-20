package me.realmm.goldeconomy.commands.sub;

import net.jamesandrew.realmlib.command.SubCommand;

public class BuyAmountCommand extends SubCommand {

    public BuyAmountCommand() {
        super("");
        setPlaceHolder((s, a) -> a[1]);
        addSubCommands(new BuyItemCommand());
    }

}
