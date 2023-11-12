package net.zaharenko424.testmod.entity.transfurTypes;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.testmod.TransfurType;
import net.zaharenko424.testmod.client.model.AbstractLatexEntityModel;
import net.zaharenko424.testmod.client.model.WhiteLatexModel;
import org.jetbrains.annotations.NotNull;

public class WhiteLatex extends TransfurType {

    public WhiteLatex(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public <T extends LivingEntity> AbstractLatexEntityModel<T> getModel(@NotNull EntityRendererProvider.Context context) {
        return (AbstractLatexEntityModel<T>) new WhiteLatexModel(context.bakeLayer(modelLayerLocation));
    }
}