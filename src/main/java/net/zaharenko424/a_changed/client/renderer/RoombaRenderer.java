package net.zaharenko424.a_changed.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.CustomMobRenderer;
import net.zaharenko424.a_changed.client.model.RoombaModel;
import net.zaharenko424.a_changed.entity.RoombaEntity;
import org.jetbrains.annotations.NotNull;

public class RoombaRenderer extends CustomMobRenderer<RoombaEntity, RoombaModel> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/roomba");

    public RoombaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RoombaModel(), .8f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull RoombaEntity pEntity) {
        return TEXTURE;
    }
}