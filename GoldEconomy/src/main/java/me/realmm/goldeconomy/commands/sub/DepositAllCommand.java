package me.realmm.goldeconomy.commands.sub;

import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.util.EconUtil;
import me.realmm.goldeconomy.util.Messages;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DepositAllCommand extends SubCommand {

    public DepositAllCommand() {
        super("all");
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;
            EconPlayer e = EconUtil.getEconPlayer(p);

            int amount = 0;

            for (ItemStack i : p.getInventory().getContents()) {
                if (i == null || i.getType() != Material.GOLD_INGOT) continue;
                amount += i.getAmount();
            }

            if (e.deposit(amount)) p.sendMessage(new Placeholder(Messages.DEPOSITED).setPlaceholders("amount").setToReplace(amount).toString());
        });
    }

}
