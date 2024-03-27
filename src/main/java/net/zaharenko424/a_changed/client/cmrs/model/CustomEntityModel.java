package net.zaharenko424.a_changed.client.cmrs.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class CustomEntityModel<E extends LivingEntity> extends HierarchicalHumanoidModel<E> {

    protected final ResourceLocation texture;

    public CustomEntityModel(ModelLayerLocation location, ResourceLocation texture) {
        this(ModelDefinitionCache.INSTANCE.bake(location), texture);
    }

    public CustomEntityModel(ModelPart root, ResourceLocation texture){
        super(root);
        if(!texture.getPath().startsWith("textures/")) this.texture = AChanged.textureLoc(texture.withPrefix("entity/"));
        else this.texture = texture;
    }

    public boolean hasArmor(){
        return true;
    }

    public boolean hasGlowingArmor(){
        return false;
    }

    public boolean hasGlowParts(){
        return false;
    }

    public ResourceLocation getTexture(){
        return texture;
    }
}