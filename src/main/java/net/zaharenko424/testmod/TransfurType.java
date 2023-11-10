package net.zaharenko424.testmod;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.testmod.client.model.LatexEntityModel;
import org.jetbrains.annotations.NotNull;

public abstract class TransfurType {

    public final ResourceLocation resourceLocation;
    public final ModelLayerLocation modelLayerLocation;


    protected TransfurType(ResourceLocation resourceLocation){
        this.resourceLocation=resourceLocation;
        modelLayerLocation =new ModelLayerLocation(resourceLocation,"main");
    }

    public LatexEntityModel<AbstractClientPlayer> playerModel(EntityRendererProvider.@NotNull Context context){
        return new LatexEntityModel<>(context.bakeLayer(modelLayerLocation));
    }
}
