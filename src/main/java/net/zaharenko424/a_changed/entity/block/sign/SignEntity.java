package net.zaharenko424.a_changed.entity.block.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;

public class SignEntity extends SignBlockEntity {

    public SignEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.SIGN_ENTITY.get(), pPos, pBlockState);
    }
}