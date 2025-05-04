package net.zaharenko424.a_changed.block.box;

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

    public TallCardboardBox(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(level.isClientSide) return;
        if(state.getValue(PART) == 0) level.addFreshEntity(new SeatEntity(level, pos, false));
    }

    public boolean use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        if(level.isClientSide) return false;

        if(!player.isCrouching()){
            BlockPos mainPos = state.getValue(PART) == 0 ? pos : pos.below();
            return sit(level, mainPos, SHAPE_0.bounds().move(mainPos), player, false);
        }

        BlockPos mainPos = getMainPos(state, pos);
        BlockState mainState = level.getBlockState(mainPos);
        BlockPos moveTo = mainPos.relative(player.getDirection());
        if(level.getBlockState(moveTo).canBeReplaced() && level.getBlockState(moveTo.above()).canBeReplaced()){
            level.setBlockAndUpdate(mainPos, getFluidState(mainState).createLegacyBlock());
            level.playSound(null, mainPos, SoundRegistry.PUSH.get(), SoundSource.BLOCKS);
            BlockState below = level.getBlockState(moveTo.below());
            if(below.isAir() || below.canBeReplaced()) {
                FallingBlockEntity.fall(level, moveTo, mainState);
                return true;
            }
            level.setBlockAndUpdate(moveTo, mainState.setValue(WATERLOGGED, level.getFluidState(moveTo).isSourceOfType(Fluids.WATER)));
            setPlacedBy(level, moveTo, state, null, ItemStack.EMPTY);
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
    public void onLand(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockState replaceableState, @NotNull FallingBlockEntity fallingBlock) {
        setPlacedBy(level, pos, state, null, ItemStack.EMPTY);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        BlockPos below = pos.below();
        BlockState stateBelow = level.getBlockState(below);
        return state.getValue(PART) == 0 ? stateBelow.isFaceSturdy(level, below, Direction.UP) : stateBelow.is(this);
    }
}