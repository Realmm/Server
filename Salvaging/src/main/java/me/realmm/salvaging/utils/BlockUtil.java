package me.realmm.salvaging.utils;

import net.jamesandrew.commons.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class BlockUtil {

    private BlockUtil(){}

    private static final Material[] validBlockTypes = {Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.IRON_BLOCK};
    private static final Material[] validItemDrops = {Material.DIAMOND, Material.GOLD_INGOT, Material.IRON_INGOT};
    private static final Material baseBlock = Material.FURNACE;

    public static boolean isValidType(Material material) {
        return Arrays.stream(validBlockTypes).anyMatch(type -> material == type);
    }

    public static void dropValidDrops(Block clickedBlock, Location location, List<ItemStack> itemStacks) {
        itemStacks.forEach(i -> {
            if (i == null) return;
            if (isValidItemDrop(i.getType())) {
                location.getWorld().dropItemNaturally(location, i);
            }
        });
    }

    public static boolean isValidItemDrop(Material m) {
        return Arrays.stream(validItemDrops).anyMatch(type -> m == type);
    }

    //Determine if the block is in a valid setup
    public static boolean isValidSetup(Block b) {
        BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

        List<BlockFace> listBlockFaces = Arrays.asList(blockFaces);
        Collections.shuffle(listBlockFaces);

        Set<Block> foundBlocks = new HashSet<>();
        for (BlockFace bf : listBlockFaces) {
            Logger.debug("relative: " + bf);
            Block block = b.getRelative(bf);
            Logger.debug("type of relative: " + block.getType());
            if (isValidType(block.getType())) {
                foundBlocks.add(block);
            } else if (block.getType() == baseBlock) {
                return true;
            }

        }

        List<Block> foundBlocksList = new ArrayList<>(foundBlocks);
        Collections.shuffle(foundBlocksList);

        boolean valid = false;
        //Determining if all the blocks are valid in a row, up until a max recursion threshold
        for (Block block : foundBlocksList) {
            Logger.debug("block found: " + block.getType());
            valid = isValidSetup(block);
            Logger.debug("validnow? " + valid);
            boolean isBaseBlock = block.getType() == baseBlock;
            if (isBaseBlock) return true;
            if (valid) break;

            if (valid && !isBaseBlock) {
               valid = isValidSetup(block);
            }

        }

        return valid;
    }


}
