package me.realmm.stonereload;

import me.realmm.stonereload.commands.StoneReloadCommand;
import me.realmm.stonereload.entities.ReloadPlayer;
import me.realmm.stonereload.listeners.PlayerInteractListener;
import me.realmm.stonereload.listeners.PlayerJoinListener;
import me.realmm.stonereload.util.StoneReloadUtil;
import net.jamesandrew.commons.logging.Logger;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.file.YMLFile;
import net.jamesandrew.realmlib.register.Register;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Stream;

public class StoneReload extends RealmLib {

    private YMLFile messages, data;

    @Override
    protected void onStart() {
        Logger.setEnableDebug(false);
        saveDefaultConfig();
        this.messages = new YMLFile("messages");
        this.data = new YMLFile("data");

        registerCommands();
        registerListeners();

        Bukkit.getOnlinePlayers().stream().map(ReloadPlayer::new).forEach(StoneReloadUtil::cacheReloadPlayer);
        StoneReloadUtil.cacheCuboids();
        new BukkitRunnable() {
            @Override
            public void run() {
                StoneReloadUtil.initChanger().thenRun(StoneReloadUtil::startReload);
            }
        }.runTaskLater(RealmLib.get(), 2);
    }

    @Override
    protected void onEnd() {

    }

    public YMLFile getMessages() {
        return messages;
    }

    public YMLFile getData() {
        return data;
    }

    private void registerCommands() {
        Stream.of(
                new StoneReloadCommand()
        ).forEach(Register::baseCommand);
    }

    private void registerListeners() {
        Stream.of(
                new PlayerInteractListener(),
                new PlayerJoinListener()
        ).forEach(l -> Register.listener(l,this));
    }

}
