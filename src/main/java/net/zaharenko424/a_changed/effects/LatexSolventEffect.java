package net.zaharenko424.a_changed.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.attachments.TransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import org.jetbrains.annotations.NotNull;

public class LatexSolventEffect extends UnRemovableEffect {

    public LatexSolventEffect() {
        super(MobEffectCategory.NEUTRAL, 15330485);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 25 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        TransfurHandler handler = TransfurHandler.of(entity);
        if(handler == null) return false;

        if(handler.isTransfurred()){
            entity.hurt(DamageSources.latexSolvent(entity.level(), null),2f);
        } else if(handler.getTransfurProgress() > 0) {
            handler.subTransfurProgress(1);
        }

        return true;
    }
}