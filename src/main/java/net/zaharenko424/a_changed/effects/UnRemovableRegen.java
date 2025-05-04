package net.zaharenko424.a_changed.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class UnRemovableRegen extends UnRemovableEffect {

    public UnRemovableRegen(MobEffectCategory category, float healAmount, int color) {
        super(category, color);
        this.healAmount = healAmount;
    }

    private final float healAmount;

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(healAmount);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 50 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}