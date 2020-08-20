package me.realmm.goldeconomy.commands;

import me.realmm.goldeconomy.commands.sub.WithdrawAllCommand;
import me.realmm.goldeconomy.commands.sub.WithdrawAmountCommand;
import net.jamesandrew.realmlib.command.BaseCommand;

public class WithdrawCommand extends BaseCommand {

    public WithdrawCommand() {
        super("withdraw");
        addSubCommands(new WithdrawAllCommand(), new WithdrawAmountCommand());
    }

}
