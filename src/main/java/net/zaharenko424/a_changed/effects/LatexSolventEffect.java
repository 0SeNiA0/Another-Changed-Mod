package net.zaharenko424.a_changed.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

public class LatexSolventEffect extends UnRemovableEffect {

    public LatexSolventEffect() {
        super(MobEffectCategory.HARMFUL, 15330485);
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
        if(TransfurManager.isTransfurred(entity)){
            entity.hurt(DamageSources.latexSolvent(entity.level(), null),2f);
            return true;
        }
        return false;
    }
}