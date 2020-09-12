package me.realmm.extra;

import me.realmm.extra.border.PlayerMoveListener;
import me.realmm.extra.chunkload.commands.LoadChunksCommand;
import me.realmm.extra.chunkload.util.ChunkUtil;
import me.realmm.extra.disableend.listeners.PlayerTeleportListener;
import me.realmm.extra.helpcommand.HelpCommand;
import me.realmm.extra.joinleavemessages.PlayerJoinListener;
import me.realmm.extra.joinleavemessages.PlayerQuitListener;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.register.Register;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Stream;

public class Extra extends RealmLib {
    @Override
    protected void onStart() {
        saveDefaultConfig();
        registerListeners();
        registerCommands();

        new BukkitRunnable() {
            @Override
            public void run() {
                ChunkUtil.initChanger();
            }
        }.runTaskLater(RealmLib.get(), 2);
    }

    @Override
    protected void onEnd() {

    }

    private void registerListeners() {
        Stream.of(
                new PlayerTeleportListener(),
                new PlayerJoinListener(),
                new PlayerQuitListener(),
                new PlayerMoveListener()
        ).forEach(l -> Register.listener(l, this));
    }

    private void registerCommands() {
        Stream.of(
                new LoadChunksCommand(),
                new HelpCommand()
        ).forEach(Register::baseCommand);
    }

}
