package me.realmm.salvaging;

import me.realmm.salvaging.listeners.PlayerInteractListener;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.register.Register;

import java.util.stream.Stream;

public class Salvaging extends RealmLib {

    @Override
    protected void onStart() {
        registerListeners();
    }

    @Override
    protected void onEnd() {

    }

    private void registerListeners() {
        Stream.of(
                new PlayerInteractListener()
        ).forEach(l -> Register.listener(l, this));
    }

}
