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
import org.jetbrains.annotations.NotNull;

public final class VanillaElytra implements RenderLayerLike {

    public static final StreamCodec<FriendlyByteBuf, VanillaElytra> CODEC = StreamCodec.composite(
            PoseTransform.CODEC,
            vanillaElytra -> vanillaElytra.transform,
            VanillaElytra::new
    );

    public static final ResourceLocation WINGS_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");
    private final ElytraModel<LivingEntity> elytra;//potentially static -f
    private final PoseTransform transform;

    public VanillaElytra(PoseTransform transform){
        elytra = new ElytraModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ELYTRA));
        this.transform = transform;
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
        transform.apply(poseStack);

        ((EntityModel<LivingEntity>)model).copyPropertiesTo(elytra);
        elytra.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        elytra.renderToBuffer(poseStack, ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(texture), stack.hasFoil()), packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}