package me.realmm.serverscoreboard;

import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.lang.Lang;
import net.jamesandrew.realmlib.register.Register;
import net.jamesandrew.realmlib.scoreboard.RealmScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class ServerScoreboard extends RealmLib {

    private final Map<UUID, RealmScoreboard> scoreboards = new HashMap<>();

    @Override
    protected void onStart() {
        registerListeners();
        saveDefaultConfig();

        Arrays.stream(Bukkit.getOfflinePlayers()).forEach(p -> {
            setScoreboard(p);
            if (p.isOnline()) getScoreboard(p).update((Player) p);
        });
    }

    @Override
    protected void onEnd() {
        Bukkit.getOnlinePlayers().stream().filter(this::hasScoreboard).forEach(p -> getScoreboard(p).remove(p));
    }

    void setScoreboard(OfflinePlayer p) {
        RealmScoreboard sb = new RealmScoreboard(Lang.color(getConfig().getString("scoreboard-title")));
        String line = "------------------------";
        sb.setLine(1, ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + line);
        sb.setLine(15, ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + line);
        scoreboards.put(p.getUniqueId(), sb);
    }

    public boolean hasScoreboard(OfflinePlayer p) {
        return scoreboards.containsKey(p.getUniqueId());
    }

    public RealmScoreboard getScoreboard(OfflinePlayer p) {
        return scoreboards.get(p.getUniqueId());
    }

    private void registerListeners() {
        Stream.of(
                new PlayerJoinListener()
        ).forEach(l -> Register.listener(l, this));
    }

}
