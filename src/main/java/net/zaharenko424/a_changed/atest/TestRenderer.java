package net.zaharenko424.a_changed.atest;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public class TestRenderer <T extends LivingEntity, M extends TestModel<T>> extends LivingEntityRenderer<T, M> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/hypno_cat");

    public TestRenderer(EntityRendererProvider.Context pContext, M pModel) {
        super(pContext, pModel, 1);
        addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LivingEntity pEntity) {
        return TEXTURE;
    }
}
