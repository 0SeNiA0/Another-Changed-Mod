package net.zaharenko424.a_changed.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.door.Abstract3By3Door;
import net.zaharenko424.a_changed.entity.block.machine.BackupGeneratorEntity;
import net.zaharenko424.a_changed.util.StateProperties;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackupGenerator extends AbstractMultiBlock implements SimpleWaterloggedBlock, EntityBlock {

    public static final BooleanProperty ACTIVE = StateProperties.ACTIVE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE;
    protected static final VoxelShape SHAPE_ACTIVE;
    protected static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public BackupGenerator(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected IntegerProperty part() {
        return Abstract3By3Door.PART;
    }

    @Override
    protected ImmutableMap<Integer, Part> parts() {
        return Abstract3By3Door.PARTS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return state.getValue(part()) != 0 ? null : new BackupGeneratorEntity(pos, state);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        boolean active = state.getValue(ACTIVE);
        int partId = state.getValue(part());
        return CACHE.getShape(state.getValue(FACING), partId + (active ? 10 : 0),
                parts().get(partId).alignShape(active ? SHAPE_ACTIVE : SHAPE));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(level.isClientSide || hand != InteractionHand.MAIN_HAND) return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        BlockPos mainPos = getMainPos(state, pos);
        BlockState mainState = level.getBlockState(mainPos);
        setActive(mainState, mainPos, level, !mainState.getValue(ACTIVE));
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if(level.isClientSide) return super.useWithoutItem(state, level, pos, player, hitResult);
        BlockPos mainPos = getMainPos(state, pos);
        BlockState mainState = level.getBlockState(mainPos);
        setActive(mainState, mainPos, level, !mainState.getValue(ACTIVE));
        return InteractionResult.SUCCESS;
    }

    protected void setActive(@NotNull BlockState mainState, BlockPos mainPos, LevelAccessor level, boolean active){
        Direction direction = mainState.getValue(FACING);
        parts().forEach((id, part) -> {
            BlockPos pos = part.toSecondaryPos(mainPos, direction);
            level.setBlock(pos, level.getBlockState(pos).setValue(ACTIVE, active), 3);
        });
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if(state == null) return null;
        if(context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER) state = state.setValue(WATERLOGGED, true);
        return state;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        Direction direction = state.getValue(FACING);
        parts().forEach((id, part) -> {
            if(id == 0) return;
            BlockPos pos1 = part.toSecondaryPos(pos, direction);
            BlockState state1 = state.setValue(part(), id);
            if(level.getFluidState(pos1).getType() == Fluids.WATER) state1 = state1.setValue(WATERLOGGED, true);
            level.setBlockAndUpdate(pos1, state1);
        });
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(state, level, pos, pNewState, pMovedByPiston);
        level.invalidateCapabilities(pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ACTIVE).add(WATERLOGGED));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide || state.getValue(part()) != 0 ? null : (a, b, c, emitter) -> ((BackupGeneratorEntity)emitter).tick();
    }

    static {
        SHAPE = Shapes.or(
                Shapes.box(-2, 0, 0.125f, 1, 3, 1),
                Shapes.box(-2, 0.9062f, 0.0625f, 1, 1.2188f, 0.125f),
                Shapes.box(-2, 0.0938f, 0.0625f, 1, 0.4062f, 0.125f),
                Shapes.box(-0.6875f, 0.5f, 0.0938f, 0.5f, 0.8125f, 0.125f),
                Shapes.box(-1.5625f, 1.875f, 0.0625f, -1.375f, 2.375f, 0.125f),
                Shapes.box(0.375f, 1.875f, 0.0625f, 0.5625f, 2.375f, 0.125f),
                Shapes.box(-1.5625f, 2.375f, 0.0625f, 0.5625f, 2.5625f, 0.125f),
                Shapes.box(-1.5625f, 1.6875f, 0.0625f, 0.5625f, 1.875f, 0.125f),
                Shapes.box(-1.3125f, 1.2812f, 0.0625f, -1.125f, 1.5938f, 0.125f),
                Shapes.box(0.125f, 1.2812f, 0.0625f, 0.3125f, 1.5938f, 0.125f),
                Shapes.box(-1.3125f, 1.5938f, 0.0625f, 0.3125f, 1.6875f, 0.125f),
                Shapes.box(-1.3125f, 1.2188f, 0.0625f, 0.3125f, 1.2812f, 0.125f),
                Shapes.box(-0.125f, 1.2188f, 0, 0.125f, 1.6562f, 0.125f),
                Shapes.box(0.1875f, 1.875f, 0.0938f, 0.25f, 2.1875f, 0.125f));
        SHAPE_ACTIVE = Shapes.or(
                Shapes.box(-2, 0, 0.125f, 1, 3, 1),
                Shapes.box(-2, 0.9062f, 0.0625f, 1, 1.2188f, 0.125f),
                Shapes.box(-2, 0.0938f, 0.0625f, 1, 0.4062f, 0.125f),
                Shapes.box(-0.6875f, 0.5f, 0.0938f, 0.5f, 0.8125f, 0.125f),
                Shapes.box(-1.5625f, 1.875f, 0.0625f, -1.375f, 2.375f, 0.125f),
                Shapes.box(0.375f, 1.875f, 0.0625f, 0.5625f, 2.375f, 0.125f),
                Shapes.box(-1.5625f, 2.375f, 0.0625f, 0.5625f, 2.5625f, 0.125f),
                Shapes.box(-1.5625f, 1.6875f, 0.0625f, 0.5625f, 1.875f, 0.125f),
                Shapes.box(-1.3125f, 1.2812f, 0.0625f, -1.125f, 1.5938f, 0.125f),
                Shapes.box(0.125f, 1.2812f, 0.0625f, 0.3125f, 1.5938f, 0.125f),
                Shapes.box(-1.3125f, 1.5938f, 0.0625f, 0.3125f, 1.6875f, 0.125f),
                Shapes.box(-1.3125f, 1.2188f, 0.0625f, 0.3125f, 1.2812f, 0.125f),
                Shapes.box(-1.125f, 1.2188f, 0, -0.875f, 1.6562f, 0.125f),
                Shapes.box(-0.5312f, 1.875f, 0.0938f, -0.4688f, 2.1875f, 0.125f));
    }
}
