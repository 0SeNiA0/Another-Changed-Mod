package net.zaharenko424.a_changed.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.RoombaModel;
import net.zaharenko424.a_changed.entity.RoombaEntity;
import org.jetbrains.annotations.NotNull;

public class RoombaRenderer extends LivingEntityRenderer<RoombaEntity, RoombaModel> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/roomba");

    public RoombaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RoombaModel(), .6f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull RoombaEntity pEntity) {
        return TEXTURE;
    }

    @Override
    protected boolean shouldShowName(@NotNull RoombaEntity entity) {
        return entity.hasCustomName() && super.shouldShowName(entity);
    }

    @Override
    protected void setupRotations(@NotNull RoombaEntity entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        if (isEntityUpsideDown(entity)) {
            poseStack.translate(0.0F, (entity.getBbHeight() + 0.1F) / scale, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }
}