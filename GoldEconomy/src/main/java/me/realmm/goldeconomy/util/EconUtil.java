package me.realmm.goldeconomy.util;

import me.realmm.goldeconomy.database.EconMongo;
import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.entities.NamedItem;
import net.jamesandrew.commons.database.mongo.callback.IDatabaseResultCallback;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class EconUtil {

    private static EconMongo econMongo;
    private static Set<EconPlayer> econPlayers = new HashSet<>();
    private static Set<NamedItem> namedItems = new HashSet<>();

    private EconUtil(){}

    public static void initMongoDB(IDatabaseResultCallback<Void> callback) {
        econMongo = new EconMongo(callback);
    }

    public static EconMongo getMongoDB() {
        return econMongo;
    }

    public static EconPlayer getEconPlayer(OfflinePlayer p) {
        return econPlayers.stream().filter(e -> e.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())).findFirst().orElseThrow(() -> new IllegalArgumentException("Player not registered as EconPlayer"));
    }

    public static void cacheEconPlayer(Player p) {
        if (econPlayers.stream().anyMatch(e -> e.getOfflinePlayer().getUniqueId().equals(p.getUniqueId()))) return;
        econPlayers.add(new EconPlayer(p));
    }

    public static void cacheEconPlayers() {
        if (!econPlayers.isEmpty()) throw new IllegalArgumentException("Attempted to cache all EconPlayers on non-empty set");
        econPlayers = econMongo.getEconPlayers().join();
    }

    public static Collection<EconPlayer> getEconPlayers() {
        return Collections.unmodifiableCollection(econPlayers);
    }

    public static void registerNamedItem(NamedItem namedItem) {
        if (isNamedItem(namedItem.getAlias())) throw new IllegalArgumentException("NamedItem alias already registered");
        namedItems.add(namedItem);
    }

    public static boolean isNamedItem(String s) {
        return namedItems.stream().anyMatch(n -> n.getAlias().equalsIgnoreCase(s));
    }

    public static NamedItem findNamedItem(String s) {
        return namedItems.stream().filter(n -> n.getAlias().equalsIgnoreCase(s)).findFirst().orElseThrow(() -> new IllegalArgumentException("No NamedItem with alias " + s));
    }

    public static Collection<NamedItem> getNamedItems() {
        return Collections.unmodifiableCollection(namedItems);
    }

    public static BigDecimal adjustDecimal(BigDecimal val) {
        return val.setScale(4, RoundingMode.HALF_UP);
    }

}
