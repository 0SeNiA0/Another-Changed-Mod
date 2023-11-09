package net.zaharenko424.testmod.client.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.client.model.TestEntityModel;
import net.zaharenko424.testmod.entity.TestEntity;
import org.jetbrains.annotations.NotNull;

public class TestEntityRenderer extends MobRenderer<TestEntity,EntityModel<TestEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TestMod.MODID,"textures/entity/test_entity.png");

    public TestEntityRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new TestEntityModel<>(p_174304_.bakeLayer(TestEntityModel.LAYER_LOCATION)), 1);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TestEntity p_114482_) {
        return TEXTURE;
    }
}