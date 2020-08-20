package me.realmm.mobcapture;

import me.realmm.mobcapture.listeners.EntityDamageByEntityListener;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.file.YMLFile;
import net.jamesandrew.realmlib.register.Register;

public class MobCapture extends RealmLib {

    private YMLFile messages;

    @Override
    protected void onStart() {
        this.messages = new YMLFile("messages");
        saveDefaultConfig();
        Register.listener(new EntityDamageByEntityListener(), this);
    }

    @Override
    protected void onEnd() {

    }

    public YMLFile getMessages() {
        return messages;
    }


}
