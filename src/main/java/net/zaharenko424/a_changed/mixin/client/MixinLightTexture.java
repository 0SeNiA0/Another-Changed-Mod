package net.zaharenko424.a_changed.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractLatexCat;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LightTexture.class)
public class MixinLightTexture {

    @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/client/player/LocalPlayer.hasEffect (Lnet/minecraft/world/effect/MobEffect;)Z"),
    method = "updateLightTexture")
    private boolean updateLightTextureProxy(@NotNull LocalPlayer instance, MobEffect mobEffect){
        return instance.hasEffect(mobEffect)
                || (mobEffect == MobEffects.NIGHT_VISION
                    && TransfurManager.isTransfurred(instance)
                    && TransfurManager.getTransfurType(instance) instanceof AbstractLatexCat);
    }
}