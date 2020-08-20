package me.realmm.stonereload.commands;

import me.realmm.stonereload.entities.CuboidSection;
import me.realmm.stonereload.entities.ReloadPlayer;
import me.realmm.stonereload.entities.SelectItem;
import me.realmm.stonereload.util.Messages;
import me.realmm.stonereload.util.Perm;
import me.realmm.stonereload.util.StoneReloadUtil;
import net.jamesandrew.commons.logging.Logger;
import net.jamesandrew.commons.number.Number;
import net.jamesandrew.realmlib.command.BaseCommand;
import net.jamesandrew.realmlib.command.SubCommand;
import net.jamesandrew.realmlib.inventory.InventoryUtil;
import net.jamesandrew.realmlib.placeholder.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

public class StoneReloadCommand extends BaseCommand {

    public StoneReloadCommand() {
        super("stonereload");
        addAlias("sr");
        setPermission(Perm.USE);
        setPermissionMessage(Messages.INCORRECT_PERMISSIONS);
        setExecution((s, a) -> {
            if (!(s instanceof Player)) return;
            Player p = (Player) s;

            ItemStack i = new SelectItem().get();

            if (!InventoryUtil.canAdd(p, i)) {
                p.sendMessage(Messages.FULL_INVENTORY);
                return;
            }

            p.getInventory().addItem(i);
            p.sendMessage(Messages.GIVEN_ITEM);
        });
        addSubCommands(new StoneReloadHelpCommand(), new StoneReloadSetCommand(), new StoneReloadListCommand(), new StoneReloadDeleteCommand());
    }

    private static class StoneReloadHelpCommand extends SubCommand {

        private StoneReloadHelpCommand() {
            super("help");
            setExecution((s, a) -> {
                StringBuilder sb = new StringBuilder();
                Messages.HELP.forEach(st -> {
                    sb.append(st);
                    sb.append("\n");
                });

                s.sendMessage(sb.toString().trim());
            });
        }

    }

    private static class StoneReloadSetCommand extends SubCommand {

        private StoneReloadSetCommand() {
            super("set");
            setExecution((s, a) -> {
                if (!(s instanceof Player)) return;
                Player p = (Player) s;
                ReloadPlayer rp = StoneReloadUtil.getReloadPlayer(p);
                if (rp.hasSetLocOne() && rp.hasSetLocTwo()) {
                    CuboidSection c = new CuboidSection(rp.getLocOne(), rp.getLocTwo());
                    StoneReloadUtil.registerCuboid(c);
                    rp.setLocOne(null);
                    rp.setLocTwo(null);
                    p.sendMessage(Messages.ADDED);
                } else p.sendMessage(Messages.SELECT_AREA);
            });
        }

    }

    private static class StoneReloadListCommand extends SubCommand {

        private StoneReloadListCommand() {
            super("list");
            addAlias("l");
            setExecution((s, a) -> {
                if (StoneReloadUtil.getCuboids().size() <= 0) {
                    s.sendMessage(Messages.NONE_SAVED);
                    return;
                }
                StringBuilder sb = new StringBuilder(Messages.LIST_TITLE);
                StoneReloadUtil.getCuboids().stream().sorted(Comparator.comparing(StoneReloadUtil::getId)).forEach(c -> {
                    sb.append("\n");
                    sb.append(new Placeholder(Messages.LIST_LINE)
                            .setPlaceholders("id", "x1", "x2", "y1", "y2", "z1", "z2")
                            .setToReplace(
                                    StoneReloadUtil.getId(c),
                                    c.getLocationOne().getBlockX(),
                                    c.getLocationTwo().getBlockX(),
                                    c.getLocationOne().getBlockY(),
                                    c.getLocationTwo().getBlockY(),
                                    c.getLocationOne().getBlockZ(),
                                    c.getLocationTwo().getBlockZ()
                            )
                    );
                });

                s.sendMessage(sb.toString());
            });
        }

    }

    private static class StoneReloadDeleteCommand extends SubCommand {

        private StoneReloadDeleteCommand() {
            super("delete");
            addAliases("del", "d");
            addSubCommands(new StoneReloadDeleteIdCommand());
        }

        private static class StoneReloadDeleteIdCommand extends SubCommand {

            private StoneReloadDeleteIdCommand() {
                super((s, a) -> a[2]);
                setExecution((s, a) -> {
                    if (!Number.isInt(getPlaceHolder())) return;
                    int id = Number.getInt(getPlaceHolder());

                    if (!StoneReloadUtil.isValidCuboidId(id)) {
                        s.sendMessage(Messages.INVALID_ID);
                        return;
                    }

                    CuboidSection c = StoneReloadUtil.getCuboid(id);

                    StoneReloadUtil.getCuboids().forEach(cuboid -> Logger.debug("cuboid: " + StoneReloadUtil.getId(cuboid)));
                    StoneReloadUtil.removeCuboid(c);
                    StoneReloadUtil.getCuboids().forEach(cuboid -> Logger.debug("cuboid2: " + StoneReloadUtil.getId(cuboid)));
                    s.sendMessage(new Placeholder(Messages.DELETED).setPlaceholders("id").setToReplace(id).toString());
                });
            }

        }

    }

}
