package net.zaharenko424.a_changed.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface Wrenchable {

    @NotNull
    InteractionResult useWrenchOn(BlockState state, BlockPos pos, ServerLevel level, UseOnContext context);
}