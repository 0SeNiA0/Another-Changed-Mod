package net.zaharenko424.a_changed.client.renderer.misc;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.entity.SeatEntity;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public class SeatRenderer extends EntityRenderer<SeatEntity> {

    public SeatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(@NotNull SeatEntity livingEntity, @NotNull Frustum camera, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SeatEntity seat) {
        return Utils.NULL_LOC;
    }
}