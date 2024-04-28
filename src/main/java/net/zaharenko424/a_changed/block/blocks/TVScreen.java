package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.HorizontalTwoBlockMultiBlock;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;

public class TVScreen extends HorizontalTwoBlockMultiBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(-1, 0, 0.875f, 1, 1, 0.9375f),
            Shapes.box(-0.9375f, 0.0625f, 0.9375f, 0.9375f, 0.9375f, 1));
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public TVScreen(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = state.getValue(FACING);
        int partId = state.getValue(PART);
        return CACHE.getShape(direction, partId, PARTS.get(partId).alignShape(SHAPE));
    }
}