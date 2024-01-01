package net.zaharenko424.a_changed.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

import java.lang.ref.WeakReference;

public class LatexAttackGoal extends MeleeAttackGoal {

    private WeakReference<LivingEntity> lastAttacker = new WeakReference<>(null);

    public LatexAttackGoal(AbstractLatexBeast p_25552_, double p_25553_, boolean p_25554_) {
        super(p_25552_, p_25553_, p_25554_);
    }

    @Override
    public boolean canUse() {
        if(mob.getLastHurtByMob()!=null&&mob.getLastHurtByMob()!= lastAttacker.get()) lastAttacker=new WeakReference<>(mob.getLastHurtByMob());
        if(mob.getTarget() instanceof Player player&&(TransfurManager.isTransfurred(player)||TransfurManager.isBeingTransfurred(player))&&lastAttacker.get()!=player) return false;
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if(mob.getLastHurtByMob()!=null&&mob.getLastHurtByMob()!= lastAttacker.get()) lastAttacker=new WeakReference<>(mob.getLastHurtByMob());
        if(mob.getTarget() instanceof Player player&&(TransfurManager.isTransfurred(player)||TransfurManager.isBeingTransfurred(player))&&lastAttacker.get()!=player) return false;
        return super.canContinueToUse();
    }
}