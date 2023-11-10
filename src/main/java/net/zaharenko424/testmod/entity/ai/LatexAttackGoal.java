package net.zaharenko424.testmod.entity.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.entity.AbstractLatexBeast;
import org.jetbrains.annotations.NotNull;

public class LatexAttackGoal extends MeleeAttackGoal {

    private final String transfurType;

    public LatexAttackGoal(AbstractLatexBeast p_25552_, double p_25553_, boolean p_25554_) {
        super(p_25552_, p_25553_, p_25554_);
        this.transfurType=p_25552_.transfurType.resourceLocation.toString();
    }

    @Override
    public boolean canUse() {
        if(mob.getTarget()==null) return false;
        if(mob.getTarget() instanceof Player player&&TransfurManager.isTransfurred(player)) return false;
        return super.canUse();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity p_25557_) {
        if (!this.canPerformAttack(p_25557_)) return;
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(p_25557_);
        if(p_25557_ instanceof Player player){
            TransfurManager.addTransfurProgress(player,5,transfurType);
        }
    }
}