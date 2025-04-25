package net.zaharenko424.a_changed.client.cmrs.properties;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import net.zaharenko424.a_changed.client.cmrs.api.MatrixStack;
import net.zaharenko424.a_changed.client.cmrs.api.RenderLayerLike;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.geom.Reusable;
import net.zaharenko424.a_changed.client.cmrs.model.PoseTransform;
import net.zaharenko424.a_changed.util.CodecUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemInMawLayer implements RenderLayerLike {

    public static final StreamCodec<FriendlyByteBuf, ItemInMawLayer> CODEC = StreamCodec.of((buffer, itemInMaw) -> {
        buffer.writeUtf(itemInMaw.maw);
        CodecUtils.writeOptionally(itemInMaw.transform, !itemInMaw.transform.isEmpty(), buffer, PoseTransform.CODEC);
    }, buffer -> {
        String maw = buffer.readUtf();
        return new ItemInMawLayer(maw, CodecUtils.readOptionally(buffer, PoseTransform.CODEC));
    });

    private String maw;
    private final PoseTransform transform;
    private final ItemInHandRenderer renderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;

    public ItemInMawLayer(@NotNull String maw, @Nullable PoseTransform transform) {
        this.maw = maw;
        this.transform = transform == null ? new PoseTransform() : transform;
    }

    public void transformToMaw(CustomModel<?> model, PoseStack matrixStack){
        ModelPart part = model.getPart(maw);
        if(part != null) part.translateAndRotate(matrixStack);
        transform.apply(matrixStack);
    }

    @Override
    public <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull CustomModel<E> model, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = livingEntity.getMainHandItem();
        if (stack.isEmpty()) return;

        /*if (stack.is(Items.SPYGLASS) && livingEntity.getUseItem() == stack && livingEntity.swingTime == 0
                && model.hasProperty(ModelPropertyRegistry.HEAD.get())) {
            renderArmWithSpyglass(livingEntity, model, stack, poseStack, buffer, packedLight);
            return;
        }*/

        MatrixStack.push(matrixStack);

        if(livingEntity.isBaby()) {
            float scale = livingEntity.getAgeScale();
            matrixStack.scale(scale, scale, scale);
        }

        transformToMaw(model, matrixStack);

        matrixStack.mulPose(Reusable.QUATERNION.get().identity().rotateZYX(0, Mth.DEG_TO_RAD * 225, Mth.DEG_TO_RAD * 90));
        if(stack.getItem() instanceof BlockItem) {
            matrixStack.translate(-.09, -.1, 0);
        }

        renderer.renderItem(livingEntity, stack, ItemDisplayContext.NONE, livingEntity.getMainArm() == HumanoidArm.LEFT, matrixStack, buffer, packedLight);
        MatrixStack.pop(matrixStack);
    }

    /*private <E extends LivingEntity> void renderArmWithSpyglass(E entity, CustomModel<E> model, ItemStack stack, MatrixStack matrixStack, MultiBufferSource buffer, int combinedLight) {
        matrixStack.push();
        ModelPart modelpart = model.getPart(model.getProperty(ModelPropertyRegistry.HEAD.get()));//TODO add headOverride
        float f = modelpart.xRot;
        modelpart.xRot = Mth.clamp(modelpart.xRot, (float) (-Math.PI / 6), (float) (Math.PI / 2));
        modelpart.translateAndRotate(matrixStack);
        modelpart.xRot = f;
        ItemOnHead.translateToHead(matrixStack, false);
        boolean flag = entity.getMainArm() == HumanoidArm.LEFT;
        matrixStack.translate((flag ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);
        renderer.renderItem(entity, stack, ItemDisplayContext.HEAD, flag, matrixStack, buffer, combinedLight);
        matrixStack.pop();
    }*/
}