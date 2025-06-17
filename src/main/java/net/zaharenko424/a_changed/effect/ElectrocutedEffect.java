package net.zaharenko424.a_changed.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import org.jetbrains.annotations.NotNull;

public class ElectrocutedEffect extends UnRemovableEffect {

    public ElectrocutedEffect() {
        super(MobEffectCategory.HARMFUL, 0);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, AChanged.resourceLoc("6be17ce2-d5b7-4895-a781-343b5a13c940"), -.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, AChanged.resourceLoc("8bd384fd-017e-4551-a0be-3f49b07cf678"), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        entity.hurt(DamageSources.electricity(entity.level(), null), .2f + (entity.isInWaterOrRain() ? .2f : 0));
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }
}