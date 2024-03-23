package net.zaharenko424.a_changed.block.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class ConnectedTextureBlock extends Block {

    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final ImmutableMap<Direction, BooleanProperty> propByDirection = ImmutableMap.of(
            Direction.UP, UP, Direction.DOWN, DOWN, Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST);

    public ConnectedTextureBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any()
                .setValue(UP,false)
                .setValue(DOWN,false)
                .setValue(NORTH,false)
                .setValue(EAST,false)
                .setValue(SOUTH,false)
                .setValue(WEST,false));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState p_60541_, @NotNull Direction p_60542_, @NotNull BlockState p_60543_, @NotNull LevelAccessor p_60544_, @NotNull BlockPos p_60545_, @NotNull BlockPos p_60546_) {
        return p_60541_.setValue(propByDirection.get(p_60542_), shouldConnectTo(p_60543_, p_60546_, p_60544_, p_60542_));
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_49820_) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        BlockState state = defaultBlockState();
        Level level = p_49820_.getLevel();
        for(Direction direction : Direction.values()){
            blockPos.setWithOffset(p_49820_.getClickedPos(), direction);
            if(shouldConnectTo(level.getBlockState(blockPos), blockPos, level, direction))
                state = state.setValue(propByDirection.get(direction),true);
        }
        return state;
    }

    protected boolean shouldConnectTo(@NotNull BlockState state, BlockPos pos, LevelAccessor level, Direction direction){
        return state.is(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_49915_) {
        p_49915_.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }
}