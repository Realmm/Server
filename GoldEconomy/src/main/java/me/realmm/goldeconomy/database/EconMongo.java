package me.realmm.goldeconomy.database;

import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoDatabase;
import me.realmm.goldeconomy.entities.EconPlayer;
import me.realmm.goldeconomy.entities.Item;
import me.realmm.goldeconomy.util.ConfigUtil;
import me.realmm.goldeconomy.util.EconUtil;
import net.jamesandrew.commons.concurrency.Latch;
import net.jamesandrew.commons.database.mongo.MongoDB;
import net.jamesandrew.commons.database.mongo.QueryBuilder;
import net.jamesandrew.commons.database.mongo.callback.IDatabaseResultCallback;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class EconMongo extends MongoDB {

    public EconMongo(IDatabaseResultCallback<Void> callback) {
        super(ConfigUtil.MONGO_DATABASE, ConfigUtil.MONGO_HOST, ConfigUtil.MONGO_PORT);
        initializeDatabase().thenRun(() -> {

            CountDownLatch latch = new CountDownLatch(Bukkit.getOnlinePlayers().size());

            Bukkit.getOnlinePlayers().forEach(o -> {
                createShop(o, new IDatabaseResultCallback<Void>() {
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

            EconUtil.cacheEconPlayers();

            callback.accept(null);
        });
    }

    public void updatePlayer(EconPlayer e) {
        createCollection(e.getOfflinePlayer().getUniqueId().toString(), new IDatabaseResultCallback<Void>() {
            @Override
            protected void onReceived(Void value) {
                String items = e.getItems().stream().map(Item::serialize).collect(Collectors.joining(", "));

                int i = 0;
                Document query = new QueryBuilder().put("_id").is(i).get();
                Document updated = new Document()
                        .append("_id", i)
                        .append("balance", e.getBalance().doubleValue())
                        .append("items", items);

                update(getCollection(e.getOfflinePlayer().getUniqueId().toString()), query, updated, null);
            }
        });

    }

    public void createShop(Player p, IDatabaseResultCallback<Void> callback) {
        createCollection(p.getUniqueId().toString(), callback);
    }


    public CompletableFuture<Set<EconPlayer>> getEconPlayers() {
        return CompletableFuture.supplyAsync(() -> {
            MongoDatabase database = getDatabase();
            Set<OfflinePlayer> list = new HashSet<>();

            Latch latch = new Latch();
            database.listCollectionNames().forEach(s -> list.add(Bukkit.getOfflinePlayer(UUID.fromString(s))), (aVoid, throwable) -> latch.countDown());
            latch.await();

            Set<EconPlayer> players = new HashSet<>();

            list.forEach(p -> {
                FindIterable<Document> cursor = getCollection(p.getUniqueId().toString()).find();
                EconPlayer ep = new EconPlayer(p);

                Latch waiter = new Latch();

                Set<Item> items = new HashSet<>();

                cursor.forEach(d -> {

                    Latch l = new Latch();
                    d.forEach((s, o) -> {
                        switch(s.toLowerCase()) {
                            case "balance":
                                ep.setBalance(BigDecimal.valueOf((double) o));
                                l.countDown();
                                break;
                            case "items":
                                String string = (String) o;
                                if (!string.equals("")) {
                                    String[] split = string.split(",");
                                    for (String item : split) {
                                        items.add(new Item(ep, item.trim()));
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
                ep.setItems(items);
                players.add(ep);
            });

            return players;
        });

    }


}
