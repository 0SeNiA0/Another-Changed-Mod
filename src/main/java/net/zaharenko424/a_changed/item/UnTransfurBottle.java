package net.zaharenko424.a_changed.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import org.jetbrains.annotations.NotNull;

public class UnTransfurBottle extends Item {

    public UnTransfurBottle() {
        super(new Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 32;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        return ItemUtils.startUsingInstantly(level, player, usedHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if(!(entity instanceof Player player)) return super.finishUsingItem(stack, level, entity);
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.UNTRANSFUR,600));
        if(player.getAbilities().instabuild) return stack;
        ItemStack result = new ItemStack(Items.GLASS_BOTTLE);
        if(stack.getCount() == 1){
            return result;
        }
        stack.shrink(1);
        if(player.getInventory().getFreeSlot() != -1){
            player.addItem(result);
        } else player.drop(result,true);
        return stack;
    }
}