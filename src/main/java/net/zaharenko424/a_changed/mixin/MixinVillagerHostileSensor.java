package net.zaharenko424.a_changed.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public abstract class MixinVillagerHostileSensor {

    @Inject(at = @At("HEAD"), method = "isMatchingEntity", cancellable = true)
    private void onIsMatchingEntity(LivingEntity p_148344_, LivingEntity p_148345_, CallbackInfoReturnable<Boolean> cir){
        if(p_148345_ instanceof AbstractLatexBeast latex){
            cir.setReturnValue(!latex.transfurType.isOrganic() && p_148345_.distanceToSqr(p_148344_) <= 12);
            return;
        }
        if(p_148345_ instanceof Player player){
            cir.setReturnValue(TransfurManager.isTransfurred(player) && !TransfurManager.isOrganic(player) && p_148345_.distanceToSqr(p_148344_)<=12);
        }
    }
}