package net.zaharenko424.a_changed.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.entity.ai.GolemTargetGoalFix;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolem.class)
public abstract class MixinIronGolem extends AbstractGolem {
    public MixinIronGolem(EntityType<? extends AbstractGolem> p_27508_, Level p_27509_) {
        super(p_27508_, p_27509_);
    }

    @Redirect(slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/world/entity/ai/goal/target/NearestAttackableTargetGoal.<init> (Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V")),
            at =  @At(value = "INVOKE", target = "net/minecraft/world/entity/ai/goal/GoalSelector.addGoal (ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 1),method = "registerGoals")
    private void registerGoalsProxy(GoalSelector instance, int pPriority, Goal pGoal){
        targetSelector.addGoal(pPriority, new GolemTargetGoalFix<>(this, LivingEntity.class, 5, false, false, entity -> {
            if(entity instanceof AbstractLatexBeast latex && latex.transfurType.isOrganic()) return false;
            if(entity instanceof Player player && TransfurManager.isTransfurred(player) && !TransfurManager.isOrganic(player)) return true;
            return entity instanceof Enemy && !(entity instanceof Creeper);
        }));
    }

    @Inject(at = @At("HEAD"), method = "doPush", cancellable = true)
    private void onDoPush(Entity pEntity, CallbackInfo ci){
        if((!(pEntity instanceof AbstractLatexBeast latex) || !latex.transfurType.isOrganic())
                && pEntity instanceof Enemy && !(pEntity instanceof Creeper)
                && getRandom().nextInt(20) == 0) setTarget((LivingEntity) pEntity);
        super.doPush(pEntity);
        ci.cancel();
    }
}