package net.zaharenko424.a_changed.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StabilizedLatexSyringeItem extends LatexSyringeItem {

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        Player player = (Player) pLivingEntity;
        if(!pLevel.isClientSide)
            TransfurHandler.nonNullOf(player).transfur(TransfurManager.getTransfurType(Objects.requireNonNull(decodeTransfur(pStack))), TransfurContext.TRANSFUR_TF);
        return onUse(pStack, new ItemStack(ItemRegistry.SYRINGE_ITEM.get()), player);
    }

    public static @NotNull ItemStack encodeTransfur(@NotNull TransfurType transfurType){
        ItemStack syringe = ItemRegistry.STABILIZED_LATEX_SYRINGE.toStack();
        syringe.set(ComponentRegistry.TRANSFUR_TYPE, transfurType.id);
        return syringe;
    }
}