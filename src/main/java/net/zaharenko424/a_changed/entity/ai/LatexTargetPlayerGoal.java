package net.zaharenko424.a_changed.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class LatexTargetPlayerGoal extends NearestAttackableTargetGoal<Player> {

    boolean wasTransfurred = false;

    public LatexTargetPlayerGoal(Mob pMob, boolean pMustSee) {
        super(pMob, Player.class, pMustSee);
    }

    public LatexTargetPlayerGoal(Mob pMob, boolean pMustSee, boolean pMustReach) {
        super(pMob, Player.class, pMustSee, pMustReach);
    }

    public LatexTargetPlayerGoal(Mob pMob, boolean pMustSee, Predicate<LivingEntity> pTargetPredicate) {
        super(pMob, Player.class, pMustSee, pTargetPredicate);
    }

    public LatexTargetPlayerGoal(Mob pMob, int pRandomInterval, boolean pMustSee, boolean pMustReach, @Nullable Predicate<LivingEntity> pTargetPredicate) {
        super(pMob, Player.class, pRandomInterval, pMustSee, pMustReach, pTargetPredicate);
    }

    @Override
    public void start() {
        wasTransfurred = TransfurManager.isTransfurred((Player) target);
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        if(!wasTransfurred && (TransfurManager.isTransfurred((Player) target) || TransfurManager.isBeingTransfurred(target))) return false;
        return super.canContinueToUse();
    }
}