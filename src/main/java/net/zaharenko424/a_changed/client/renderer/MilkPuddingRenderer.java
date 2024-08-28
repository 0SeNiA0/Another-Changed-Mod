package net.zaharenko424.a_changed.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.CustomMobRenderer;
import net.zaharenko424.a_changed.client.model.MilkPuddingModel;
import net.zaharenko424.a_changed.entity.MilkPuddingEntity;
import org.jetbrains.annotations.NotNull;

public class MilkPuddingRenderer extends CustomMobRenderer<MilkPuddingEntity, MilkPuddingModel> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/latex_covered");

    public MilkPuddingRenderer(EntityRendererProvider.Context context) {
        super(context, new MilkPuddingModel(), .6f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MilkPuddingEntity pEntity) {
        return TEXTURE;
    }
}