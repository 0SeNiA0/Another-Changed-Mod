package net.zaharenko424.a_changed.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.util.StateProperties;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Whiteboard extends AbstractMultiBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final ImmutableMap<Integer, Part> PARTS;
    protected static final VoxelShape SHAPE;
    protected static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public Whiteboard(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected IntegerProperty part() {
        return StateProperties.PART6;
    }

    @Override
    protected ImmutableMap<Integer, Part> parts() {
        return PARTS;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        int partId = state.getValue(part());
        return CACHE.getShape(state.getValue(FACING), partId, parts().get(partId).alignShape(SHAPE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if(state == null) return null;

        Direction direction = context.getClickedFace();
        if(context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER) state = state.setValue(WATERLOGGED, true);
        if(direction.getAxis() == Direction.Axis.Y) return state;
        return state.setValue(FACING, direction);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        Direction direction = state.getValue(FACING);
        parts().forEach((id, part) -> {
            if(id == 0) return;
            BlockPos pos1 = part.toSecondaryPos(pos, direction);
            BlockState state1 = state.setValue(part(), id);
            state1 = state1.setValue(WATERLOGGED, level.getFluidState(pos1).getType() == Fluids.WATER);
            level.setBlockAndUpdate(pos1, state1);
        });
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    }

    static {
        PARTS = ImmutableMap.<Integer, Part>builder()
                .put(0, new Part(0, 0, 0))
                .put(1, new Part(0, 1, 0))
                .put(2, new Part(-1, 1, 0))
                .put(3, new Part(-1, 0, 0))
                .put(4, new Part(-2, 0, 0))
                .put(5, new Part(-2, 1, 0)).build();

        SHAPE = Shapes.or(
                Shapes.box(-1.875f, 0.125f, 0.875f, 0.875f, 1.875f, 1),
                Shapes.box(0.875f, 0.125f, 0.8125f, 1, 1.875f, 1),
                Shapes.box(-2, 0.125f, 0.8125f, -1.875f, 1.875f, 1),
                Shapes.box(-2, 1.875f, 0.8125f, 1, 2, 1),
                Shapes.box(-2, 0, 0.75f, 1, 0.125f, 1),
                Shapes.box(0.5f, 0.125f, 0.7812f, 0.75f, 0.1875f, 0.8438f),
                Shapes.box(-1.4375f, 0.3125f, 0.8438f, -1.1875f, 0.6875f, 0.875f));
    }
}
