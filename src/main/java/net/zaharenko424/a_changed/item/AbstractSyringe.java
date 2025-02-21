package net.zaharenko424.a_changed.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSyringe extends Item {

    public AbstractSyringe(@NotNull Properties pProperties) {
        super(pProperties);
    }

    public abstract int getContentsColor(ItemStack stack);

    public abstract int getSecondaryColor(ItemStack stack);

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack, @NotNull LivingEntity entity) {
        return 32;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        return onUse(stack, applyUseEffects(stack, level, entity), entity);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        if(context.getHand() != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        Player player = context.getPlayer();
        if(player == null) return InteractionResult.PASS;
        Level level = context.getLevel();
        if(level.isClientSide || !context.isSecondaryUseActive()) return super.useOn(context);

        ItemStack stack = context.getItemInHand();
        SyringeProjectile syringe = new SyringeProjectile(level, player, stack, null);
        syringe.place(context.getClickLocation());
        level.addFreshEntity(syringe);
        syringe.setYRot(-player.getYRot() + 45);
        player.setItemInHand(InteractionHand.MAIN_HAND, onUse(stack, ItemStack.EMPTY, player));
        return InteractionResult.SUCCESS;
    }

    protected abstract ItemStack applyUseEffects(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity);

    public abstract ItemStack applyEffectsAsProjectile(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, @NotNull SyringeProjectile projectile, @Nullable Entity shooter);

    protected ItemStack onUse(@NotNull ItemStack inUse, @NotNull ItemStack result, @NotNull LivingEntity entity){
        if(!(entity instanceof Player player) || !player.isCreative()) {
            if (inUse.getCount() == 1) {
                return result;
            }
            inUse.shrink(1);
            if(result.isEmpty()) return inUse;
        }

        if(entity instanceof Player player) {
            ItemHandlerHelper.giveItemToPlayer(player, result);
        } else Block.popResource(entity.level(), entity.blockPosition(), result);
        return inUse;
    }
}