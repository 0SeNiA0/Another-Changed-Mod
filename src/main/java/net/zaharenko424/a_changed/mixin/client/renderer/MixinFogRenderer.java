package net.zaharenko424.a_changed.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.util.AbilityUtils;
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
        if(!(camera.getEntity() instanceof LivingEntity entity)) return false;

        return AbilityUtils.hasCatAbility(entity) || (AbilityUtils.hasFishAbility(entity) && entity.isInWaterOrBubble());
    }
}