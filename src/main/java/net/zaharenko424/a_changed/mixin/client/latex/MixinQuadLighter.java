package net.zaharenko424.a_changed.mixin.client.latex;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.lighting.QuadLighter;
import net.zaharenko424.a_changed.BakedQuadExtension;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(QuadLighter.class)
public abstract class MixinQuadLighter {

    @Unique
    private CoveredWith mod$coveredWith;

    /**
     * Checks whether the block is latex covered.
     */
    @Inject(at = @At("TAIL"), method = "setup")
    private void onSetup(BlockAndTintGetter level, BlockPos pos, BlockState state, CallbackInfo ci){
        mod$coveredWith = LatexCoveredData.of(Minecraft.getInstance().level.getChunkAt(pos)).getCoveredWith(pos);
    }

    @Inject(at = @At("TAIL"), method = "reset")
    private void onReset(CallbackInfo ci){
        mod$coveredWith = CoveredWith.NOTHING;
    }

    /**
     * Sets BakedQuad textures to latex textures if block is latex covered
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/BakedQuad;isTinted()Z"),
            method = "process")
    private boolean onProcess(boolean original, @Local(argsOnly = true) BakedQuad quad){
        if(mod$coveredWith == CoveredWith.NOTHING) return quad.isTinted();

        if(mod$coveredWith == CoveredWith.DARK_LATEX){
            ((BakedQuadExtension)quad).mod$darkLatex();
        } else ((BakedQuadExtension)quad).mod$whiteLatex();

        return quad.isTinted();
    }

    /**
     *  Resets BakedQuad texture to default if it was switched before rendering.
     */
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFF[IIZ)V", shift = At.Shift.AFTER),
            method = "process")
    private void onPostProcess(VertexConsumer consumer, PoseStack.Pose pose, BakedQuad quad, int overlay, CallbackInfo ci){
        if(mod$coveredWith == CoveredWith.NOTHING) return;

        ((BakedQuadExtension)quad).mod$clear();
    }
}