package me.realmm.toggleping.utils;

import me.realmm.toggleping.TogglePing;
import me.realmm.toggleping.runnables.PingRunnable;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ToggleUtil {

    private static final Set<PingRunnable> runnables = new HashSet<>();

    public static void toggle(Player p, boolean state) {
        if (state) {
            PingRunnable r = new PingRunnable(p);
            r.runTaskTimer(TogglePing.get(), 0, 20);
            runnables.add(r);
        } else {
            Optional<PingRunnable> o = runnables.stream().filter(ru -> ru.getUUID().equals(p.getUniqueId())).findFirst();
            o.ifPresent(r -> {
                runnables.remove(r);
                r.cancel();
                r.run();
            });
        }
    }

    public static boolean isToggled(Player p) {
        return runnables.stream().anyMatch(r -> r.getUUID().equals(p.getUniqueId()));
    }

}
