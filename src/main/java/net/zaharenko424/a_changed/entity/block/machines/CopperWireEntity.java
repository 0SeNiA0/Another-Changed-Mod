package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;

public class CopperWireEntity extends AbstractProxyWire {

    public CopperWireEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.COPPER_WIRE_ENTITY.get(), pPos, pBlockState);
    }
}