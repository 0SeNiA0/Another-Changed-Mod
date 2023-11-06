package net.zaharenko424.testmod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.TransfurManager;
import org.jetbrains.annotations.NotNull;

public class LatexSyringeItem extends AbstractSyringe{
    public LatexSyringeItem(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(TransfurManager.isTransfurred(pPlayer)) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        ((Player)pLivingEntity).displayClientMessage(Component.literal("Imagine, that you are transfurred."),true);
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }
}