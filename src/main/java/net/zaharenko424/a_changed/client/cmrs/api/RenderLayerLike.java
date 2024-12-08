package net.zaharenko424.a_changed.client.cmrs.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface RenderLayerLike {

    <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull CustomModel<E> model, @NotNull PoseStack poseStack,
                                         @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount,
                                         float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);
}