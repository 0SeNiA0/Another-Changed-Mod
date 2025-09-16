package net.zaharenko424.a_changed.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import org.jetbrains.annotations.NotNull;

public class StabilizedLatexSyringeItem extends LatexSyringeItem {

    @Override
    protected void transfur(ItemStack stack, LivingEntity entity) {
        TransfurType<?> transfurType = decodeTransfur(stack);
        if(transfurType != null) TransfurHandler.nonNullOf(entity).transfur(transfurType, TransfurContext.TRANSFUR);
    }

    @Override
    public ItemStack applyEffectsAsProjectile(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, @NotNull SyringeProjectile syringe, Entity shooter) {
        if(level.isClientSide) return ItemRegistry.SYRINGE_ITEM.toStack();

        if(DamageSources.checkTFTarget(entity)) {
            TransfurType<?> transfurType = decodeTransfur(stack);
            if(transfurType != null) TransfurHandler.nonNullOf(entity).addTransfurProgress(TransfurManager.TRANSFUR_TOLERANCE / 2, transfurType, TransfurContext.TRANSFUR);
        }

        return ItemRegistry.SYRINGE_ITEM.toStack();
    }

    public static @NotNull ItemStack encodeTransfur(@NotNull TransfurType<?> transfurType){
        ItemStack syringe = ItemRegistry.STABILIZED_LATEX_SYRINGE.toStack();
        syringe.set(ComponentRegistry.TRANSFUR_TYPE, transfurType.id);
        return syringe;
    }
}