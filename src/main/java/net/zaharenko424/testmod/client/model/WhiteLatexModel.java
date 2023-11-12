package net.zaharenko424.testmod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class WhiteLatexModel<T extends LivingEntity> extends AbstractLatexEntityModel<T> {

    public WhiteLatexModel(@NotNull ModelPart root) {
        super(root);
    }

    public static @NotNull LayerDefinition createBodyLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -31.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(12, 42).addBox(0.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -22.0F, 0.0F));

        PartDefinition left_arm_elbow = left_arm.addOrReplaceChild("left_arm_elbow", CubeListBuilder.create().texOffs(44, 45).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 4.0F, 0.0F));

        PartDefinition left_arm_lower = left_arm_elbow.addOrReplaceChild("left_arm_lower", CubeListBuilder.create().texOffs(40, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(36, 35).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -22.0F, 0.0F));

        PartDefinition right_arm_elbow = right_arm.addOrReplaceChild("right_arm_elbow", CubeListBuilder.create().texOffs(28, 45).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 4.0F, 0.0F));

        PartDefinition right_arm_lower = right_arm_elbow.addOrReplaceChild("right_arm_lower", CubeListBuilder.create().texOffs(36, 25).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9F, -13.0F, 0.1F, 0.0F, 0.1309F, 0.1309F));

        PartDefinition left_leg_thigh_r1 = right_leg.addOrReplaceChild("left_leg_thigh_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1305F, -0.9914F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", CubeListBuilder.create(), PartPose.offset(-0.1305F, 4.0086F, -2.0F));

        PartDefinition left_leg_shin_r1 = right_leg_shin.addOrReplaceChild("left_leg_shin_r1", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -4.1F, -1.4F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));

        PartDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));

        PartDefinition left_leg__r1 = right_leg_.addOrReplaceChild("left_leg__r1", CubeListBuilder.create().texOffs(20, 29).addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition left_leg_foot = right_leg_.addOrReplaceChild("left_leg_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_leg_foot_r1 = left_leg_foot.addOrReplaceChild("left_leg_foot_r1", CubeListBuilder.create().texOffs(48, 22).addBox(-2.1F, 0.1F, -2.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9F, -13.0F, 0.1F, 0.0F, -0.1309F, -0.1309F));

        PartDefinition right_leg_thigh_r1 = left_leg.addOrReplaceChild("right_leg_thigh_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1305F, -0.9914F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", CubeListBuilder.create(), PartPose.offset(0.1305F, 4.0086F, -2.0F));

        PartDefinition right_leg_shin_r1 = left_leg_shin.addOrReplaceChild("right_leg_shin_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -4.1F, -1.4F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));

        PartDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));

        PartDefinition right_leg__r1 = left_leg_.addOrReplaceChild("right_leg__r1", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition right_leg_foot = left_leg_.addOrReplaceChild("right_leg_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg_foot_r1 = right_leg_foot.addOrReplaceChild("right_leg_foot_r1", CubeListBuilder.create().texOffs(48, 7).addBox(-1.9F, 0.1F, -2.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
