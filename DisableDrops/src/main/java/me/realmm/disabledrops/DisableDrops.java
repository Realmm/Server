package me.realmm.disabledrops;

import me.realmm.disabledrops.listeners.EntityDeathListener;
import me.realmm.disabledrops.utils.ConfigUtil;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.register.Register;

public class DisableDrops extends RealmLib {
    @Override
    protected void onStart() {
        saveDefaultConfig();
        ConfigUtil.populateDisabledMaterials();
        Register.listener(new EntityDeathListener(), this);
    }

    @Override
    protected void onEnd() {

    }
}
