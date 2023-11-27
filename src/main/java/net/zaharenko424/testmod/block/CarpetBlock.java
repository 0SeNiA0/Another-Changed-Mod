package net.zaharenko424.testmod.block;

import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.testmod.registry.BlockRegistry;
import org.jetbrains.annotations.NotNull;

public class CarpetBlock extends ConnectedTextureBlock{
    public CarpetBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected boolean isSame(@NotNull BlockState state) {
        return state.is(BlockRegistry.CARPET_BLOCK);
    }
}