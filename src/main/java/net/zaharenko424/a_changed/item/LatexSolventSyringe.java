package net.zaharenko424.a_changed.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexSolventSyringe extends AbstractSyringe {

    public LatexSolventSyringe(@NotNull Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
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
    protected ItemStack applyUseEffects(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if(!level.isClientSide)
            entity.addEffect(new MobEffectInstance(MobEffectRegistry.LATEX_SOLVENT, 200));

        return ItemRegistry.SYRINGE_ITEM.toStack();
    }

    @Override
    public ItemStack applyEffectsAsProjectile(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, @NotNull SyringeProjectile syringe, @Nullable Entity shooter) {
        if(!level.isClientSide)
            entity.addEffect(new MobEffectInstance(MobEffectRegistry.LATEX_SOLVENT, 200));

        return ItemRegistry.SYRINGE_ITEM.toStack();
    }
}