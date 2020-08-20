package me.realmm.toggleping;

import me.realmm.toggleping.commands.TogglePingCommand;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.register.Register;

public class TogglePing extends RealmLib {

    @Override
    protected void onStart() {
        saveDefaultConfig();
        Register.baseCommand(new TogglePingCommand());
    }

    @Override
    protected void onEnd() {

    }
}
