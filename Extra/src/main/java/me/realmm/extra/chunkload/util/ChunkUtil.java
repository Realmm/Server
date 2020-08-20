package me.realmm.extra.chunkload.util;

import me.realmm.extra.util.ConfigUtil;
import net.jamesandrew.realmlib.location.BlockChanger;
import net.jamesandrew.realmlib.location.Changer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.util.concurrent.CompletableFuture;

public final class ChunkUtil {

    private ChunkUtil(){}

    private static boolean isInit;
    private static Changer changer;

    public static CompletableFuture<Void> initChanger() {
        isInit = true;
        changer = new BlockChanger(Bukkit.getWorld(ConfigUtil.WORLD_NAME));
        changer.setMaxChanges(ConfigUtil.CHUNK_LOAD_AMOUNT);
        changer.setTick(ConfigUtil.CHUNK_UPDATE_INTERVAL);
        changer.callback(() -> {
            Bukkit.broadcastMessage("done loading chunks");
        });

        int mapSize = ConfigUtil.MAP_SIZE;
        double size = Math.pow(mapSize / 16D * 2, 2);
//        int[] count = {1};
        return CompletableFuture.runAsync(() -> {

            for (int x = -mapSize / 16; x < mapSize / 16; x++) {
                for (int z = -mapSize / 16; z < mapSize / 16; z++) {
                    final int x1 = x, z1 = z;
//                    double d = count[0] / size * 100;
//                    Logger.debug("count: " + count[0] + " percent: (" + Number.round(d, 2) + ") size: " + size);
                    changer.addChange(c -> {
                        Chunk chunk = c.getWorld().getChunkAt(x1, z1);
                        if (!chunk.isLoaded()) chunk.load(true);
                    });
//                    count[0]++;
                }
            }

            isInit = false;
        });
    }

    public static boolean isInitialising() {
        return isInit;
    }

    public static Changer getChanger() {
        return changer;
    }

}
