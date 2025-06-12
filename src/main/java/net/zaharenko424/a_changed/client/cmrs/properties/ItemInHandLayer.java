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
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import net.zaharenko424.a_changed.client.cmrs.api.MatrixStack;
import net.zaharenko424.a_changed.client.cmrs.api.ModelPropertyRegistry;
import net.zaharenko424.a_changed.client.cmrs.api.RenderLayerLike;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.model.PoseTransform;
import net.zaharenko424.a_changed.client.cmrs.util.StreamCodecUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemInHandLayer implements RenderLayerLike {
//absolute translate, rotate, scale for now(not additive)
    public static final StreamCodec<FriendlyByteBuf, ItemInHandLayer> CODEC = StreamCodec.of((buffer, itemInHand) -> {
            StreamCodecUtils.writeOptionally(itemInHand.armR, itemInHand.armR != null, buffer, ByteBufCodecs.STRING_UTF8);
            StreamCodecUtils.writeOptionally(itemInHand.transformR, !itemInHand.transformR.isEmpty(), buffer, PoseTransform.CODEC);
            StreamCodecUtils.writeOptionally(itemInHand.armL, itemInHand.armL != null, buffer, ByteBufCodecs.STRING_UTF8);
            StreamCodecUtils.writeOptionally(itemInHand.transformL, !itemInHand.transformL.isEmpty(), buffer, PoseTransform.CODEC);
            StreamCodecUtils.writeOptionally(itemInHand.headOverride, itemInHand.headOverride != null, buffer, ByteBufCodecs.STRING_UTF8);
        }, buffer -> {
            String armR = StreamCodecUtils.readOptionally(buffer, ByteBufCodecs.STRING_UTF8);
            PoseTransform transformR = StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC);
            String armL = StreamCodecUtils.readOptionally(buffer, ByteBufCodecs.STRING_UTF8);
            PoseTransform transformL = StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC);
            return new ItemInHandLayer(armR, transformR, armL, transformL, StreamCodecUtils.readOptionally(buffer, ByteBufCodecs.STRING_UTF8));
    });

    private String armR;
    private final PoseTransform transformR;//TODO switch to modifying the modelPart instead?
    private String armL;
    private final PoseTransform transformL;
    //Set to empty or unused string to cancel the spyglass rendering
    private String headOverride;
    private final ItemInHandRenderer renderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;

    public ItemInHandLayer(@Nullable String armR, @Nullable PoseTransform transformR, @Nullable String armL, @Nullable PoseTransform transformL){
        this(armR, transformR, armL, transformL, null);
    }

    public ItemInHandLayer(@Nullable String armR, @Nullable PoseTransform transformR, @Nullable String armL, @Nullable PoseTransform transformL, @Nullable String headOverride){
        this.armR = armR;
        this.transformR = transformR == null ? new PoseTransform(null, null, null) : transformR;
        this.armL = armL;
        this.transformL = transformL == null ? new PoseTransform(null, null, null) : transformL;
        this.headOverride = headOverride;
    }

    public ModelPart getArm(CustomModel<?> model, HumanoidArm arm){
        String target = arm == HumanoidArm.RIGHT ? armR : armL;
        if(target == null) return null;
        return model.getPart(target);
    }

    public void transformToArm(CustomModel<?> model, HumanoidArm arm, PoseStack matrixStack){
        ModelPart part = getArm(model, arm);
        if(part != null) part.translateAndRotate(matrixStack);
        if(arm == HumanoidArm.RIGHT){
            transformR.apply(matrixStack);
        } else transformL.apply(matrixStack);
    }

    @Override
    public <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull CustomModel<E> model, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean flag = livingEntity.getMainArm() == HumanoidArm.RIGHT;
        ItemStack itemstack = flag ? livingEntity.getOffhandItem() : livingEntity.getMainHandItem();
        ItemStack itemstack1 = flag ? livingEntity.getMainHandItem() : livingEntity.getOffhandItem();
        if(!itemstack.isEmpty())
            renderArmWithItem(livingEntity, model, itemstack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, matrixStack, buffer, packedLight);

        if(!itemstack1.isEmpty())
            renderArmWithItem(livingEntity, model, itemstack1, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, matrixStack, buffer, packedLight);
    }

    private <E extends LivingEntity> void renderArmWithItem(E livingEntity, CustomModel<E> model, ItemStack itemStack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (itemStack.is(Items.SPYGLASS) && livingEntity.getUseItem() == itemStack && livingEntity.swingTime == 0
                && (headOverride != null || model.hasProperty(ModelPropertyRegistry.HEAD.get()))) {
            if(renderArmWithSpyglass(livingEntity, model, itemStack, arm, matrixStack, buffer, packedLight)) return;
        }
        MatrixStack.push(matrixStack);
        transformToArm(model, arm, matrixStack);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        boolean flag = arm == HumanoidArm.LEFT;
        matrixStack.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
        renderer.renderItem(livingEntity, itemStack, displayContext, flag, matrixStack, buffer, packedLight);
        MatrixStack.pop(matrixStack);
    }

    private <E extends LivingEntity> boolean renderArmWithSpyglass(E entity, CustomModel<E> model, ItemStack stack, HumanoidArm arm, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight) {
        MatrixStack.push(matrixStack);
        ModelPart modelpart = model.getPart(headOverride != null ? headOverride : model.getProperty(ModelPropertyRegistry.HEAD.get()));
        if(modelpart == null) return false;
        float f = modelpart.xRot;
        modelpart.xRot = Mth.clamp(modelpart.xRot, (float) (-Math.PI / 6), (float) (Math.PI / 2));
        modelpart.translateAndRotate(matrixStack);
        modelpart.xRot = f;
        ItemOnHead.translateToHead(matrixStack, false);
        boolean flag = arm == HumanoidArm.LEFT;
        matrixStack.translate((flag ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);
        renderer.renderItem(entity, stack, ItemDisplayContext.HEAD, false, matrixStack, buffer, combinedLight);
        MatrixStack.pop(matrixStack);
        return true;
    }
}