package me.realmm.goldeconomy.commands;

import me.realmm.goldeconomy.commands.sub.BuyAmountCommand;
import net.jamesandrew.realmlib.command.BaseCommand;

public class BuyCommand extends BaseCommand {

    public BuyCommand() {
        super("buy");
        addSubCommands(new BuyAmountCommand());
    }

}
