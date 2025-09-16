package net.zaharenko424.a_changed.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DangerSign extends HorizontalDirectionalBlock {

    private static final VoxelShape SHAPE_NORTH = Shapes.or(Shapes.box(0.4375, 0.125, 0.9375, 0.5625, 0.875, 1),
            Shapes.box(0.5625, 0.125, 0.9375, 0.625, 0.8125, 1),
            Shapes.box(0.375, 0.125, 0.9375, 0.4375, 0.8125, 1),
            Shapes.box(0.3125, 0.125, 0.9375, 0.375, 0.6875, 1),
            Shapes.box(0.625, 0.125, 0.9375, 0.6875, 0.6875, 1),
            Shapes.box(0.6875, 0.125, 0.9375, 0.75, 0.5625, 1),
            Shapes.box(0.25, 0.125, 0.9375, 0.3125, 0.5625, 1),
            Shapes.box(0.75, 0.125, 0.9375, 0.8125, 0.4375, 1),
            Shapes.box(0.1875, 0.125, 0.9375, 0.25, 0.4375, 1),
            Shapes.box(0.8125, 0.125, 0.9375, 0.875, 0.3125, 1),
            Shapes.box(0.125, 0.125, 0.9375, 0.1875, 0.3125, 1));
    private static final VoxelShape SHAPE_EAST = Utils.rotateShape(Direction.EAST, SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = Utils.rotateShape(Direction.SOUTH, SHAPE_NORTH);
    private static final VoxelShape SHAPE_WEST = Utils.rotateShape(Direction.WEST, SHAPE_NORTH);

    public DangerSign(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(DangerSign::new);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)){
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return canSurvive(state, level, pos) ? super.updateShape(state, direction, neighborState, level, pos, neighborPos) : Blocks.AIR.defaultBlockState();
    }
}