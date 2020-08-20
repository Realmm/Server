package me.realmm.goldeconomy.commands;

import me.realmm.goldeconomy.commands.sub.DepositAllCommand;
import me.realmm.goldeconomy.commands.sub.DepositAmountCommand;
import net.jamesandrew.realmlib.command.BaseCommand;

public class DepositCommand extends BaseCommand {

    public DepositCommand() {
        super("deposit");
        addAlias("d");
        addSubCommands(new DepositAllCommand(), new DepositAmountCommand());
    }

}
