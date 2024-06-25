package net.zaharenko424.a_changed.mixin.client.latex;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.zaharenko424.a_changed.BakedQuadExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FaceBakery.class)
public class MixinFaceBakery {

    @ModifyReturnValue(at = @At(value = "TAIL"), method = "bakeQuad")
    private BakedQuad onBakeQuad(BakedQuad original, @Local BlockFaceUV uv){
        float[] uvs = new float[8];
        for(int i = 0; i < 4; i++){
            uvs[i * 2] = uv.getU(i) / 16;
            uvs[i * 2 + 1] = uv.getV(i) / 16;
        }
        ((BakedQuadExtension)original).mod$initUV(uvs);
        return original;
    }
}