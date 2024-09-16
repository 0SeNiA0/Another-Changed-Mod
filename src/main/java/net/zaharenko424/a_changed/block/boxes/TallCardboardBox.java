package net.zaharenko424.a_changed.block.boxes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.zaharenko424.a_changed.block.ISeatBlock;
import net.zaharenko424.a_changed.entity.SeatEntity;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class TallCardboardBox extends TallBox implements ISeatBlock<SeatEntity>, Fallable {
    public TallCardboardBox(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void onPlace(@NotNull BlockState p_60566_, @NotNull Level p_60567_, @NotNull BlockPos p_60568_, @NotNull BlockState p_60569_, boolean p_60570_) {
        super.onPlace(p_60566_, p_60567_, p_60568_, p_60569_, p_60570_);
        if(p_60567_.isClientSide) return;
        if(p_60566_.getValue(PART) == 0) p_60567_.addFreshEntity(new SeatEntity(p_60567_, p_60568_, false));
    }

    public boolean use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos p_60505_, @NotNull Player player) {
        if(level.isClientSide) return false;

        if(!player.isCrouching()){
            BlockPos pos = state.getValue(PART) == 0 ? p_60505_ : p_60505_.below();
            return sit(level, pos, SHAPE_0.bounds().move(pos), player, false);
        }

        BlockPos mainPos = getMainPos(state, p_60505_);
        BlockState mainState = level.getBlockState(mainPos);
        BlockPos pos = mainPos.relative(player.getDirection());
        if(level.getBlockState(pos).canBeReplaced() && level.getBlockState(pos.above()).canBeReplaced()){
            level.setBlockAndUpdate(mainPos, getFluidState(mainState).createLegacyBlock());
            level.playSound(null, mainPos, SoundRegistry.PUSH.get(), SoundSource.BLOCKS);
            BlockState below = level.getBlockState(pos.below());
            if(below.isAir() || below.canBeReplaced()) {
                FallingBlockEntity.fall(level, pos, mainState);
                return true;
            }
            level.setBlockAndUpdate(pos, mainState.setValue(WATERLOGGED, level.getFluidState(pos).isSourceOfType(Fluids.WATER)));
            setPlacedBy(level, pos, state, null, ItemStack.EMPTY);
            return true;
        }
        return false;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        return use(state, level, pos, player) ? InteractionResult.SUCCESS_NO_ITEM_USED : super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        return use(state, level, pos, player) ? ItemInteractionResult.SUCCESS : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void onLand(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockState pReplaceableState, @NotNull FallingBlockEntity pFallingBlock) {
        setPlacedBy(level, pos, state, null, ItemStack.EMPTY);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState p_60525_, @NotNull LevelReader p_60526_, @NotNull BlockPos p_60527_) {
        BlockPos pos = p_60527_.below();
        BlockState state = p_60526_.getBlockState(pos);
        return p_60525_.getValue(PART) == 0 ? state.isFaceSturdy(p_60526_, pos, Direction.UP) : state.is(this);
    }
}