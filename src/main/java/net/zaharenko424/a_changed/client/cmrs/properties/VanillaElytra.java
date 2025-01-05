package net.zaharenko424.a_changed.client.cmrs.properties;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import net.zaharenko424.a_changed.client.cmrs.api.RenderLayerLike;
import net.zaharenko424.a_changed.client.cmrs.model.PoseTransform;
import net.zaharenko424.a_changed.util.CodecUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VanillaElytra implements RenderLayerLike {

    public static final StreamCodec<FriendlyByteBuf, VanillaElytra> CODEC = StreamCodec.of((buffer, vanillaElytra) -> {
            CodecUtils.writeOptionally(vanillaElytra.transform, !vanillaElytra.transform.isEmpty(), buffer, PoseTransform.CODEC);
            CodecUtils.writeOptionally(vanillaElytra.transformFlying, !vanillaElytra.transformFlying.isEmpty(), buffer, PoseTransform.CODEC);
            CodecUtils.writeOptionally(vanillaElytra.transformCrouching, !vanillaElytra.transformCrouching.isEmpty(), buffer, PoseTransform.CODEC);
            boolean babyTransform = vanillaElytra.babyTransform;
            buffer.writeBoolean(babyTransform);
            if(!babyTransform) return;
            CodecUtils.writeOptionally(vanillaElytra.bTransform, !vanillaElytra.bTransform.isEmpty(), buffer, PoseTransform.CODEC);
            CodecUtils.writeOptionally(vanillaElytra.bTransformFlying, !vanillaElytra.bTransformFlying.isEmpty(), buffer, PoseTransform.CODEC);
            CodecUtils.writeOptionally(vanillaElytra.bTransformCrouching, !vanillaElytra.bTransformCrouching.isEmpty(), buffer, PoseTransform.CODEC);
    }, buffer -> {
            PoseTransform transform = CodecUtils.readOptionally(buffer, PoseTransform.CODEC);
            PoseTransform transformFlying = CodecUtils.readOptionally(buffer, PoseTransform.CODEC);
            PoseTransform transformCrouching = CodecUtils.readOptionally(buffer, PoseTransform.CODEC);

            return !buffer.readBoolean() ? new VanillaElytra(transform, transformFlying, transformCrouching)
                    : new VanillaElytra(transform, transformFlying, transformCrouching, true,
                        CodecUtils.readOptionally(buffer, PoseTransform.CODEC),
                        CodecUtils.readOptionally(buffer, PoseTransform.CODEC),
                        CodecUtils.readOptionally(buffer, PoseTransform.CODEC));
            }
    );

    public static final ResourceLocation WINGS_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");
    private final ElytraModel<LivingEntity> elytra;//potentially static -f
    private final PoseTransform transform;
    private final PoseTransform transformFlying;
    private final PoseTransform transformCrouching;
    private boolean babyTransform;
    private final PoseTransform bTransform;
    private final PoseTransform bTransformFlying;
    private final PoseTransform bTransformCrouching;

    public VanillaElytra(){
        this(null, null, null);
    }

    public VanillaElytra(@Nullable PoseTransform transform, @Nullable PoseTransform transformFlying, @Nullable PoseTransform transformCrouching){
        this(transform, transformFlying, transformCrouching, false, null, null, null);
    }

    public VanillaElytra(@Nullable PoseTransform transform, @Nullable PoseTransform transformFlying, @Nullable PoseTransform transformCrouching, boolean babyTransform, @Nullable PoseTransform bTransform, @Nullable PoseTransform bTransformFlying, @Nullable PoseTransform bTransformCrouching){
        elytra = new ElytraModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ELYTRA));
        this.transform = transform == null ? new PoseTransform() : transform;
        this.transformFlying = transformFlying == null ? new PoseTransform() : transformFlying;
        this.transformCrouching = transformCrouching == null ? new PoseTransform() : transformCrouching;
        this.babyTransform = babyTransform;
        this.bTransform = bTransform == null ? new PoseTransform() : bTransform;
        this.bTransformFlying = bTransformFlying == null ? new PoseTransform() : bTransformFlying;
        this.bTransformCrouching = bTransformCrouching == null ? new PoseTransform() : bTransformCrouching;
    }

    @Override
    public <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull CustomModel<E> model, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if(!(stack.getItem() instanceof ElytraItem)) return;

        ResourceLocation texture;
        if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
            PlayerSkin playerskin = abstractclientplayer.getSkin();
            if (playerskin.elytraTexture() != null) {
                texture = playerskin.elytraTexture();
            } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                texture = playerskin.capeTexture();
            } else {
                texture = WINGS_LOCATION;
            }
        } else {
            texture = WINGS_LOCATION;
        }

        poseStack.pushPose();
        poseStack.scale(-1, -1, 1);//flip since vanilla model is intended to be flipped but CustomModel is not
        poseStack.translate(0, -1.501, 0.125);

        boolean b = livingEntity.isBaby() && babyTransform;
        PoseTransform transform = livingEntity.isFallFlying() ? b ? bTransformFlying : transformFlying
                : livingEntity.isCrouching() ? b ? bTransformCrouching : transformCrouching
                : b ? bTransform : this.transform;
        transform.apply(poseStack);

        ((EntityModel<LivingEntity>)model).copyPropertiesTo(elytra);
        elytra.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        elytra.young = false;//Stop vanilla from applying baby transforms
        elytra.renderToBuffer(poseStack, ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(texture), stack.hasFoil()), packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}