package me.realmm.goldeconomy.commands.sub;

import net.jamesandrew.realmlib.command.SubCommand;

public class BuyItemCommand extends SubCommand {

    public BuyItemCommand() {
        super("");
        setPlaceHolder((s, a) -> a[2]);
        addSubCommands(new BuyLimitCommand());
    }

}
