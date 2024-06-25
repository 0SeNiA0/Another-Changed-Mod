package net.zaharenko424.a_changed.mixin.client.latex;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.zaharenko424.a_changed.BakedQuadExtension;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(BlockRenderDispatcher.class)
public abstract class MixinBlockRendererDispatcher {

    @Shadow public abstract BakedModel getBlockModel(BlockState pState);

    @Unique
    private boolean mod$wasLatexCovered;

    /**
     * Sets BakedQuads textures to latex textures if block is latex covered
     */
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;tesselateBlock(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"), index = 1,
            method = "renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;Lnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V")
    private BakedModel onRenderBatched(BakedModel model, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) RandomSource random, @Local(argsOnly = true) ModelData data, @Local(argsOnly = true) RenderType renderType){
        CoveredWith coveredWith = LatexCoveredData.of(Minecraft.getInstance().level.getChunkAt(pos)).getCoveredWith(pos);
        if(coveredWith == CoveredWith.NOTHING) return model;

        mod$wasLatexCovered = true;
        Consumer<BakedQuad> consumer = coveredWith == CoveredWith.DARK_LATEX ? BakedQuadExtension.dlMode : BakedQuadExtension.wlMode;

        long seed = state.getSeed(pos);
        for(Direction direction : Direction.values()){
            random.setSeed(seed);
            model.getQuads(state, direction, random, data, renderType).forEach(consumer);
        }
        random.setSeed(seed);
        model.getQuads(state, null, random, data, renderType).forEach(consumer);
        return model;
    }

    /**
     *  Resets BakedQuads texture to default if it was switched before rendering.
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;tesselateBlock(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V", shift = At.Shift.AFTER),
            method = "renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;Lnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V")
    private void onPostRenderBatched(BlockState state, BlockPos pos, BlockAndTintGetter p_234358_, PoseStack p_234359_, VertexConsumer p_234360_, boolean p_234361_, RandomSource random, ModelData data, RenderType renderType, CallbackInfo ci){
        if(!mod$wasLatexCovered) return;

        BakedModel model = getBlockModel(state);
        long seed = state.getSeed(pos);
        for(Direction direction : Direction.values()){
            random.setSeed(seed);
            model.getQuads(state, direction, random, data, renderType).forEach(quad -> ((BakedQuadExtension)quad).mod$clear());
        }
        random.setSeed(seed);
        model.getQuads(state, null, random, data, renderType).forEach(quad -> ((BakedQuadExtension)quad).mod$clear());
    }

    //renderSingleBlock does not have BlockPos -> don't need to touch it
}