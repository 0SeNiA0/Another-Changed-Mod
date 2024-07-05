package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;

public class ConnectedTextureBlockImpl extends ConnectedTextureBlock {

    public static final BooleanProperty LOCKED_STATE = StateProperties.LOCKED_STATE;

    public ConnectedTextureBlockImpl(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(LOCKED_STATE, false));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if(state.getValue(LOCKED_STATE)) return state;
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected boolean shouldConnectTo(@NotNull BlockState state, BlockPos pos, LevelAccessor level, Direction direction) {
        return super.shouldConnectTo(state, pos, level, direction)
                && (state.getValue(LOCKED_STATE) ? state.getValue(propByDirection.get(direction.getOpposite())) : true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, StateProperties.LOCKED_STATE);
    }
}