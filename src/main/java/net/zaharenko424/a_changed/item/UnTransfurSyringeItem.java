package net.zaharenko424.a_changed.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.attachments.TransfurHandler;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.LatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class UnTransfurSyringeItem extends AbstractSyringe {

    public UnTransfurSyringeItem(){
        this(new Properties().rarity(Rarity.RARE));
    }

    protected UnTransfurSyringeItem(@NotNull Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public int getContentsColor(ItemStack stack) {
        return Color.GREEN.getRGB();
    }

    @Override
    public int getSecondaryColor(ItemStack stack) {
        return 0;
    }

    @Override
    protected ItemStack applyUseEffects(@NotNull ItemStack item, @NotNull Level level, @NotNull LivingEntity entity){
        if(level.isClientSide) return ItemRegistry.SYRINGE_ITEM.toStack();

        if(TransfurManager.isTransfurred(entity)){
            if(entity instanceof LatexBeast){
                entity.hurt(DamageSources.untransfurKill(level, entity), Float.MAX_VALUE);
                return ItemRegistry.SYRINGE_ITEM.toStack();
            }

            entity.hurt(DamageSources.syringe(level, entity), .5f);
            untransfur(item, entity);
        } else {
            giveDebuffs(entity, 2);
            giveWither(entity, .5f, 1);
        }
        return ItemRegistry.SYRINGE_ITEM.toStack();
    }

    @Override
    public ItemStack applyEffectsAsProjectile(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, @NotNull SyringeProjectile syringe, Entity shooter) {
        if(level.isClientSide) return ItemRegistry.SYRINGE_ITEM.toStack();

        if(TransfurManager.isTransfurred(entity)){
            if(!entity.hasEffect(MobEffectRegistry.UNTRANSFUR_STACK)){
                entity.setInvulnerable(false);
                entity.hurt(DamageSources.syringe(level, syringe, shooter), .5f);
                entity.addEffect(new MobEffectInstance(MobEffectRegistry.UNTRANSFUR_STACK, 600));//stack for 30s
                return ItemRegistry.SYRINGE_ITEM.toStack();
            }

            if(entity instanceof LatexBeast){
                entity.hurt(DamageSources.untransfurKill(level, syringe, shooter), Float.MAX_VALUE);
            } else {
                entity.hurt(DamageSources.syringe(level, syringe, shooter), .5f);
                untransfur(stack, entity);
            }
        } else {
            giveDebuffs(entity, 2);
            giveWither(entity, .5f, 1);
        }
        return ItemRegistry.SYRINGE_ITEM.toStack();
    }

    protected void untransfur(@NotNull ItemStack item, @NotNull LivingEntity entity){
        TransfurHandler.nonNullOf(entity).unTransfur(TransfurContext.UNTRANSFUR);
        if(entity.getRandom().nextFloat() > .5) giveDebuffs(entity, 1);
    }

    protected void giveDebuffs(@NotNull LivingEntity entity, int durationMul){
        int duration = 60 * durationMul;
        entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration, 0, false, false));
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, 0, false, false));
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1, false, false));
    }

    protected void giveWither(@NotNull LivingEntity entity, float chance, float durationMul){
        if(entity.getRandom().nextFloat() > 1 - chance)
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, (int) (120 * durationMul), 1, false, false));
    }
}