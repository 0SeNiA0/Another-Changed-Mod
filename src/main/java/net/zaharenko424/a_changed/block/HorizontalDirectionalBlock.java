package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class HorizontalDirectionalBlock extends net.minecraft.world.level.block.HorizontalDirectionalBlock {

    protected HorizontalDirectionalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockPos pos1 = pos.relative(direction);
        return level.getBlockState(pos1).isFaceSturdy(level, pos1, direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        if(direction.getAxis() == Direction.Axis.Y) direction = context.getHorizontalDirection().getOpposite();
        return defaultBlockState().setValue(FACING, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }
}