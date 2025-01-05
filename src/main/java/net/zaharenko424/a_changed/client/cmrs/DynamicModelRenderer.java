package net.zaharenko424.a_changed.client.cmrs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DynamicModelRenderer <E extends LivingEntity, M extends EntityModel<E> & CustomModel<E>> extends CustomModelRenderer<E, M> {

    protected final Function<E, M> func;

    public DynamicModelRenderer(@NotNull EntityRendererProvider.Context context, @NotNull Function<@NotNull E, @Nullable M> modelFunction, float shadowRadius) {
        super(context, null, shadowRadius);
        func = modelFunction;
    }

    @Override
    public void render(@NotNull E entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        updateModel(entity);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void renderHand(@NotNull E entity, @NotNull PoseStack stack, int packedLight, @NotNull HumanoidArm arm){
        updateModel(entity);
        super.renderHand(entity, stack, packedLight, arm);
    }

    protected void updateModel(@NotNull E entity) {
        model = func.apply(entity);
    }
}