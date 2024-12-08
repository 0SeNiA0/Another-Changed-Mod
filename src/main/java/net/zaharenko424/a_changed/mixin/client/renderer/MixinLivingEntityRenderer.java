package net.zaharenko424.a_changed.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.client.cmrs.api.NoYFlip;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    @Shadow
    protected M model;

    @Unique
    private static final ResourceLocation achanged$TEXTURE = AChanged.textureLoc("entity/latex_covered");

    @Shadow
    public static int getOverlayCoords(LivingEntity livingEntity, float u) {
        return 0;
    }

    @Shadow protected abstract float getWhiteOverlayProgress(T livingEntity, float partialTicks);

    @Shadow protected abstract boolean isBodyVisible(T livingEntity);

    @Unique
    protected final List<RenderLayer<T, M>> a_changed$noFlipLayers = new ArrayList<>();

    protected MixinLivingEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), method = "addLayer")
    private <E> boolean addNoYFlipLayer(List<E> instance, E e, Operation<Boolean> original) {
        if (!(e instanceof NoYFlip)) return original.call(instance, e);//false error highlight
        a_changed$noFlipLayers.add((RenderLayer<T, M>) e);
        return true;
    }

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V", ordinal = 1),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private boolean noFlip(PoseStack instance, float x, float y, float z) {
        return !(model instanceof NoYFlip);
    }

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 1),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private boolean noTranslate(PoseStack instance, float x, float y, float z, @Local(ordinal = 5) float a) {
        return !(model instanceof NoYFlip);
    }

    @Unique
    private final float[] a_changed$tmp = new float[5];

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;prepareMobModel(Lnet/minecraft/world/entity/Entity;FFF)V"),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void captureFloats(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci, @Local(name = "f5") float limbSwing, @Local(name = "f4") float limbSwingAmount, @Local(name = "f9") float ageInTicks, @Local(name = "f2") float netHeadYaw, @Local(name = "f6") float headPitch){
        a_changed$tmp[0] = limbSwing;
        a_changed$tmp[1] = limbSwingAmount;
        a_changed$tmp[2] = ageInTicks;
        a_changed$tmp[3] = netHeadYaw;
        a_changed$tmp[4] = headPitch;
    }
    
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;"),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private RenderType renderCustomModel(RenderType original, @Local(argsOnly = true) PoseStack poseStack, @Local(argsOnly = true) MultiBufferSource buffer, @Local(argsOnly = true) int packedLight, @Local(argsOnly = true) T entity, @Local(ordinal = 1, argsOnly = true) float partialTicks, @Local Minecraft minecraft){
        if(!(model instanceof CustomModel<?> custom)) return original;

        boolean visible = isBodyVisible(entity);
        boolean translucent = !visible && !entity.isInvisibleTo(minecraft.player);
        Function<ResourceLocation, RenderType> func;
        if(original == null){
            func = null;
        } else if(translucent){
            func = RenderType::itemEntityTranslucentCull;
        } else if(visible){
            func = model::renderType;
        } else func = minecraft.shouldEntityAppearGlowing(entity) ? RenderType::outline : null;

        ((CustomModel<LivingEntity>)custom).renderToBuffer(entity, poseStack, func, packedLight, getOverlayCoords(entity, getWhiteOverlayProgress(entity, partialTicks)), translucent ? 654311423 : -1);
        return null;
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private boolean renderLayers(boolean original, @Local(argsOnly = true) PoseStack poseStack, @Local(argsOnly = true) MultiBufferSource bufferSource, @Local(argsOnly = true) int light, @Local(argsOnly = true) T entity, @Local(ordinal = 1, argsOnly = true) float partialTicks) {
        if (original) return true;
        boolean noFlipModel = model instanceof NoYFlip;
        boolean noFlipLayers = !a_changed$noFlipLayers.isEmpty();

        if (noFlipLayers || model instanceof CustomModel<?>) {
            if (!noFlipModel) {
                poseStack.scale(-1, -1, 1);
                poseStack.translate(0, 1.501, 0);
            }
            if(model instanceof CustomModel<?> m) ((CustomModel<LivingEntity>)m).renderLayers(poseStack, light, entity, a_changed$tmp[0], a_changed$tmp[1], partialTicks, a_changed$tmp[2], a_changed$tmp[3], a_changed$tmp[4]);
            if(noFlipLayers) a_changed$noFlipLayers.forEach(layer -> layer.render(poseStack, bufferSource, light, entity, a_changed$tmp[0], a_changed$tmp[1], partialTicks, a_changed$tmp[2], a_changed$tmp[3], a_changed$tmp[4]));
        } else if (!noFlipModel) return false;

        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -1.501, 0);
        return false;
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

        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(achanged$TEXTURE)),
                pPackedLight, OverlayTexture.NO_OVERLAY,
                FastColor.ARGB32.color(FastColor.as8BitChannel(progress / TransfurManager.TRANSFUR_TOLERANCE), primaryColor));
        pPoseStack.popPose();
    }
}