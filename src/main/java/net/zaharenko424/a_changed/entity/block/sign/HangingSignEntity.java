package net.zaharenko424.a_changed.entity.block.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;

public class HangingSignEntity extends SignBlockEntity {

    public HangingSignEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.HANGING_SIGN_ENTITY.get(), pPos, pBlockState);
    }

    public int getTextLineHeight() {
        return 9;
    }

    public int getMaxTextLineWidth() {
        return 60;
    }
}