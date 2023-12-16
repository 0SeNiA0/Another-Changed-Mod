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
    public int getUseDuration(@NotNull ItemStack p_41454_) {
        return 32;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack p_41452_) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        return ItemUtils.startUsingInstantly(p_41432_,p_41433_,p_41434_);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_41409_, @NotNull Level p_41410_, @NotNull LivingEntity p_41411_) {
        if(!(p_41411_ instanceof Player player)) return super.finishUsingItem(p_41409_,p_41410_,p_41411_);
        p_41411_.addEffect(new MobEffectInstance(MobEffectRegistry.UNTRANSFUR.get(),600));
        if(player.getAbilities().instabuild) return p_41409_;
        ItemStack result=new ItemStack(Items.GLASS_BOTTLE);
        if(p_41409_.getCount()==1){
            return result;
        }
        p_41409_.shrink(1);
        if(player.getInventory().getFreeSlot()!=-1){
            player.addItem(result);
        } else player.drop(result,true);
        return p_41409_;
    }
}