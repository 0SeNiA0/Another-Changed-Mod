package net.zaharenko424.cmrs.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.client.layer.RenderLayerType;
import org.jetbrains.annotations.NotNull;

public interface RenderLayer {

    DeferredHolder<RenderLayerType<?>, ?> type();

    <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull CustomModel<E> model, @NotNull PoseStack matrixStack,
                                         @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount,
                                         float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);
}