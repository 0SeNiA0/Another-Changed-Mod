package net.zaharenko424.a_changed.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class FriendlyGrabEffect extends UnRemovableRegen{

    public FriendlyGrabEffect() {
        super(MobEffectCategory.BENEFICIAL, 1, 0);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if(entity instanceof ServerPlayer player)
            player.getFoodData().eat(amplifier + 1, 1.0F);
    }
}