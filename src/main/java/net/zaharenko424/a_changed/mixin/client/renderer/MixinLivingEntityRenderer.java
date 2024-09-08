package net.zaharenko424.a_changed.mixin.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer <T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    @Shadow protected M model;

    @Unique
    private static final ResourceLocation mod$TEXTURE = AChanged.textureLoc("entity/latex_covered");

    protected MixinLivingEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    /**
     *  Renders latex overlay depending on transfur progress
     */
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.BEFORE),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void latexOverlay(@NotNull T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci){
        if(TransfurHandler.of(pEntity) == null) return;
        float progress = TransfurManager.getTransfurProgress(pEntity);
        if(progress <= 0 || progress >= TransfurManager.TRANSFUR_TOLERANCE) return;
        pPoseStack.pushPose();
        pPoseStack.scale(1.02f, 1.02f, 1.02f);
        int primaryColor = TransfurManager.getTransfurType(pEntity).getPrimaryColor();
        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(mod$TEXTURE)),
                pPackedLight, OverlayTexture.NO_OVERLAY,
                (0xFF & (primaryColor >> 16)) / 255f, (0xFF & (primaryColor >> 8)) / 255f, (0xFF & primaryColor) / 255f,
                progress / TransfurManager.TRANSFUR_TOLERANCE);
        pPoseStack.popPose();
    }
}