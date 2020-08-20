package me.realmm.warps.database;

import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoDatabase;
import me.realmm.warps.entities.Home;
import me.realmm.warps.entities.Warp;
import me.realmm.warps.entities.WarpPlayer;
import me.realmm.warps.util.ConfigUtil;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.commons.concurrency.Latch;
import net.jamesandrew.commons.database.mongo.MongoDB;
import net.jamesandrew.commons.database.mongo.QueryBuilder;
import net.jamesandrew.commons.database.mongo.callback.IDatabaseResultCallback;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class WarpMongo extends MongoDB {

    public WarpMongo(IDatabaseResultCallback<Void> callback) {
        super(ConfigUtil.MONGO_DATABASE, ConfigUtil.MONGO_HOST, ConfigUtil.MONGO_PORT);
        initializeDatabase().thenRun(() -> {

            CountDownLatch latch = new CountDownLatch(Bukkit.getOnlinePlayers().size());

            Bukkit.getOnlinePlayers().forEach(o -> {
                createWarpCollection(o, new IDatabaseResultCallback<Void>() {
                    @Override
                    protected void onReceived(Void value) {
                        latch.countDown();
                    }
                });
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            WarpUtil.cacheWarpPlayers();

            callback.accept(null);
        });
    }

    public void updatePlayer(WarpPlayer wp) {
        createCollection(wp.getOfflinePlayer().getUniqueId().toString(), new IDatabaseResultCallback<Void>() {
            @Override
            protected void onReceived(Void value) {
                String warps = wp.getWarps().stream().map(Warp::serialize).collect(Collectors.joining(", "));

                int i = 0;
                Document query = new QueryBuilder().put("_id").is(i).get();
                Document updated = new Document()
                        .append("_id", i)
                        .append("home", wp.hasHome() ? wp.getHome().serialize() : "")
                        .append("warps", warps);

                update(getCollection(wp.getOfflinePlayer().getUniqueId().toString()), query, updated, null);
            }
        });

    }

    public void createWarpCollection(Player p, IDatabaseResultCallback<Void> callback) {
        createCollection(p.getUniqueId().toString(), callback);
    }


    public CompletableFuture<Set<WarpPlayer>> getWarpPlayers() {
        return CompletableFuture.supplyAsync(() -> {
            MongoDatabase database = getDatabase();
            Set<OfflinePlayer> list = new HashSet<>();

            Latch latch = new Latch();
            database.listCollectionNames().forEach(s -> list.add(Bukkit.getOfflinePlayer(UUID.fromString(s))), (aVoid, throwable) -> latch.countDown());
            latch.await();

            Set<WarpPlayer> players = new HashSet<>();

            list.forEach(p -> {
                FindIterable<Document> cursor = getCollection(p.getUniqueId().toString()).find();
                WarpPlayer wp = new WarpPlayer(p);

                Latch waiter = new Latch();

                Set<Warp> warps = new HashSet<>();

                cursor.forEach(d -> {

                    Latch l = new Latch();
                    d.forEach((s, o) -> {
                        switch(s.toLowerCase()) {
                            case "home":
                                String st = (String) o;
                                if (!st.equals("")) wp.setHome(new Home(wp, st));
                                l.countDown();
                                break;
                            case "warps":
                                String string = (String) o;
                                if (!string.equals("")) {
                                    String[] split = string.split(",");
                                    for (String warp : split) {
                                        warps.add(new Warp(wp, warp.trim()));
                                    }
                                }
                                l.countDown();
                                break;
                            default: l.countDown();
                        }
                    });

                    l.await();
                }, (v, t) -> {
                    if (t != null) t.printStackTrace();
                    waiter.countDown();
                });

                waiter.await();
                wp.setWarps(warps);
                players.add(wp);
            });

            return players;
        });

    }

}
