package net.zaharenko424.a_changed.client.cmrs.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;

public class URLLoadedModel <E extends LivingEntity> extends CustomEntityModel<E> {

    public URLLoadedModel(ModelPart root, ResourceLocation texture) {
        super(root, texture);
    }
}