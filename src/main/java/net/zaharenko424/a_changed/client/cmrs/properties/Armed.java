package net.zaharenko424.a_changed.client.cmrs.properties;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zaharenko424.a_changed.client.cmrs.CustomModelRenderer;
import net.zaharenko424.a_changed.client.cmrs.model.PartTransform;
import net.zaharenko424.a_changed.client.cmrs.model.PoseTransform;
import net.zaharenko424.a_changed.client.cmrs.api.RenderLayerLike;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import org.jetbrains.annotations.NotNull;

public final class Armed implements RenderLayerLike {
//absolute translate, rotate, scale for now(not additive)
    public static final StreamCodec<FriendlyByteBuf, Armed> CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, armed -> armed.armR,
        PoseTransform.CODEC, armed -> armed.transformR,
        PartTransform.CODEC, armed -> armed.transformRFP,
        ByteBufCodecs.STRING_UTF8, armed -> armed.armL,
        PoseTransform.CODEC, armed -> armed.transformL,
        PartTransform.CODEC, armed -> armed.transformRFP,
        Armed::new
    );

    private String armR;
    private final PoseTransform transformR;//TODO switch to modifying the modelPart instead?
    private final PartTransform transformRFP;//first person
    private String armL;
    private final PoseTransform transformL;
    private final PartTransform transformLFP;//first person
    private final ItemInHandRenderer renderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;

    public Armed(@NotNull String armR, @NotNull PoseTransform transformR, @NotNull PartTransform transformRFP, @NotNull String armL, @NotNull PoseTransform transformL, @NotNull PartTransform transformLFP){
        this.armR = armR;
        this.transformR = transformR;
        this.transformRFP = transformRFP;
        this.armL = armL;
        this.transformL = transformL;
        this.transformLFP = transformLFP;
    }

    public ModelPart getArm(CustomModel<?> model, HumanoidArm arm){
        return model.getPart(arm == HumanoidArm.RIGHT ? armR : armL);
    }

    public void transformToArm(CustomModel<?> model, HumanoidArm arm, PoseStack poseStack){
        ModelPart part = getArm(model, arm);
        if(part != null) part.translateAndRotate(poseStack);
        if(arm == HumanoidArm.RIGHT){
            transformR.apply(poseStack);
        } else transformL.apply(poseStack);
    }

    public void transformFirstPerson(CustomModel<?> model, HumanoidArm arm){
        ModelPart part = getArm(model, arm);
        if(part == null) return;
        if(arm == HumanoidArm.RIGHT){
            transformRFP.apply(part);
        } else transformLFP.apply(part);
    }

    @Override
    public <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull CustomModel<E> model, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean flag = livingEntity.getMainArm() == HumanoidArm.RIGHT;
        ItemStack itemstack = flag ? livingEntity.getOffhandItem() : livingEntity.getMainHandItem();
        ItemStack itemstack1 = flag ? livingEntity.getMainHandItem() : livingEntity.getOffhandItem();
        if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
            this.renderArmWithItem(livingEntity, model, itemstack1, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, buffer, packedLight);
            this.renderArmWithItem(livingEntity, model, itemstack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, buffer, packedLight);
        }
    }

    private <E extends LivingEntity> void renderArmWithItem(E livingEntity, CustomModel<E> model, ItemStack itemStack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (itemStack.is(Items.SPYGLASS) && livingEntity.getUseItem() == itemStack && livingEntity.swingTime == 0
                && model.hasProperty(CustomModelRenderer.HEAD)) {
            renderArmWithSpyglass(livingEntity, model, itemStack, arm, poseStack, buffer, packedLight);
            return;
        }
        if(itemStack.isEmpty()) return;
        poseStack.pushPose();
        transformToArm(model, arm, poseStack);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        boolean flag = arm == HumanoidArm.LEFT;
        poseStack.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
        renderer.renderItem(livingEntity, itemStack, displayContext, flag, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    private <E extends LivingEntity> void renderArmWithSpyglass(E entity, CustomModel<E> model, ItemStack stack, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int combinedLight) {
        poseStack.pushPose();
        ModelPart modelpart = model.getProperty(CustomModelRenderer.HEAD).getPart(model);
        float f = modelpart.xRot;
        modelpart.xRot = Mth.clamp(modelpart.xRot, (float) (-Math.PI / 6), (float) (Math.PI / 2));
        modelpart.translateAndRotate(poseStack);
        modelpart.xRot = f;
        Head.translateToHead(poseStack, false);
        boolean flag = arm == HumanoidArm.LEFT;
        poseStack.translate((flag ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);
        renderer.renderItem(entity, stack, ItemDisplayContext.HEAD, false, poseStack, buffer, combinedLight);
        poseStack.popPose();
    }
}