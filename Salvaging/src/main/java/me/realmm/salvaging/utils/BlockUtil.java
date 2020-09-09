package me.realmm.salvaging.utils;

import net.jamesandrew.commons.logging.Logger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.realmm.salvaging.blocks.ItemBlock;

import java.util.*;

public final class BlockUtil {

    private BlockUtil(){}

    private static final Material baseBlock = Material.FURNACE;
    private static final Set<ItemBlock> itemBlocks = new HashSet<>();

    public static boolean isValidType(Material material) {
        return itemBlocks.stream().anyMatch(b -> b.getBlockType() == material);
    }

    public static void registerItemBlock(ItemBlock block) {
        if (itemBlocks.stream().anyMatch(b -> b.getBlockType() == block.getBlockType() || b.getDropType() == block.getDropType())) 
            throw new IllegalArgumentException("Unable to register item block, block type or drop type already registered");
        itemBlocks.add(block);
    }

    public static ItemBlock asItemBlock(Material material) {
        return itemBlocks.stream().filter(b -> b.getBlockType() == material).findFirst().orElse(null);
    }

    public static boolean isItemBlock(Material material) {
        return asItemBlock(material) != null;
    } 

    //Determine if the block is in a valid setup
    public static boolean isValidSetup(Block b) {
        BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

        List<BlockFace> listBlockFaces = Arrays.asList(blockFaces);
        Collections.shuffle(listBlockFaces);

        Set<Block> foundBlocks = new HashSet<>();
        for (BlockFace bf : listBlockFaces) {
            Block block = b.getRelative(bf);
            if (isValidType(block.getType())) {
                foundBlocks.add(block);
            } else if (block.getType() == baseBlock) {
                return true;
            }

        }

        List<Block> foundBlocksList = new ArrayList<>(foundBlocks);
        Collections.shuffle(foundBlocksList);

        boolean valid = false;
        for (Block block : foundBlocksList) {
            valid = isValidSetup(block);
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
