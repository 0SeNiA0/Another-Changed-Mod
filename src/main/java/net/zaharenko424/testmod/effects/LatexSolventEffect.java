package net.zaharenko424.testmod.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.testmod.TransfurDamageSource;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.entity.AbstractLatexBeast;
import org.jetbrains.annotations.NotNull;

public class LatexSolventEffect extends MobEffect {
    public LatexSolventEffect() {
        super(MobEffectCategory.HARMFUL, 15330485);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295329_, int p_295167_) {
        int i = 25 >> p_295167_;
        if (i > 0) {
            return p_295329_ % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity p_19467_, int p_19468_) {
        if(p_19467_ instanceof AbstractLatexBeast||(p_19467_ instanceof Player player&& TransfurManager.isTransfurred(player))){
            p_19467_.hurt(TransfurDamageSource.latexSolvent(p_19467_),2f);
        }
    }
}
