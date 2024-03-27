package net.zaharenko424.a_changed.block.blocks;

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
import net.zaharenko424.a_changed.block.HorizontalDirectionalBlock;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
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

    public DangerSign(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends net.minecraft.world.level.block.HorizontalDirectionalBlock> codec() {
        return simpleCodec(DangerSign::new);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch (p_60555_.getValue(FACING)){
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return canSurvive(p_60541_, p_60544_, p_60545_) ? super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_) : Blocks.AIR.defaultBlockState();
    }
}