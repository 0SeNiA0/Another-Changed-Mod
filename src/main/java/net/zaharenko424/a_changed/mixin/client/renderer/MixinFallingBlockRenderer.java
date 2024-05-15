package net.zaharenko424.a_changed.mixin.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.block.VerticalTwoBlockMultiBlock;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FallingBlockRenderer.class)
public abstract class MixinFallingBlockRenderer extends EntityRenderer<FallingBlockEntity> {
    @Shadow @Final private BlockRenderDispatcher dispatcher;

    protected MixinFallingBlockRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    /**
     *  Render falling tall box
     */
    @Inject(at = @At(value = "INVOKE", target = "com/mojang/blaze3d/vertex/PoseStack.popPose ()V", shift = At.Shift.BEFORE),
            method = "render(Lnet/minecraft/world/entity/item/FallingBlockEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", locals = LocalCapture.CAPTURE_FAILHARD, allow = 1)
    private void onRender(FallingBlockEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci, @NotNull BlockState blockstate, Level level, BlockPos blockpos){
        if(!(blockstate.getBlock() instanceof VerticalTwoBlockMultiBlock)) return;
        blockstate = blockstate.setValue(StateProperties.PART2, 1);
        blockpos = blockpos.above();
        BakedModel model = dispatcher.getBlockModel(blockstate);
        pPoseStack.translate(0, 1, 0);
        for (RenderType renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(pEntity.getStartPos())), net.neoforged.neoforge.client.model.data.ModelData.EMPTY)) {
            dispatcher.getModelRenderer().tesselateBlock(
                level,
                dispatcher.getBlockModel(blockstate),
                blockstate,
                blockpos,
                pPoseStack,
                pBuffer.getBuffer(net.neoforged.neoforge.client.RenderTypeHelper.getMovingBlockRenderType(renderType)),
                false,
                RandomSource.create(),
                blockstate.getSeed(pEntity.getStartPos()),
                OverlayTexture.NO_OVERLAY,
                net.neoforged.neoforge.client.model.data.ModelData.EMPTY,
                renderType
            );
        }
    }
}