package me.realmm.goldeconomy.commands;

import me.realmm.goldeconomy.commands.sub.PriceAmountCommand;
import net.jamesandrew.realmlib.command.BaseCommand;

public class PriceCommand extends BaseCommand {

    public PriceCommand() {
        super("price");
        addSubCommands(new PriceAmountCommand());
    }

}
