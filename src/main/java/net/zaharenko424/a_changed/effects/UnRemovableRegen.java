package net.zaharenko424.a_changed.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class UnRemovableRegen extends UnRemovableEffect {

    public UnRemovableRegen(MobEffectCategory pCategory, float healAmount, int pColor) {
        super(pCategory, pColor);
        this.healAmount = healAmount;
    }

    private final float healAmount;

    @Override
    public void applyEffectTick(@NotNull LivingEntity p_295924_, int p_296417_) {
        super.applyEffectTick(p_295924_, p_296417_);
        if (p_295924_.getHealth() < p_295924_.getMaxHealth()) {
            p_295924_.heal(healAmount);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295946_, int p_295536_) {
        int i = 50 >> p_295536_;
        if (i > 0) {
            return p_295946_ % i == 0;
        } else {
            return true;
        }
    }
}