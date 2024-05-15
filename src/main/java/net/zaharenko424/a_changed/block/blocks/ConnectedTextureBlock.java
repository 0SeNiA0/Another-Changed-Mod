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
import net.zaharenko424.a_changed.util.StateProperties;
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
    public static final BooleanProperty LOCKED_STATE = StateProperties.LOCKED_STATE;

    public ConnectedTextureBlock(Properties properties) {
        super(properties);
        BlockState defaultState = stateDefinition.any()
                .setValue(UP,false)
                .setValue(DOWN,false)
                .setValue(NORTH,false)
                .setValue(EAST,false)
                .setValue(SOUTH,false)
                .setValue(WEST,false);
        if(defaultState.hasProperty(LOCKED_STATE)) defaultState = defaultState.setValue(LOCKED_STATE, false);
        registerDefaultState(defaultState);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if(state.getValue(LOCKED_STATE)) return state;
        return state.setValue(propByDirection.get(direction), shouldConnectTo(neighborState, neighborPos, level, direction));
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        BlockState state = defaultBlockState();
        Level level = context.getLevel();
        for(Direction direction : Direction.values()){
            blockPos.setWithOffset(context.getClickedPos(), direction);
            if(shouldConnectTo(level.getBlockState(blockPos), blockPos, level, direction))
                state = state.setValue(propByDirection.get(direction),true);
        }
        return state;
    }

    protected boolean shouldConnectTo(@NotNull BlockState state, BlockPos pos, LevelAccessor level, Direction direction){
        return state.is(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, StateProperties.LOCKED_STATE);
    }
}