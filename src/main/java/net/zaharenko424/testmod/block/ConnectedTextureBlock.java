package net.zaharenko424.testmod.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.zaharenko424.testmod.registry.BlockRegistry;
import org.jetbrains.annotations.NotNull;

public abstract class ConnectedTextureBlock extends Block {
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final ImmutableMap<Direction,BooleanProperty> propByDirection=ImmutableMap.of(
            Direction.UP,UP,Direction.DOWN,DOWN,Direction.NORTH,NORTH,Direction.EAST,EAST,Direction.SOUTH,SOUTH,Direction.WEST,WEST);

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
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_49820_) {
        BlockPos.MutableBlockPos blockPos=new BlockPos.MutableBlockPos();
        BlockState state=defaultBlockState();
        for(Direction direction: Direction.values()){
            blockPos.set(p_49820_.getClickedPos()).move(direction);
            if(p_49820_.getLevel().getBlockState(blockPos).is(BlockRegistry.CARPET_BLOCK)) state=state.setValue(propByDirection.get(direction),true);
        }
        return state;
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState p_60541_, @NotNull Direction p_60542_, @NotNull BlockState p_60543_, @NotNull LevelAccessor p_60544_, @NotNull BlockPos p_60545_, @NotNull BlockPos p_60546_) {
        return p_60541_.setValue(propByDirection.get(p_60542_),isSame(p_60543_));
    }

    protected abstract boolean isSame(BlockState state);

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_49915_) {
        p_49915_.add(UP,DOWN,NORTH,EAST,SOUTH,WEST);
    }
}