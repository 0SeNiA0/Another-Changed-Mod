package net.zaharenko424.cmrs.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.client.geom.Node;
import net.zaharenko424.cmrs.client.model.Texture;
import net.zaharenko424.cmrs.property.ModelPropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface CustomModel <E extends LivingEntity> {

    Node root();

    Node getPart(@NotNull String name);

    boolean hasProperty(@NotNull String key, @NotNull ModelPropertyType<?> type);

    boolean hasProperty(@NotNull String key, @NotNull DeferredHolder<ModelPropertyType<?>, ?> type);

    <P extends ModelProperty> P getProperty(@NotNull String key, @NotNull ModelPropertyType<P> type);

    <P extends ModelProperty> P getProperty(@NotNull String key, @NotNull DeferredHolder<ModelPropertyType<?>, ModelPropertyType<P>> type);

    void renderToBuffer(@NotNull E entity, @NotNull PoseStack poseStack, @Nullable Function<ResourceLocation, RenderType> suggestedRenderType, int packedLight, int packedOverlay, int color);

    void renderLayers(@NotNull PoseStack poseStack, int packedLight, @NotNull E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    /**
     * For rendering hands in first person view.
     */
    void renderHand(@NotNull E entity, @NotNull PoseStack poseStack, int light, @NotNull HumanoidArm arm);

    void setupAnim(@NotNull E entity, @NotNull PoseStack poseStack, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

    ResourceLocation getTexture();

    Texture getTexture(int index);

    float getShadowRadius(@NotNull E entity);
}