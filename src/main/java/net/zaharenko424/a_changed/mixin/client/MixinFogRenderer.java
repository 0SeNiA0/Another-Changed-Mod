package net.zaharenko424.a_changed.mixin.client;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractLatexCat;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractWaterLatex;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class MixinFogRenderer {

    @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.hasEffect (Lnet/minecraft/world/effect/MobEffect;)Z"),
    method = "setupColor")
    private static boolean setupColorProxy(@NotNull LivingEntity instance, MobEffect pEffect){
        if(instance.hasEffect(pEffect)) return true;
        if(pEffect != MobEffects.NIGHT_VISION || !(instance instanceof Player player)
                || !TransfurManager.isTransfurred(player)) return false;
        AbstractTransfurType transfurType = TransfurManager.getTransfurType(player);
        return transfurType instanceof AbstractLatexCat
                || (transfurType instanceof AbstractWaterLatex && player.isInWaterOrBubble());
    }
}