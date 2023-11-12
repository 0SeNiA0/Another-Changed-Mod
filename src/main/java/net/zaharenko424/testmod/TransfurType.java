package net.zaharenko424.testmod;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.testmod.client.model.AbstractLatexEntityModel;
import org.jetbrains.annotations.NotNull;

public abstract class TransfurType {

    public final ResourceLocation resourceLocation;
    public final ModelLayerLocation modelLayerLocation;

    protected TransfurType(ResourceLocation resourceLocation){
        this.resourceLocation = resourceLocation;
        modelLayerLocation = new ModelLayerLocation(resourceLocation,"main");
    }

    public abstract <T extends LivingEntity> AbstractLatexEntityModel<T> getModel(EntityRendererProvider.@NotNull Context context);

    public Component fancyName(){
        return Component.translatable("transfur."+ resourceLocation);
    }
}