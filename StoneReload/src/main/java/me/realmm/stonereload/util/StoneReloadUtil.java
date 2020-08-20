package me.realmm.stonereload.util;

import me.realmm.stonereload.StoneReload;
import me.realmm.stonereload.entities.CuboidSection;
import me.realmm.stonereload.entities.ReloadPlayer;
import me.realmm.stonereload.runnables.ReloadRunnable;
import net.jamesandrew.commons.logging.Logger;
import net.jamesandrew.commons.number.Number;
import net.jamesandrew.realmlib.file.YMLFile;
import net.jamesandrew.realmlib.location.BlockChange;
import net.jamesandrew.realmlib.location.BlockChanger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public final class StoneReloadUtil {

    private static final Set<CuboidSection> cuboids = new HashSet<>();
    private static final Set<ReloadPlayer> reloadPlayers = new HashSet<>();
    private static ReloadRunnable runnable;
    private static final Map<Integer, CuboidSection> sectionIdMap = new HashMap<>();
    private static BlockChanger changer;
    private static boolean isInit;

    private StoneReloadUtil(){}

    public static boolean isInitialising() {
        return isInit;
    }

    public static CompletableFuture<Void> initChanger() {
        isInit = true;
        changer = new BlockChanger(Bukkit.getWorld(ConfigUtil.WORLD_NAME));
        changer.setMaxChanges(ConfigUtil.BLOCK_CHANGE_AMOUNT);
        changer.setTick(ConfigUtil.BLOCK_UPDATE_INTERVAL);
        changer.callback(() -> {
            Bukkit.broadcastMessage("done");
            StoneReloadUtil.getReloadPlayers().forEach(ReloadPlayer::updateScoreboard);
        });

//        StoneReloadUtil.getCuboids().forEach(Cuboid::loadChunks);

//        for (int i = 0; i < 300000; i++) {
//            Logger.debug("i3: " + i);
//        }

        Logger.debug("size: " + StoneReloadUtil.getCuboids().size());
        return CompletableFuture.runAsync(() -> {
//            for (int i = 0; i < 300000; i++) {
//                Logger.debug("i4: " + i);
//            }
//            Queue<BlockChange> queue = new LinkedList<>();
//            Queue<Integer> queue = new ArrayDeque<>();
            int size = StoneReloadUtil.getCuboids().stream().flatMapToInt(c -> IntStream.of(c.getBlockVectors().size())).sum();
            int[] count = {0};
            StoneReloadUtil.getCuboids().forEach(c -> {
                Logger.debug("aa " + StoneReloadUtil.getId(c));

                try {
                    c.getBlockVectors().forEach(b -> {
                        double d = count[0]/(double) size * 100;
//                        Logger.debug("d13ab (" + Number.round(d, 2) + "%) (count: " + count[0] + " total: " + size + " )");
                        count[0]++;
                        if (b.getY() <= 5) return;
//                    if (b.getY() <= ConfigUtil.STONE_Y) {
//                        queue.add(1);
//                    } else queue.add(0);
                        if (b.getY() <= ConfigUtil.STONE_Y) {
                            changer.addChange(new BlockChange(b, Material.STONE, (byte) 0));
                        } else changer.addChange(new BlockChange(b, Material.AIR, (byte) 0));
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
//            changer.setBlockChanges(queue);
            isInit = false;
        });
    }

    public static boolean hasChanger() {
        return changer != null;
    }

    public static BlockChanger getChanger() {
        return changer;
    }

    public static int getId(CuboidSection c) {
        return sectionIdMap.entrySet().stream().filter(e -> e.getValue() == c).map(Map.Entry::getKey).findFirst().orElse(-1);
    }

    public static CuboidSection getCuboid(int id) {
        return sectionIdMap.get(id);
    }

    public static boolean isValidCuboidId(int id) {
        return sectionIdMap.containsKey(id);
    }
    
    public static boolean cuboidHasId(CuboidSection cuboidSection, int id) {
        return sectionIdMap.get(id) == cuboidSection;
    }

    public static void registerCuboid(CuboidSection c) {
        registerCuboid(c, -1);
    }

    public static void registerCuboid(CuboidSection c, int id) {
        if (sectionIdMap.containsValue(c)) return;
        if (id < 0) {
            sectionIdMap.put(Number.getNextInt(sectionIdMap.keySet(), 0), c);
        } else sectionIdMap.put(id, c);
        cuboids.add(c);
        updateCuboidSections();

        if (!isReloading() && !isInit) startReload();
//        if (!isReloading()) startReload();
    }

    public static void removeCuboid(CuboidSection c) {
        if (cuboids.size() > 0) {
            cuboids.remove(c);
            sectionIdMap.entrySet().removeIf(e -> e.getValue() == c);
            updateCuboidSections();
            if (cuboids.size() == 0 && !isInit) stopReload();
//            if (cuboids.size() == 0) stopReload();
        }
    }

    public static Collection<CuboidSection> getCuboids() {
        return Collections.unmodifiableCollection(cuboids);
    }

    public static void cacheReloadPlayer(ReloadPlayer r) {
        if (reloadPlayers.stream().anyMatch(re -> re.getOfflinePlayer().getUniqueId().equals(r.getOfflinePlayer().getUniqueId()))) return;
        reloadPlayers.add(r);
    }

    public static void cacheCuboids() {
        final String path = "cuboids";
        if (!cuboids.isEmpty()) throw new IllegalArgumentException("Attempted to register all CuboidSections on non-empty set");
        Map<Integer, CuboidSection> sections = new HashMap<>();
        YMLFile data = JavaPlugin.getPlugin(StoneReload.class).getData();
        if (data.getConfig().getString(path) == null || data.getConfig().getConfigurationSection(path).getKeys(false).size() <= 0) return;
        for (String key : data.getConfig().getConfigurationSection(path).getKeys(false)) {
            if (!Number.isInt(key)) continue;
            String s = data.getConfig().getString(path + "." + key);
            int i = Number.getInt(key);
            sections.put(i, new CuboidSection(s.trim()));
        }
        sections.forEach((i, c) -> registerCuboid(c, i));
    }

    public static Collection<ReloadPlayer> getReloadPlayers() {
        return Collections.unmodifiableCollection(reloadPlayers);
    }

    public static ReloadPlayer getReloadPlayer(OfflinePlayer p) {
        return reloadPlayers.stream().filter(r -> r.getOfflinePlayer().getUniqueId().equals(p.getUniqueId())).findFirst().orElseThrow(() -> new IllegalArgumentException("ReloadPlayer not registered"));
    }

    public static void updateCuboidSections() {
        YMLFile data = JavaPlugin.getPlugin(StoneReload.class).getData();
        if (sectionIdMap.size() == 0) {
            data.setSaveReload("cuboids", null);
            return;
        }
        sectionIdMap.forEach((i, c) -> data.setSaveReload("cuboids." + i, c.serialize()));
    }

    public static void startReload() {
        if (cuboids.isEmpty() || (changer != null && changer.isStarted())) return;
        if (runnable != null) runnable.cancel();
        runnable = new ReloadRunnable();
        runnable.runTaskTimer(StoneReload.get(), 0L, ConfigUtil.RELOAD * 20);
    }

    public static boolean isReloading() {
        return runnable != null;
    }

    public static void stopReload() {
        if (runnable != null) runnable.cancel();
        runnable = null;
    }

}
