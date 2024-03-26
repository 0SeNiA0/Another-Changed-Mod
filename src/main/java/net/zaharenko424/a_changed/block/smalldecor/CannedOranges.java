package net.zaharenko424.a_changed.block.smalldecor;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.entity.block.CannedOrangesEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CannedOranges extends MetalCan implements EntityBlock {

    public CannedOranges(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CannedOrangesEntity(pos, state);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return super.getLightEmission(state, level, pos);
    }

    @Override
    public int getLightBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.getLightBlock(pState, pLevel, pPos);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult pHit) {
        if(level.isClientSide || hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if(state.getValue(OPEN)){
            if(!player.isCrouching() && level.getBlockEntity(pos) instanceof CannedOrangesEntity can && can.hasFoodLeft()) {
                can.consumeFood();
                player.getFoodData().eat(4, .5f);
                level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS);
                return InteractionResult.SUCCESS;
            }
            openCloseCan(state, pos, level);
            return InteractionResult.SUCCESS;
        }
        if(player.isCrouching()){
            if(!player.getItemInHand(hand).isEmpty()
                    || !(level.getBlockEntity(pos) instanceof CannedOrangesEntity can)) return InteractionResult.PASS;
            ItemHandlerHelper.giveItemToPlayer(player, can.getCan());
            level.removeBlockEntity(pos);
            level.removeBlock(pos, false);
            return InteractionResult.SUCCESS;
        }
        openCloseCan(state, pos, level);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        if(!(level.getBlockEntity(pos) instanceof CannedOrangesEntity can)) return;
        can.setFood(pStack.getMaxDamage() - pStack.getDamageValue());
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if(!newState.is(state.getBlock()) && level.getBlockEntity(pos) instanceof CannedOrangesEntity can){
            popResource(level, pos, can.getCan());
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}