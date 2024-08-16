package net.zaharenko424.a_changed.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LightTexture.class)
public abstract class MixinLightTexture {

    /**
     * Enables night vision for certain latexes.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z", ordinal = 0), method = "updateLightTexture")
    private boolean onUpdateLightTexture(boolean original){
        if(original) return true;
        Player player = Minecraft.getInstance().player;
        if(!TransfurManager.isTransfurred(player)) return false;
        return TransfurManager.hasCatAbility(player)
                || (TransfurManager.hasFishAbility(player) && player.isInWaterOrBubble());
    }
}