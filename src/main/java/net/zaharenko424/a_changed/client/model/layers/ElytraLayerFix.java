package net.zaharenko424.a_changed.client.model.layers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.model.ElytraModelFix;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ElytraLayerFix<E extends LivingEntity,M extends EntityModel<E>> extends ElytraLayer<E,M> {

    public ElytraLayerFix(RenderLayerParent<E, M> parent, EntityModelSet modelSet) {
        super(parent, modelSet);
        elytraModel=new ElytraModelFix<>(modelSet.bakeLayer(ModelLayers.ELYTRA));
    }
}