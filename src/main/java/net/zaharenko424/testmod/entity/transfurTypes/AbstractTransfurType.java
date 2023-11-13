package net.zaharenko424.testmod.entity.transfurTypes;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.testmod.client.model.AbstractLatexEntityModel;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTransfurType {

    public final ResourceLocation resourceLocation;
    public final ResourceLocation entityResourceLocation;

    protected AbstractTransfurType(@NotNull ResourceLocation resourceLocation,@NotNull ResourceLocation entityResourceLocation){
        this.resourceLocation = resourceLocation;
        this.entityResourceLocation=entityResourceLocation;
    }
    @OnlyIn(Dist.CLIENT)
    public abstract <T extends LivingEntity> AbstractLatexEntityModel<T> getModel(EntityRendererProvider.@NotNull Context context);
    @OnlyIn(Dist.CLIENT)
    public ModelLayerLocation modelLayerLocation(){
        return new ModelLayerLocation(resourceLocation,"main");
    }

    public Component fancyName(){
        return Component.translatable("transfur."+ resourceLocation);
    }
}