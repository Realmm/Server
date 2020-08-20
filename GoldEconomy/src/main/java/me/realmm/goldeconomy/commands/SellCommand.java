package me.realmm.goldeconomy.commands;

import me.realmm.goldeconomy.commands.sub.SellAmountCommand;
import net.jamesandrew.realmlib.command.BaseCommand;

public class SellCommand extends BaseCommand {

    public SellCommand() {
        super("sell");
        addSubCommands(new SellAmountCommand());
    }

}
