package net.zaharenko424.a_changed.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public abstract class MixinFogRenderer {

    /**
     * Enables night vision for certain latexes.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z", ordinal = 0),
            method = "setupColor")
    private static boolean onSetupColor(boolean original, Camera camera){
        if(original) return true;
        Entity entity = camera.getEntity();

        if(entity instanceof AbstractLatexBeast latex
                && (TransfurManager.hasCatAbility(latex) || (TransfurManager.hasFishAbility(latex) && latex.isInWaterOrBubble()))) return true;

        if(!(entity instanceof Player player) || !TransfurManager.isTransfurred(player)) return false;
        return TransfurManager.hasCatAbility(player)
                || (TransfurManager.hasFishAbility(player) && player.isInWaterOrBubble());
    }
}