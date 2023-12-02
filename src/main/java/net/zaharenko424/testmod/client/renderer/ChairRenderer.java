package net.zaharenko424.testmod.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.testmod.entity.SeatEntity;
import org.jetbrains.annotations.NotNull;

public class ChairRenderer extends EntityRenderer<SeatEntity> {
    public ChairRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SeatEntity p_114482_) {
        return new ResourceLocation("_");
    }
}