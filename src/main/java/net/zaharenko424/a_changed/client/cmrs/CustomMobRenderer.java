package net.zaharenko424.a_changed.client.cmrs;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;

public abstract class CustomMobRenderer<E extends Mob, M extends EntityModel<E>> extends LivingEntityRenderer<E, M> {

    protected final EntityRendererProvider.Context context;

    public CustomMobRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
        this.context = context;
    }

}