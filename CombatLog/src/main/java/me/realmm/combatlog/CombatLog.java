package me.realmm.combatlog;

import me.realmm.combatlog.listeners.PlayerJoinListener;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.file.YMLFile;
import net.jamesandrew.realmlib.register.Register;

import java.util.stream.Stream;

public class CombatLog extends RealmLib {

    private YMLFile data;

    @Override
    protected void onStart() {
        this.data = new YMLFile("data");
        registerListeners();
    }

    @Override
    protected void onEnd() {

    }

    private void registerListeners() {
        Stream.of(
                new PlayerJoinListener()
        ).forEach(l -> Register.listener(l, this));
    }

    public YMLFile getData() {
        return data;
    }

}
