package net.zaharenko424.a_changed.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import org.jetbrains.annotations.NotNull;

public class ElectrocutedEffect extends UnRemovableEffect {

    public ElectrocutedEffect() {
        super(MobEffectCategory.HARMFUL, 0);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, "6be17ce2-d5b7-4895-a781-343b5a13c940", -.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "8bd384fd-017e-4551-a0be-3f49b07cf678", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.hurt(DamageSources.electricity(pLivingEntity.level(), null), .2f + (pLivingEntity.isInWaterOrRain() ? .2f : 0));
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration % 5 == 0;
    }
}