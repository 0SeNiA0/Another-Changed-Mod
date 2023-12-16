package net.zaharenko424.a_changed.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSyringe extends Item {
    public AbstractSyringe(@NotNull Properties pProperties) {
        super(pProperties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        return ItemUtils.startUsingInstantly(pLevel,pPlayer,pUsedHand);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 32;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.DRINK;
    }

    protected ItemStack onUse(@NotNull ItemStack inUse, @NotNull ItemStack result, @NotNull Player player){
        if(!player.getAbilities().instabuild) {
            if (inUse.getCount() == 1) {
                return result;
            }
            inUse.shrink(1);
        }
        if(player.getInventory().getFreeSlot()!=-1){
            player.addItem(result);
        } else player.drop(result,true);
        return inUse;
    }
}