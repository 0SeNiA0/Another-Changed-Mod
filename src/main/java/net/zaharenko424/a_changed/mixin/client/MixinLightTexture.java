package net.zaharenko424.a_changed.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractLatexCat;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractWaterLatex;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LightTexture.class)
public class MixinLightTexture {

    /**
     * Enables night vision for certain latexes.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z", ordinal = 0), method = "updateLightTexture")
    private boolean onUpdateLightTexture(boolean original){
        if(original) return true;
        Player player = Minecraft.getInstance().player;
        if(!TransfurManager.isTransfurred(player)) return false;
        TransfurType transfurType = TransfurManager.getTransfurType(player);
        return transfurType instanceof AbstractLatexCat
                || (transfurType instanceof AbstractWaterLatex && player.isInWaterOrBubble());
    }
}