package net.zaharenko424.a_changed.client.cmrs.api;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.geom.MatrixStack;
import org.jetbrains.annotations.NotNull;

public interface RenderLayerLike {

    <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull CustomModel<E> model, @NotNull MatrixStack matrixStack,
                                         @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount,
                                         float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);
}