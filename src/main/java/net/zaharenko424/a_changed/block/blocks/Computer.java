package net.zaharenko424.a_changed.block.blocks;


import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Computer extends HorizontalDirectionalBlock {

    private static final VoxelShape SHAPE_NORTH = Shapes.or(Shapes.box(0.0625, 0.1875, 0.6875, 0.9375, 0.8125, 0.75),
            Shapes.box(0.4375, 0.0625, 0.6875, 0.5625, 0.1875, 0.8125),
            Shapes.box(0.25, 0, 0.125, 0.9375, 0.0625, 0.5),
            Shapes.box(0.0625, 0, 0.1875, 0.1875, 0.0625, 0.375),
            Shapes.box(0.3125, 0, 0.625, 0.6875, 0.0625, 0.875),
            Shapes.box(0.4375, 0.1875, 0.75, 0.5625, 0.3125, 0.8125));
    private static final VoxelShape SHAPE_EAST = Utils.rotateShape(Direction.EAST, SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = Utils.rotateShape(Direction.SOUTH, SHAPE_NORTH);
    private static final VoxelShape SHAPE_WEST = Utils.rotateShape(Direction.WEST, SHAPE_NORTH);

    public Computer(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Computer::new);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch(p_60555_.getValue(FACING)){
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return defaultBlockState().setValue(FACING, p_49820_.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(FACING));
    }
}