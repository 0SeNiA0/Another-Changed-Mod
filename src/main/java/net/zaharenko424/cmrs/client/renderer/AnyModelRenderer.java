package net.zaharenko424.cmrs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.cmrs.api.CustomModel;
import org.jetbrains.annotations.NotNull;

public class AnyModelRenderer <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> extends CustomModelRenderer<E, M> {

    public AnyModelRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, null);
    }

    public void setModel(M model){
        this.model = model;
    }

    public void render(M model, @NotNull E entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        setModel(model);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        setModel(null);
    }
}
