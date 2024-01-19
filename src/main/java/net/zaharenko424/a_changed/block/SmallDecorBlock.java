package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

@SuppressWarnings("deprecation")
public abstract class SmallDecorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {

    protected SmallDecorBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction pDirection, @NotNull BlockState pNeighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos pNeighborPos) {
        return canSurvive(state, level, pos) ? state : level.getFluidState(pos).createLegacyBlock();
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext p_49820_) {
        BlockState state = defaultBlockState().setValue(FACING, p_49820_.getHorizontalDirection().getOpposite());
        if(p_49820_.getLevel().getFluidState(p_49820_.getClickedPos()).getType() == Fluids.WATER) state = state.setValue(WATERLOGGED, true);
        return state;
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult pHit) {
        if(level.isClientSide || !player.isCrouching() || !player.getItemInHand(hand).isEmpty()) return super.use(pState, level, pos, player, hand, pHit);
        level.removeBlock(pos, false);
        player.setItemInHand(hand, new ItemStack(pState.getBlock()));
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState p_60525_, @NotNull LevelReader p_60526_, @NotNull BlockPos p_60527_) {
        BlockPos pos = p_60527_.below();
        return p_60526_.getBlockState(pos).isFaceSturdy(p_60526_, pos, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(FACING, WATERLOGGED));
    }
}