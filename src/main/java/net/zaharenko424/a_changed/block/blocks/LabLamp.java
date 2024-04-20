package net.zaharenko424.a_changed.block.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LabLamp extends RedstoneLampBlock {

    public static final MapCodec<RedstoneLampBlock> CODEC = simpleCodec(LabLamp::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    private static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(0, 0, 0.9375f, 1, 1, 1),
            Shapes.box(0.0625f, 0.0625f, 0.875f, 0.9375f, 0.9375f, 0.9375f));
    private static final VoxelShape SHAPE_UP = Shapes.or(
            Shapes.box(0, 0, 0, 1, 0.0625f, 1),
            Shapes.box(0.0625f, 0.0625f, 0.0625f, 0.9375f, 0.125f, 0.9375f));
    private static final VoxelShape SHAPE_DOWN = Shapes.or(
            Shapes.box(0, 0.9375f, 0, 1, 1, 1),
            Shapes.box(0.0625f, 0.875f, 0.0625f, 0.9375f, 0.9375f, 0.9375f));
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public LabLamp(Properties p_55657_) {
        super(p_55657_);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @Override
    public @NotNull MapCodec<RedstoneLampBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        if(direction.getAxis() == Direction.Axis.Y)
            return CACHE.getShape(Direction.NORTH, direction.ordinal(), ()-> direction == Direction.UP ? SHAPE_UP : SHAPE_DOWN);
        return CACHE.getShape(direction, 2, ()-> SHAPE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(FACING, pContext.getClickedFace());
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING));
    }
}