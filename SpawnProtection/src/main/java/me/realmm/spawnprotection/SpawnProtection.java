package me.realmm.spawnprotection;

import me.realmm.spawnprotection.commands.ProtectCommand;
import me.realmm.spawnprotection.commands.SpawnCommand;
import me.realmm.spawnprotection.entities.ProtectPlayer;
import me.realmm.spawnprotection.entities.Protection;
import me.realmm.spawnprotection.listeners.*;
import me.realmm.spawnprotection.utils.ProtectUtil;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.register.Register;
import org.bukkit.Bukkit;

import java.util.stream.Stream;

public class SpawnProtection extends RealmLib {

    @Override
    protected void onStart() {
        ProtectUtil.cachePointsFromSave();
        ProtectUtil.cacheProtectPlayers();
        saveDefaultConfig();

        registerListeners();
        registerCommands();

        Protection prot = ProtectUtil.getProtection();
        if (!prot.isEnabled()) return;
        Bukkit.getOnlinePlayers().forEach(p -> {
            ProtectPlayer pp = ProtectUtil.getProtectPlayer(p);
            pp.setProtected(prot.isInside(p));
        });
    }

    @Override
    protected void onEnd() {

    }

    private void registerCommands() {
        Stream.of(
                new ProtectCommand(),
                new SpawnCommand()
        ).forEach(Register::baseCommand);
    }

    private void registerListeners() {
        Stream.of(
                new PlayerMoveListener(),
                new PlayerJoinListener(),
                new EntityDamageByEntityListener(),
                new FoodLevelChangeListener(),
                new EntityDamageListener()
        ).forEach(l -> Register.listener(l,this));
    }

}
