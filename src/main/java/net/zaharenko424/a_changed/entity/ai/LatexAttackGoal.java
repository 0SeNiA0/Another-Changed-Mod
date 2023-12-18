package net.zaharenko424.a_changed.entity.ai;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.TransfurManager;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;

public class LatexAttackGoal extends MeleeAttackGoal {

    public LatexAttackGoal(AbstractLatexBeast p_25552_, double p_25553_, boolean p_25554_) {
        super(p_25552_, p_25553_, p_25554_);
    }

    @Override
    public boolean canUse() {
        if(mob.getTarget() instanceof Player player&&(TransfurManager.isTransfurred(player)||TransfurManager.isBeingTransfurred(player))&&mob.getLastHurtByMob()!=player) return false;
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if(mob.getTarget() instanceof Player player&&(TransfurManager.isTransfurred(player)||TransfurManager.isBeingTransfurred(player))&&mob.getLastHurtByMob()!=player) return false;
        return super.canContinueToUse();
    }
}