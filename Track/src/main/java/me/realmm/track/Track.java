package me.realmm.track;

import me.realmm.track.commands.TrackCommand;
import me.realmm.track.listeners.ItemSpawnListener;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.file.YMLFile;
import net.jamesandrew.realmlib.register.Register;

import java.util.stream.Stream;

public class Track extends RealmLib {

    private YMLFile ymlFile;

    @Override
    public void onStart() {
        this.ymlFile = new YMLFile("messages");
        saveDefaultConfig();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onEnd() {

    }

    public YMLFile getMessages() {
        return ymlFile;
    }

    private void registerListeners() {
        Stream.of(
                new ItemSpawnListener()
        ).forEach(l -> Register.listener(l, this));
    }

    private void registerCommands() {
        Stream.of(
                new TrackCommand("track")
        ).forEach(Register::baseCommand);
    }

}
