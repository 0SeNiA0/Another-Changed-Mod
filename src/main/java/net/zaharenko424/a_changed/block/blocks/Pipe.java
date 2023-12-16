package net.zaharenko424.a_changed.block.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class Pipe extends Block {

    public static final IntegerProperty NORTH = IntegerProperty.create("north",0,2);
    public static final IntegerProperty EAST = IntegerProperty.create("east",0,2);
    public static final IntegerProperty SOUTH = IntegerProperty.create("south",0,2);
    public static final IntegerProperty WEST = IntegerProperty.create("west",0,2);
    public static final ImmutableMap<Direction,IntegerProperty> propByDirection=ImmutableMap.of(
            Direction.NORTH,NORTH,Direction.EAST,EAST,Direction.SOUTH,SOUTH,Direction.WEST,WEST);

    public Pipe(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return super.getShape(p_60555_, p_60556_, p_60557_, p_60558_);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState p_60541_, @NotNull Direction p_60542_, @NotNull BlockState p_60543_, @NotNull LevelAccessor p_60544_, @NotNull BlockPos p_60545_, @NotNull BlockPos p_60546_) {
        BlockState newState=getConnections(p_60541_,p_60544_,p_60545_);
        return canSurvive(newState,p_60544_,p_60545_)?newState: Blocks.AIR.defaultBlockState();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return getConnections(defaultBlockState(),p_49820_.getLevel(),p_49820_.getClickedPos());
    }

    public BlockState getConnections(@NotNull BlockState state, @NotNull LevelAccessor level, @NotNull BlockPos pos) {
        int i=0;
        List<Direction> exclude=new ArrayList<>();
        for(Direction direction:propByDirection.keySet()){
            if(i==2) return state;
            if(validConnection(level.getBlockState(pos.relative(direction)))) {
                state = state.setValue(propByDirection.get(direction), 1);
                exclude.add(direction);
                i++;
            } else state = state.setValue(propByDirection.get(direction), 0);
        }
        for(Direction direction:propByDirection.keySet()){
            if(exclude.contains(direction)) continue;
            if(i==2) return state;
            if(validConnection2(pos.relative(direction),direction.getOpposite(),level)){
                state=state.setValue(propByDirection.get(direction),2);
                i++;
            } else state = state.setValue(propByDirection.get(direction), 0);
        }
        return state;
    }

    public boolean validConnection(BlockState state) {
        return state.is(this);
    }

    public boolean validConnection2(BlockPos pos, Direction direction, LevelAccessor level){
        return level.getBlockState(pos).isFaceSturdy(level,pos,direction);
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        int i=0;
        for(IntegerProperty prop:propByDirection.values()){
            if(p_60525_.getValue(prop)!=0) i++;
        }
        return i>0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(NORTH,EAST,SOUTH,WEST);
    }
}