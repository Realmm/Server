package me.realmm.goldeconomy;

import me.realmm.goldeconomy.commands.*;
import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.entities.NamedItem;
import me.realmm.goldeconomy.listeners.PlayerJoinListener;
import me.realmm.goldeconomy.util.EconUtil;
import net.jamesandrew.commons.database.mongo.callback.IDatabaseResultCallback;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.file.YMLFile;
import net.jamesandrew.realmlib.item.Potion;
import net.jamesandrew.realmlib.register.Register;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public class GoldEconomy extends RealmLib {

    private YMLFile messages;

    @Override
    protected void onStart() {
        EconUtil.initMongoDB(new IDatabaseResultCallback<Void>() {
            @Override
            protected void onReceived(Void value) {
                Bukkit.getOnlinePlayers().forEach(o -> {
                    EconPlayer ep = EconUtil.getEconPlayer(o);
                    ep.updateScoreboard();
                });
            }
        });

        this.messages = new YMLFile("messages");
        saveDefaultConfig();

        registerCommands();
        registerListener();
        registerNamedItems();
    }

    @Override
    protected void onEnd() {

    }

    public static GoldEconomy get() {
        return JavaPlugin.getPlugin(GoldEconomy.class);
    }

    public YMLFile getMessages() {
        return messages;
    }

    private void registerNamedItems() {
        Stream.of(
                new NamedItem("strp2", "Potion of Strength", Material.POTION, Potion.STRENGTH_TWO.getId()),
                new NamedItem("swp2", "Potion of Swiftness", Material.POTION, Potion.SWIFTNESS_TWO.getId()),
                new NamedItem("frp1e", "Potion of Fire Resistance", Material.POTION, Potion.FIRE_RESISTANCE_EXTENDED.getId()),
                new NamedItem("dp2s", "Splash Potion of Harming", Material.POTION, Potion.HARMING_SPLASH_TWO.getId()),
                new NamedItem("soup", "Mushroom Soup", Material.MUSHROOM_SOUP),
                new NamedItem("invp", "Potion of Invisibility", Material.POTION, Potion.INVISIBILITY_EXTENDED.getId())
        ).forEach(EconUtil::registerNamedItem);
    }

    private void registerListener() {
        Stream.of(
                new PlayerJoinListener()
        ).forEach(l -> Register.listener(l, this));
    }

    private void registerCommands() {
        Stream.of(
                new BalanceCommand(),
                new BalTopCommand(),
                new DepositCommand(),
                new PriceCommand(),
                new ShopCommand(),
                new WithdrawCommand(),
                new BuyCommand(),
                new SellCommand()
        ).forEach(Register::baseCommand);
    }

}
