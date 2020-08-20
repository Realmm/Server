package me.realmm.goldeconomy.commands.sub;

import net.jamesandrew.realmlib.command.SubCommand;

public class SellItemCommand extends SubCommand {

    public SellItemCommand() {
        super("");
        setPlaceHolder((s, a) -> a[2]);
        addSubCommands(new SellCostCommand());
    }

}
