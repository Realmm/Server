package me.realmm.instantsoup;

import me.realmm.instantsoup.listeners.PlayerInteractListener;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.register.Register;

public class InstantSoup extends RealmLib {
    @Override
    protected void onStart() {
        saveDefaultConfig();
        Register.listener(new PlayerInteractListener(), this);
    }

    @Override
    protected void onEnd() {

    }
}
