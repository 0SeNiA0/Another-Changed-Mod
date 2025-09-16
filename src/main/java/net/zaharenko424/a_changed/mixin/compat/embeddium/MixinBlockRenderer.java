package net.zaharenko424.a_changed.mixin.compat.embeddium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.a_changed.BakedQuadExtension;
import net.zaharenko424.a_changed.attachment.LatexCoveredData;
import net.zaharenko424.a_changed.transfurSystem.CoveredWith;
import org.embeddedt.embeddium.api.render.chunk.BlockRenderContext;
import org.embeddedt.embeddium.impl.model.light.data.QuadLightData;
import org.embeddedt.embeddium.impl.model.quad.BakedQuadView;
import org.embeddedt.embeddium.impl.render.chunk.compile.buffers.ChunkModelBuilder;
import org.embeddedt.embeddium.impl.render.chunk.compile.pipeline.BlockRenderer;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public class MixinBlockRenderer {

    @Unique
    private boolean achanged$latexCovered;

    @Inject(at = @At("HEAD"), method = "writeGeometry")
    private void setupLatex(BlockRenderContext ctx, ChunkModelBuilder builder, Vec3 offset, Material material, BakedQuadView quad, int[] colors, QuadLightData light, CallbackInfo ci){
        BlockPos pos = ctx.pos();
        CoveredWith coveredWith = LatexCoveredData.of(Minecraft.getInstance().level.getChunkAt(pos)).getCoveredWith(pos);
        if(coveredWith == CoveredWith.NOTHING) return;

        achanged$latexCovered = true;
        ((BakedQuadExtension)quad).achanged$prepareLatex(coveredWith);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lorg/embeddedt/embeddium/impl/model/quad/BakedQuadView;getTexU(I)F"),
            method = "writeGeometry")
    private float modifyU(BakedQuadView instance, int i, Operation<Float> original){
        if(!achanged$latexCovered) return original.call(instance, i);
        return ((BakedQuadExtension)instance).achanged$getU(i);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lorg/embeddedt/embeddium/impl/model/quad/BakedQuadView;getTexV(I)F"),
            method = "writeGeometry")
    private float modifyV(BakedQuadView instance, int i, Operation<Float> original){
        if(!achanged$latexCovered) return original.call(instance, i);
        return ((BakedQuadExtension)instance).achanged$getV(i);
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lorg/embeddedt/embeddium/impl/util/ModelQuadUtil;mixARGBColors(II)I"), index = 0,
            method = "writeGeometry")
    private int modifyTint(int colorA){
        if(!achanged$latexCovered) return colorA;
        return -1;
    }

    @Inject(at = @At("RETURN"), method = "writeGeometry")
    private void clearLatex(BlockRenderContext ctx, ChunkModelBuilder builder, Vec3 offset, Material material, BakedQuadView quad, int[] colors, QuadLightData light, CallbackInfo ci){
        if(!achanged$latexCovered) return;

        achanged$latexCovered = false;
        ((BakedQuadExtension)quad).achanged$clear();
    }
}
