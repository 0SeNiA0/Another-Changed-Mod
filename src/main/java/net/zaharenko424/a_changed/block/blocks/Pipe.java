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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class Pipe extends Block {

    private static final VoxelShape SHAPE_N, SHAPE_NE, SHAPE_NS;
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final ImmutableMap<Direction, BooleanProperty> propByDirection = ImmutableMap.of(
            Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST);

    public Pipe(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(NORTH, false).setValue(EAST, false)
                .setValue(SOUTH, false).setValue(WEST, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean n = state.getValue(NORTH);
        boolean e = state.getValue(EAST);
        boolean s = state.getValue(SOUTH);
        boolean w = state.getValue(WEST);

        if(n && !e && !s && !w) return SHAPE_N;
        if(!n && e && !s && !w) return CACHE.getShape(Direction.EAST, 0, SHAPE_N);
        if(!n && !e && s && !w) return CACHE.getShape(Direction.SOUTH, 0, SHAPE_N);
        if(!n && !e && !s && w) return CACHE.getShape(Direction.WEST, 0, SHAPE_N);

        if(n && e && !s && !w) return SHAPE_NE;
        if(!n && e && s && !w) return CACHE.getShape(Direction.EAST, 1, SHAPE_NE);
        if(!n && !e && s) return CACHE.getShape(Direction.SOUTH, 1, SHAPE_NE);
        if(n && !e && !s) return CACHE.getShape(Direction.WEST, 1, SHAPE_NE);

        if(!n && e && !s) return CACHE.getShape(Direction.EAST, 10, SHAPE_NS);
        return SHAPE_NS;
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState p_60541_, @NotNull Direction p_60542_, @NotNull BlockState p_60543_, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos p_60546_) {
        BlockState state = getConnections(level, pos);
        return canSurvive(state, level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getConnections(context.getLevel(), context.getClickedPos());
    }

    public BlockState getConnections(@NotNull LevelAccessor level, @NotNull BlockPos pos) {
        BlockState state = defaultBlockState();
        int i = 0;
        List<Direction> exclude = new ArrayList<>();
        for(Direction direction : propByDirection.keySet()){
            if(!validConnection(pos.relative(direction), direction.getOpposite(), level)) continue;
            state = state.setValue(propByDirection.get(direction), true);
            exclude.add(direction);
            i++;
            if(i == 2) return state;
        }
        for(Direction direction : propByDirection.keySet()){
            if(exclude.contains(direction) || !validConnection2(pos.relative(direction), direction.getOpposite(), level)) continue;
            state = state.setValue(propByDirection.get(direction),true);
            i++;
            if(i == 2) return state;
        }
        return state;
    }

    public boolean validConnection(BlockPos pos, Direction connectionDir, LevelAccessor level){
        BlockState state = level.getBlockState(pos);
        if(!state.is(this)) return false;
        int i = 0;
        Direction direction;
        for(Map.Entry<Direction, BooleanProperty> entry : propByDirection.entrySet()){
            direction = entry.getKey();
            if(state.getValue(entry.getValue()) && direction != connectionDir && level.getBlockState(pos.relative(direction)).is(this)) i++;
            if(i == 2) return false;
        }
        return true;
    }

    public boolean validConnection2(BlockPos pos, Direction direction, LevelAccessor level){
        return level.getBlockState(pos).isFaceSturdy(level, pos, direction);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for(BooleanProperty prop : propByDirection.values()){
            if(state.getValue(prop)) return true;
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        if(rotation == Rotation.NONE) return state;
        BlockState newState = state;
        for(Direction direction : Direction.Plane.HORIZONTAL){
            newState = newState.setValue(propByDirection.get(rotation.rotate(direction)), state.getValue(propByDirection.get(direction)));
        }
        return newState;
    }

    static {
        SHAPE_N = Shapes.or(Shapes.box(0.374375, 0.374375, -0.000625, 0.625625, 0.625625, 0.500625),
                Shapes.box(0.4375, 0.6, 0, 0.5625, 0.6625, 0.5),
                Shapes.box(0.4375, 0.3375, 0, 0.5625, 0.4, 0.5),
                Shapes.box(0.3375, 0.4375, 0, 0.4, 0.5625, 0.5),
                Shapes.box(0.6, 0.4375, 0, 0.6625, 0.5625, 0.5));
        SHAPE_NE = Shapes.or(Shapes.box(0.436875, 0.374375, 0.374375, 1.000625, 0.625625, 0.625625),
                Shapes.box(0.5, 0.6, 0.4375, 1, 0.6625, 0.5625),
                Shapes.box(0.5, 0.3375, 0.4375, 1, 0.4, 0.5625),
                Shapes.box(0.625, 0.4375, 0.3375, 1, 0.5625, 0.4),
                Shapes.box(0.5, 0.4375, 0.6, 1, 0.5625, 0.6625),
                Shapes.box(0.6, 0.4375, 0, 0.6625, 0.5625, 0.375),
                Shapes.box(0.3375, 0.4375, 0, 0.4, 0.5625, 0.5),
                Shapes.box(0.4375, 0.3375, 0, 0.5625, 0.4, 0.5),
                Shapes.box(0.4375, 0.6, 0, 0.5625, 0.6625, 0.5),
                Shapes.box(0.374375, 0.374375, -0.000625, 0.625625, 0.625625, 0.563125),
                Shapes.box(0.39375, 0.40625, 0.54375, 0.45625, 0.59375, 0.60625));
        SHAPE_NS = Shapes.or(Shapes.box(0.374375, 0.374375, -0.000625, 0.625625, 0.625625, 1.000625),
                Shapes.box(0.4375, 0.6, 0, 0.5625, 0.6625, 1),
                Shapes.box(0.4375, 0.3375, 0, 0.5625, 0.4, 1),
                Shapes.box(0.6, 0.4375, 0, 0.6625, 0.5625, 1),
                Shapes.box(0.3375, 0.4375, 0, 0.4, 0.5625, 1));
    }
}