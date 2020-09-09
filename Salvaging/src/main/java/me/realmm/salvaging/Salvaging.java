package me.realmm.salvaging;

import me.realmm.salvaging.blocks.DiamondBlock;
import me.realmm.salvaging.blocks.GoldBlock;
import me.realmm.salvaging.blocks.IronBlock;
import me.realmm.salvaging.listeners.PlayerInteractListener;
import me.realmm.salvaging.utils.BlockUtil;
import net.jamesandrew.realmlib.RealmLib;
import net.jamesandrew.realmlib.register.Register;

import java.util.stream.Stream;

public class Salvaging extends RealmLib {

    @Override
    protected void onStart() {
        registerListeners();
        registerBlocks();
    }

    @Override
    protected void onEnd() {

    }

    private void registerListeners() {
        Stream.of(
                new PlayerInteractListener()
        ).forEach(l -> Register.listener(l, this));
    }

    private void registerBlocks() {
        Stream.of(
            new DiamondBlock(),
            new GoldBlock(),
            new IronBlock()
        ).forEach(BlockUtil::registerItemBlock);
    }
}
