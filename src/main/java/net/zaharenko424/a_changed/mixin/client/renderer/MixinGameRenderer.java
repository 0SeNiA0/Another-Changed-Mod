package net.zaharenko424.a_changed.mixin.client.renderer;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(at = @At("HEAD"), method = "getNightVisionScale", cancellable = true)
    private static void onGetNightVisionScale(LivingEntity pLivingEntity, float pNanoTime, CallbackInfoReturnable<Float> cir){
        if(!(pLivingEntity instanceof Player player) || !TransfurManager.isTransfurred(player)) return;
        if(TransfurManager.hasCatAbility(player)
                || (TransfurManager.hasFishAbility(player) && player.isInWaterOrBubble()))
            cir.setReturnValue(1f);
    }
}