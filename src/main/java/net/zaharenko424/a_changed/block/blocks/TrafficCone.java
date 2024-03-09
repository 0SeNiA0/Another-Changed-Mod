package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TrafficCone extends Block {

    private static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.0625, 0.8125),
            Shapes.box(0.3125, 0.0625, 0.3125, 0.6875, 0.5, 0.6875),
            Shapes.box(0.375, 0.5, 0.375, 0.625, 0.875, 0.625));

    public TrafficCone(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return !canSurvive(p_60541_, p_60544_, p_60545_) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        BlockPos pos = p_60527_.below();
        return p_60526_.getBlockState(pos).isFaceSturdy(p_60526_, pos, Direction.UP);
    }
}