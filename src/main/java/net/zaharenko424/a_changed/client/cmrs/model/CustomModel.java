package net.zaharenko424.a_changed.client.cmrs.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomModel {

    ModelPart root();

    default void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int color){
        root().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    void renderToBuffer(@NotNull PoseStack poseStack, @Nullable RenderType suggestedRenderType, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay, int color);

    ResourceLocation getTexture();

    void setDrawAll(boolean draw);

    void setAllVisible(boolean visible);

    boolean hasGlowParts();

    void setupDrawGlow(boolean draw);

    boolean hasArmor();

    boolean hasGlowingArmor();

    void setupArmorPart(EquipmentSlot slot, boolean glowing);
}