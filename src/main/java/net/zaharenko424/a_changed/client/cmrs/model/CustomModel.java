package net.zaharenko424.a_changed.client.cmrs.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.atest.ModelPropertyType;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface CustomModel {

    ModelPart root();

    ModelPart getPart(String name);

    boolean hasProperty(ModelPropertyType<?> type);

    <P> P getProperty(ModelPropertyType<P> type);

    void renderToBuffer(@NotNull LivingEntity entity, @NotNull PoseStack poseStack, @Nullable Function<ResourceLocation, RenderType> suggestedRenderType, int packedLight, int packedOverlay, int color);

    ResourceLocation getTexture();

    void setDrawAll(boolean draw);

    void setAllVisible(boolean visible);
}