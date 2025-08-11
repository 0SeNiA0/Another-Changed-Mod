package net.zaharenko424.a_changed.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class DynamicClipContext extends ClipContext {

    protected final ShapeGetter block;
    protected final Predicate<FluidState> canPick;
    protected final CollisionContext context;

    public DynamicClipContext(Vec3 from, Vec3 to, ShapeGetter block, Predicate<FluidState> canPick, CollisionContext collisionContext) {
        super(from, to, Block.COLLIDER, Fluid.NONE, collisionContext);
        this.block = block;
        this.canPick = canPick;
        this.context = collisionContext;
    }

    @Override
    public @NotNull VoxelShape getBlockShape(@NotNull BlockState blockState, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return block.get(blockState, level, pos, context);
    }

    @Override
    public @NotNull VoxelShape getFluidShape(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return canPick.test(state) ? state.getShape(level, pos) : Shapes.empty();
    }
}
