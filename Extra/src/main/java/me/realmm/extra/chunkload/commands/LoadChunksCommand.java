package me.realmm.extra.chunkload.commands;

import me.realmm.extra.chunkload.util.ChunkUtil;
import net.jamesandrew.commons.number.Number;
import net.jamesandrew.commons.time.Time;
import net.jamesandrew.realmlib.command.BaseCommand;
import net.jamesandrew.realmlib.location.Changer;
import org.bukkit.ChatColor;

import java.util.concurrent.TimeUnit;

public class LoadChunksCommand extends BaseCommand {

    public LoadChunksCommand() {
        super("loadchunks");
        setPermission("chunkload.use");
        setExecution((s, a) -> {
            Changer c = ChunkUtil.getChanger();
            if (ChunkUtil.isInitialising() || (c != null && c.isStarted())) {
                if (ChunkUtil.isInitialising()) {
                    s.sendMessage(ChatColor.GRAY + "Initialising... please try again soon");
                } else {
                    s.sendMessage(ChatColor.RED + "Chunks are already loading... " + ChatColor.YELLOW + Number.round(c.getPercentageLeft(), 2) + "% \n" +
                            ChatColor.RED + "Approximately " + ChatColor.YELLOW + Time.format(TimeUnit.SECONDS, (long) Number.round(c.getApproximateSecondsLeft(), 2)) + ChatColor.RED + " left");
                }
            } else {
                if (c != null && !c.isStarted()) {
                    c.start();
                    s.sendMessage(ChatColor.GRAY + "Started loading chunks");
                }

            }
        });
    }



}
