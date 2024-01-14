package net.zaharenko424.a_changed.client.renderer.misc;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.entity.SeatEntity;
import org.jetbrains.annotations.NotNull;

public class ChairRenderer extends EntityRenderer<SeatEntity> {
    public ChairRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SeatEntity seat) {
        return new ResourceLocation("_");
    }
}