package me.realmm.warps;

import me.realmm.warps.commands.*;
import me.realmm.warps.listeners.EntityDamageByEntityListener;
import me.realmm.warps.listeners.PlayerJoinListener;
import me.realmm.warps.listeners.PlayerMoveListener;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.file.YMLFile;
import net.jamesandrew.realmlib.register.Register;

import java.util.stream.Stream;

public class Warps extends RealmLib {

    private YMLFile messages;

    @Override
    protected void onStart() {
        WarpUtil.initMongoDB(null);

        saveDefaultConfig();
        this.messages = new YMLFile("messages");

        registerCommands();
        registerListeners();
    }

    @Override
    protected void onEnd() {

    }

    public YMLFile getMessages() {
        return messages;
    }

    private void registerCommands() {
        Stream.of(
                new WarpCommand(),
                new YesCommand(),
                new NoCommand(),
                new HomeCommand(),
                new SetHomeCommand()
        ).forEach(Register::baseCommand);
    }

    private void registerListeners() {
        Stream.of(
                new PlayerJoinListener(),
                new PlayerMoveListener(),
                new EntityDamageByEntityListener()
        ).forEach(l -> Register.listener(l, this));
    }

}
