package net.zaharenko424.a_changed.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class GolemTargetGoalFix<E extends LivingEntity> extends NearestAttackableTargetGoal<E> {
    public GolemTargetGoalFix(Mob pMob, Class<E> pTargetType, int pRandomInterval, boolean pMustSee, boolean pMustReach, @Nullable Predicate<LivingEntity> pTargetPredicate) {
        super(pMob, pTargetType, pRandomInterval, pMustSee, pMustReach, pTargetPredicate);
    }

    @Override
    public boolean canContinueToUse() {
        if(target instanceof Player player && (!TransfurManager.isTransfurred(player) || TransfurManager.isOrganic(player))) {
            ((IronGolem)mob).stopBeingAngry();
            return false;
        }
        return super.canContinueToUse();
    }
}