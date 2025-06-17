package net.zaharenko424.a_changed.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;

import javax.annotation.ParametersAreNonnullByDefault;
@ParametersAreNonnullByDefault
public class UnTransfurEffect extends UnRemovableEffect {

    public UnTransfurEffect() {
        super(MobEffectCategory.NEUTRAL, 13816530);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration == 1;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity instanceof ServerPlayer player)
            TransfurHandler.nonNullOf(player).unTransfur(TransfurContext.UNTRANSFUR);
        return true;
    }
}