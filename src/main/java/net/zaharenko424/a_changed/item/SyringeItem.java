package net.zaharenko424.a_changed.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SyringeItem extends AbstractSyringe {

    public SyringeItem() {
        super(new Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public int getContentsColor(ItemStack stack) {
        return 0;
    }

    @Override
    public int getSecondaryColor(ItemStack stack) {
        return 0;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player player, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        if(player.level().isClientSide || hand != InteractionHand.MAIN_HAND || !player.isCrouching()
                || TransfurManager.isTransfurred(target) || target instanceof AbstractFish) return super.interactLivingEntity(pStack, player, target, hand);

        target.hurt(DamageSources.syringe(player.level(), player), 2);
        onUse(pStack, applyUseEffects(pStack, player.level(), target), player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if(!level.isClientSide) entity.hurt(DamageSources.syringe(level, entity), 2);
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    protected ItemStack applyUseEffects(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        return TransfurManager.isTransfurred(entity) ? LatexSyringeItem.encodeTransfur(Objects.requireNonNull(TransfurManager.getTransfurType(entity)))
                : BloodSyringe.encodeEntity(entity);
    }

    @Override
    public ItemStack applyEffectsAsProjectile(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, @NotNull SyringeProjectile syringe, Entity shooter) {
        if(!level.isClientSide) entity.hurt(DamageSources.syringe(level, syringe, shooter), .5f);
        return stack;
    }
}