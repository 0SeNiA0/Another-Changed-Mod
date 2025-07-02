package net.zaharenko424.a_changed.mixin.cmrs.renderer;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LightTexture;turnOffLightLayer()V"),
            method = "renderItemInHand")
    private void endBatch(Camera camera, float partialTick, Matrix4f projectionMatrix, CallbackInfo ci){
        MultiBufferSource.getInstance().endBatch();
    }
}
