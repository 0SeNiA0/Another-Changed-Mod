package net.zaharenko424.a_changed.mixin.client.latex;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.zaharenko424.a_changed.BakedQuadExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VertexConsumer.class)
public interface MixinVertexConsumer {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;addVertex(FFFIFFIIFFF)V"), method = "putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFFF[IIZ)V")
    private void onPutBulkData(VertexConsumer instance, float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ, Operation<Void> original, @Local(argsOnly = true) BakedQuad quad, @Local(name = {"l"}) int i){
        if(((BakedQuadExtension)quad).achanged$isCovered()){
            original.call(instance, x, y, z, color, ((BakedQuadExtension)quad).achanged$getU(i), ((BakedQuadExtension)quad).achanged$getV(i),
                    packedOverlay, packedLight, normalX, normalY, normalZ);
            return;
        }

        original.call(instance, x, y, z, color, u, v, packedOverlay, packedLight, normalX, normalY, normalZ);
    }
}