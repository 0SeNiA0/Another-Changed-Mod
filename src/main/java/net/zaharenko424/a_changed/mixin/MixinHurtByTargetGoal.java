package net.zaharenko424.a_changed.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HurtByTargetGoal.class)
public abstract class MixinHurtByTargetGoal extends TargetGoal {

    @Unique
    boolean mod$wasTransfurred = false;

    public MixinHurtByTargetGoal(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
    }

    @Inject(at = @At("HEAD"), method = "start")
    private void onStart(CallbackInfo ci){
        if(mob.getLastHurtByMob() instanceof Player player) mod$wasTransfurred = TransfurManager.isTransfurred(player);
    }

    @Override
    public boolean canContinueToUse() {
        if(!mod$wasTransfurred && targetMob instanceof Player player && (TransfurManager.isTransfurred(player) || TransfurManager.isBeingTransfurred(player))) return false;
        return super.canContinueToUse();
    }
}