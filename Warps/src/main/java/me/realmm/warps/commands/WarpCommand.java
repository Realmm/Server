package me.realmm.warps.commands;

import me.realmm.warps.entities.TeleportResponse;
import me.realmm.warps.entities.Warp;
import me.realmm.warps.entities.WarpPlayer;
import me.realmm.warps.util.Messages;
import me.realmm.warps.util.WarpUtil;
import net.jamesandrew.realmlib.command.BaseCommand;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import net.jamesandrew.realmlib.placeholder.ReplacePattern;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

public class WarpCommand extends BaseCommand {

    public WarpCommand() {
        super("warp");
        addAlias("go");
        addSubCommands(new WarpSetSubCommand(), new WarpDelSubCommand(), new WarpListSubCommand(), new WarpPlaceSubCommand());
        setExecution((s, a) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(Messages.WARP_HELP);
            Messages.HELP_LIST.forEach(l -> {
                sb.append("\n");
                sb.append(l);
            });
            s.sendMessage(sb.toString());
        });
    }

    private static class WarpPlaceSubCommand extends SubCommand {

        private WarpPlaceSubCommand() {
            super("");
            setPlaceHolder((s, a) -> a[1]);
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;

                Player p = (Player) s;
                WarpPlayer wp = WarpUtil.getWarpPlayer(p);

                if (!wp.hasWarp(a[1])) {
                    p.sendMessage(new Placeholder(Messages.NO_WARP_FOUND).setPlaceholders("warp").setToReplace(a[1]).toString());
                    return;
                }

                Warp warp = wp.findWarp(a[1]);

                TeleportResponse response = warp.teleport();

                switch (response) {
                    case SUCCESS:
                        p.sendMessage(Messages.ATTACK_COOLDOWN);
                        break;
                    case INSIDE_SPAWN:
                        p.sendMessage(Messages.WARP_SPAWN);
                        break;
                    case CLOSE_PLAYER:
                        p.sendMessage(Messages.NEARBY);
                        wp.setWarpCooldown(warp.getLocation(), true);
                        break;
                }
            });
        }

    }

    private static class WarpSetNameSubCommand extends SubCommand {

        private WarpSetNameSubCommand() {
            super("");
            setPlaceHolder((s, a) -> a[2]);
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;

                Player p = (Player) s;
                WarpPlayer wp = WarpUtil.getWarpPlayer(p);

                if (!wp.canSetWarp()) {
                    p.sendMessage(Messages.SET_WARP_SPAWN);
                    return;
                }

                if (wp.getWarps().size() >= wp.getMaxWarps()) {
                    p.sendMessage(Messages.MAXIMUM_WARPS);
                    return;
                }

                if (wp.hasWarp(a[2])) {
                    String yes = "%yes%";
                    String no = "%no%";

                    ReplacePattern pattern = new ReplacePattern().setPlaceholders("warp").setToReplace(wp.findWarp(a[2]).getName());

                    ComponentBuilder c = new ComponentBuilder("");
                    String split = new Placeholder(Messages.OVERWRITE_WARP, pattern).toString().split(yes)[0];
                    boolean yesFirst = !split.contains(no);
                    split = split.contains(no) ? split.split(no)[0] : split;
                    c.append(split);

                    String firstHover = yesFirst ? new Placeholder(Messages.YES_HOVER).setPlaceholders("warp").setToReplace(a[2]).toString() : Messages.NO_HOVER;
                    String secondHover = yesFirst ? Messages.NO_HOVER : new Placeholder(Messages.YES_HOVER).setPlaceholders("warp").setToReplace(a[2]).toString();
                    String between = StringUtils.substringBetween(new Placeholder(Messages.OVERWRITE_WARP, pattern).toString(), yes, no);
                    String after = StringUtils.substringAfter(new Placeholder(Messages.OVERWRITE_WARP, pattern).toString(), yesFirst ? no : yes);

                    c.append(yesFirst ? Messages.YES : Messages.NO);
                    c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(firstHover)));
                    c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, yesFirst ? "/yes" : "/no"));

                    c.append(between);

                    c.append(yesFirst ? Messages.NO : Messages.YES);
                    c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(secondHover)));
                    c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, yesFirst ? "/no" : "/yes"));

                    c.append(after);

                    p.spigot().sendMessage(c.create());
                    wp.beginOverwriting(p.getLocation(), wp.findWarp(a[2]));
                } else {
                    wp.addWarp(new Warp(wp, a[2], p.getLocation()));
                    p.sendMessage(new Placeholder(Messages.SET_WARP).setPlaceholders("warp").setToReplace(a[2]).toString());
                }

            });
        }

    }

    private static class WarpDelNameSubCommand extends SubCommand {

        private WarpDelNameSubCommand() {
            super("");
            setPlaceHolder((s, a) -> a[2]);
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;

                Player p = (Player) s;
                WarpPlayer wp = WarpUtil.getWarpPlayer(p);

                if (wp.hasWarp(a[2])) {
                    Warp warp = wp.findWarp(a[2]);
                    wp.removeWarp(warp);
                    p.sendMessage(new Placeholder(Messages.DELETE_WARP).setPlaceholders("warp").setToReplace(warp.getName()).toString());
                } else {
                    p.sendMessage(new Placeholder(Messages.WARP_NOT_FOUND).setPlaceholders("warp").setToReplace(a[2]).toString());
                }

            });
        }

    }

    private static class WarpListSubCommand extends SubCommand {

        private WarpListSubCommand() {
            super("list");
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;
                WarpPlayer wp = WarpUtil.getWarpPlayer(p);

                ComponentBuilder c = new ComponentBuilder("");

                c.append(new Placeholder(Messages.WARP_LIST_TITLE).setPlaceholders("warpAmount", "warpMaxAmount").setToReplace(wp.getWarps().size(), wp.getMaxWarps()).toString());

                c.append("\n");

                c.append("[");
                c.color(Messages.WARP_LIST_BRACKET_COLOR);

                int count = 0;
                for (Warp w : wp.getWarps()) {
                    c.append(w.getName());
                    c.color(Messages.WARP_LIST_WARP_COLOR);
                    c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + w.getName()));
                    c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(new Placeholder(Messages.WARP_LIST_HOVER).setPlaceholders("warp").setToReplace(w.getName()).toString())));

                    if (count < wp.getWarps().size() - 1) {
                        c.append(Messages.WARP_LIST_SEPARATOR);
                    }

                    count++;
                }

                c.append("]");
                c.color(Messages.WARP_LIST_BRACKET_COLOR);

                p.spigot().sendMessage(c.create());
            });
        }

    }

    private static class WarpSetSubCommand extends SubCommand {

        private WarpSetSubCommand() {
            super("set");
            addSubCommands(new WarpSetNameSubCommand());
        }

    }

    private static class WarpDelSubCommand extends SubCommand {

        private WarpDelSubCommand() {
            super("del");
            addSubCommands(new WarpDelNameSubCommand());
        }

    }

}
