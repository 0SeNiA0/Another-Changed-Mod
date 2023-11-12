package net.zaharenko424.testmod.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.TransfurManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.zaharenko424.testmod.TransfurManager.TRANSFUR_TYPE_KEY;

public class SyringeItem extends AbstractSyringe {
    public SyringeItem(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(!TransfurManager.isTransfurred(pPlayer)) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        return super.use(pLevel,pPlayer,pUsedHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        Player player= (Player) pLivingEntity;
        if(player.getCommandSenderWorld().isClientSide) return pStack;
        ItemStack syringe=new ItemStack(TestMod.LATEX_SYRINGE_ITEM.get());
        CompoundTag tag=syringe.hasTag()?syringe.getTag():new CompoundTag();
        //TransfurManager.getTransfurTypeAsStr(player)   should never be null
        TransfurManager.modTag(tag).putString(TRANSFUR_TYPE_KEY, Objects.requireNonNull(TransfurManager.getTransfurType(player)).resourceLocation.toString());
        syringe.setTag(tag);
        return onUse(pStack,syringe,player);
    }
}