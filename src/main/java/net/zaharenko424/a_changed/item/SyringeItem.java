package net.zaharenko424.a_changed.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SyringeItem extends AbstractSyringe {
    public SyringeItem() {
        super(new Properties().stacksTo(16));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(!TransfurManager.isTransfurred(pPlayer)) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if(pPlayer.level().isClientSide || pUsedHand != InteractionHand.MAIN_HAND || !pPlayer.isCrouching()
                || pInteractionTarget instanceof AbstractLatexBeast) return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
        ItemHandlerHelper.giveItemToPlayer(pPlayer, BloodSyringe.encodeEntity(pInteractionTarget));
        if(!pPlayer.isCreative()) pStack.shrink(1);
        pInteractionTarget.hurt(pPlayer.damageSources().generic(), .1f);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        Player player = (Player) pLivingEntity;
        if(player.level().isClientSide) return pStack;
        return onUse(pStack, LatexSyringeItem.encodeTransfur(Objects.requireNonNull(TransfurManager.getTransfurType(player))), player);
    }
}