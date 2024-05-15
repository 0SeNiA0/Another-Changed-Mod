package net.zaharenko424.a_changed.client.cmrs.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;

public class URLLoadedModel <E extends LivingEntity> extends CustomHumanoidModel<E> {

    private final boolean hasGlowParts;

    public URLLoadedModel(ModelPart root, ResourceLocation texture) {
        super(root, texture);
        hasGlowParts = root.getAllParts().anyMatch(ModelPart::isGlowing);
    }

    @Override
    public boolean hasGlowParts() {
        return hasGlowParts;
    }
}