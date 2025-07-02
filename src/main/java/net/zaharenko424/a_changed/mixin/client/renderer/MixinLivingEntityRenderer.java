package net.zaharenko424.a_changed.mixin.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.cmrs.api.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    @Shadow
    protected M model;

    @Unique
    private static final ResourceLocation achanged$TEXTURE = AChanged.textureLoc("entity/latex_covered");

    protected MixinLivingEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    /**
     *  Renders latex overlay depending on transfur progress
     */
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void latexOverlay(@NotNull T entity, float pEntityYaw, float pPartialTicks, PoseStack stack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci){
        TransfurHandler handler = TransfurHandler.of(entity);
        if(handler == null || handler.isTransfurred() || handler.getTransfurType() == null) return;//TODO move before layers -> villager hat is set to visible in a layer

        float progress = handler.getTransfurProgress();
        if(progress <= 0) return;

        MatrixStack.push(stack);
        stack.scale(1.02f, 1.02f, 1.02f);
        int primaryColor = handler.getTransfurType().getPrimaryColor();

        model.renderToBuffer(stack, pBuffer.getBuffer(RenderType.entityTranslucent(achanged$TEXTURE)),
                pPackedLight, OverlayTexture.NO_OVERLAY,
                FastColor.ARGB32.color(FastColor.as8BitChannel(progress / TransfurManager.TRANSFUR_TOLERANCE), primaryColor));
        MatrixStack.pop(stack);
    }
}