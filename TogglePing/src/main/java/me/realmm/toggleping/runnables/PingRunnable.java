package me.realmm.toggleping.runnables;

import me.realmm.serverscoreboard.ServerScoreboard;
import me.realmm.toggleping.TogglePing;
import me.realmm.toggleping.utils.ToggleUtil;
import net.jamesandrew.realmlib.lang.Lang;
import net.jamesandrew.realmlib.nms.NMS;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import net.jamesandrew.serverscoreboard.realmlib.scoreboard.RealmScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class PingRunnable extends BukkitRunnable {

    private final UUID u;
    private final ServerScoreboard plugin;

    public PingRunnable(Player p) {
        this.u = p.getUniqueId();
        this.plugin = (ServerScoreboard) Bukkit.getPluginManager().getPlugin("ServerScoreboard");
    }

    @Override
    public void run() {
        OfflinePlayer o = Bukkit.getOfflinePlayer(u);
        if (!o.isOnline() || plugin == null) return;
        Player p = (Player) o;

        RealmScoreboard scoreboard = plugin.getScoreboard(p);
        if (ToggleUtil.isToggled(p)) {
            scoreboard.setBlankLine(8);
            scoreboard.setLine(14, new Placeholder(Lang.color(TogglePing.get().getConfig().getString("scoreboard-ping"))).setPlaceholders("ping").setToReplace(String.valueOf(getPing(p))).toString());
        } else scoreboard.removeLines(8, 14);

        scoreboard.update(p);
    }

    public UUID getUUID() {
        return u;
    }

    private int getPing(Player p) {
        try {
            Class<?> clazz = Class.forName("org.bukkit.craftbukkit." + NMS.getVersion() + ".entity.CraftPlayer");
            Object CraftPlayer = clazz.cast(p);

            Method getHandle = CraftPlayer.getClass().getMethod("getHandle");
            Object EntityPlayer = getHandle.invoke(CraftPlayer);

            Field ping = EntityPlayer.getClass().getDeclaredField("ping");

            return ping.getInt(EntityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
