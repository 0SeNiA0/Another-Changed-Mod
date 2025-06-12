package net.zaharenko424.a_changed.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdrenalineSyringe extends AbstractSyringe {

    public AdrenalineSyringe(){
        this(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public AdrenalineSyringe(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    public int getContentsColor(ItemStack stack) {
        return 168430090;
    }

    @Override
    public int getSecondaryColor(ItemStack stack) {
        return 0;
    }

    @Override
    protected ItemStack applyUseEffects(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if(level.isClientSide()) return ItemRegistry.SYRINGE_ITEM.toStack();

        entity.hurt(DamageSources.syringe(level, entity), .5f);
        apply(entity);
        return ItemRegistry.SYRINGE_ITEM.toStack();
    }

    @Override
    public ItemStack applyEffectsAsProjectile(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, @NotNull SyringeProjectile projectile, @Nullable Entity shooter) {
        if(level.isClientSide()) return ItemRegistry.SYRINGE_ITEM.toStack();

        entity.hurt(DamageSources.syringe(level, projectile, shooter), .5f);
        apply(entity);
        return ItemRegistry.SYRINGE_ITEM.toStack();
    }

    protected void apply(@NotNull LivingEntity entity){
        MobEffectInstance effect = entity.getEffect(MobEffectRegistry.ADRENALINE);

        if(effect == null){
            entity.addEffect(new MobEffectInstance(MobEffectRegistry.ADRENALINE, 1800));
            return;
        }

        if(effect.getAmplifier() <= 0){
            entity.addEffect(new MobEffectInstance(MobEffectRegistry.ADRENALINE, 1800, 1));
            return;
        }

        entity.addEffect(new MobEffectInstance(MobEffects.HARM, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 300, effect.getAmplifier()));
    }
}
