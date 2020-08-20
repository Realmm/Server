package me.realmm.teams;

import me.realmm.teams.commands.PointsTopCommand;
import me.realmm.teams.commands.TeamCommand;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.listeners.*;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.commons.database.mongo.callback.IDatabaseResultCallback;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.file.YMLFile;
import net.jamesandrew.realmlib.register.Register;
import org.bukkit.Bukkit;

import java.util.stream.Stream;

public class Teams extends RealmLib {

    private YMLFile messages;

    @Override
    protected void onStart() {
        TeamUtil.initMongoDB(new IDatabaseResultCallback<Void>() {
            @Override
            protected void onReceived(Void value) {
                Bukkit.getOnlinePlayers().forEach(o -> {
                    if (!TeamUtil.isInTeam(o)) return;
                    TeamPlayer tp = TeamUtil.getTeamPlayer(o);
                    tp.updateScoreboard();
                });
            }
        });

        this.messages = new YMLFile("messages");
        registerListeners();
        registerCommands();
    }

    @Override
    protected void onEnd() {

    }

    public YMLFile getMessages() {
        return messages;
    }

    private void registerCommands() {
        Stream.of(
                new PointsTopCommand(),
                new TeamCommand()
        ).forEach(Register::baseCommand);
    }

    private void registerListeners() {
        Stream.of(
                new EntityDamageByEntityListener(),
                new AsyncPlayerChatListener(),
                new PlayerJoinListener(),
                new PlayerQuitListener(),
                new PlayerDeathListener()
        ).forEach(l -> Register.listener(l, this));
    }

}
