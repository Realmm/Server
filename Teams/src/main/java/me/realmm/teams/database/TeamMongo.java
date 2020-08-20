package me.realmm.teams.database;

import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoDatabase;
import me.realmm.teams.entities.Team;
import me.realmm.teams.entities.TeamPlayer;
import me.realmm.teams.util.ConfigUtil;
import me.realmm.teams.util.TeamUtil;
import net.jamesandrew.commons.concurrency.Latch;
import net.jamesandrew.commons.database.mongo.MongoDB;
import net.jamesandrew.commons.database.mongo.QueryBuilder;
import net.jamesandrew.commons.database.mongo.callback.IDatabaseResultCallback;
import net.jamesandrew.realmlib.location.LocationSerializer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TeamMongo extends MongoDB {

    public TeamMongo(IDatabaseResultCallback<Void> callback) {
        super(ConfigUtil.MONGO_DATABASE, ConfigUtil.MONGO_HOST, ConfigUtil.MONGO_PORT);
        initializeDatabase().thenRun(() -> {
            TeamUtil.registerTeams();
            callback.accept(null);
        });
    }

    public void updateTeam(Team t) {
        String members = t.getMembers().stream().map(TeamPlayer::getOfflinePlayer).map(OfflinePlayer::getUniqueId).map(String::valueOf).collect(Collectors.joining(", "));
        String promoted = t.getPromoted().stream().map(TeamPlayer::getOfflinePlayer).map(OfflinePlayer::getUniqueId).map(String::valueOf).collect(Collectors.joining(", "));
        String toggled = t.getToggled().stream().map(TeamPlayer::getOfflinePlayer).map(OfflinePlayer::getUniqueId).map(String::valueOf).collect(Collectors.joining(", "));

        int i = 0;
        Document query = new QueryBuilder().put("_id").is(i).get();
        Document updated = new Document()
                .append("_id", i)
                .append("leader", t.getLeader().getOfflinePlayer().getUniqueId().toString())
                .append("password", t.getPassword())
                .append("points", t.getPoints())
                .append("ff", t.getFriendlyFire())
                .append("members", members)
                .append("promoted", promoted)
                .append("toggled", toggled)
                .append("hq", t.hasHq() ? LocationSerializer.serialize(t.getHq()) : null)
                .append("rally", t.hasRally() ? LocationSerializer.serialize(t.getRally()) : null);

        update(getCollection(t.getName()), query, updated, null);
    }

    public void createTeam(Team t, IDatabaseResultCallback<Void> callback) {
        createCollection(t.getName(), callback);
    }

    public void deleteTeam(Team t, IDatabaseResultCallback<Void> callback) {
        deleteCollection(t.getName(), callback);
    }


    public CompletableFuture<Set<Team>> getTeams() {
        return CompletableFuture.supplyAsync(() -> {
            MongoDatabase database = getDatabase();
            Set<String> teamNames = new HashSet<>();

            Latch latch = new Latch();
            database.listCollectionNames().forEach(teamNames::add, (aVoid, throwable) -> latch.countDown());
            latch.await();

            Set<Team> teams = new HashSet<>();

            teamNames.forEach(name -> {
                FindIterable<Document> cursor = getCollection(name).find();

                Latch waiter = new Latch();

                OfflinePlayer[] leader = {null};
                String[] password = {""};
                int[] points = {0};
                boolean[] ff = {false};
                Set<OfflinePlayer> members = new HashSet<>();
                Set<OfflinePlayer> promoted = new HashSet<>();
                Set<OfflinePlayer> toggled = new HashSet<>();
                Location[] locs = new Location[2];

                cursor.forEach(d -> {

                    Latch l = new Latch();
                    d.forEach((s, o) -> {
                        switch(s.toLowerCase()) {
                            case "leader":
                                leader[0] = Bukkit.getOfflinePlayer(UUID.fromString((String) o));
                                l.countDown();
                                break;
                            case "password":
                                if (o != null) password[0] = (String) o;
                                l.countDown();
                                break;
                            case "points":
                                points[0] = (int) o;
                                l.countDown();
                                break;
                            case "ff":
                                ff[0] = (boolean) o;
                                l.countDown();
                                break;
                            case "members":
                                String string = (String) o;
                                if (!string.equals("")) {
                                    String[] split = string.split(",");
                                    Arrays.stream(split).forEach(id -> members.add(Bukkit.getOfflinePlayer(UUID.fromString(id.trim()))));
                                }
                                l.countDown();
                                break;
                            case "promoted":
                                String stri = (String) o;
                                if (!stri.equals("")) {
                                    String[] split = stri.split(",");
                                    Arrays.stream(split).forEach(id -> promoted.add(Bukkit.getOfflinePlayer(UUID.fromString(id.trim()))));
                                }
                                l.countDown();
                                break;
                            case "toggled":
                                String strin = (String) o;
                                if (!strin.equals("")) {
                                    String[] split = strin.split(",");
                                    Arrays.stream(split).forEach(id -> toggled.add(Bukkit.getOfflinePlayer(UUID.fromString(id.trim()))));
                                }
                                l.countDown();
                                break;
                            case "hq":
                                if (o != null) {
                                    String st = (String) o;
                                    if (!LocationSerializer.isFormatted(st)) throw new IllegalArgumentException("Hq not formatted");
                                    locs[0] = LocationSerializer.deserialize(st);
                                }
                                l.countDown();
                                break;
                            case "rally":
                                if (o != null) {
                                    String str = (String) o;
                                    if (!LocationSerializer.isFormatted(str)) throw new IllegalArgumentException("Rally not formatted");
                                    locs[1] = LocationSerializer.deserialize(str);
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
                Team t = new Team(name, leader[0]);
                t.setPassword(password[0]);
                members.stream().filter(o -> !o.getUniqueId().equals(leader[0].getUniqueId())).forEach(t::addMember);
                promoted.stream().map(t::getMember).forEach(tp -> tp.setPromoted(true));
                toggled.stream().map(t::getMember).forEach(tp -> tp.setToggled(true));
                t.setPoints(points[0]);
                t.setHq(locs[0]);
                t.setRally(locs[1]);
                t.setFriendlyFire(ff[0]);

                teams.add(t);
            });

            return teams;
        });

    }


}
