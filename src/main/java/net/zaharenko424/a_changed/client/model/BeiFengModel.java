package net.zaharenko424.a_changed.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public class BeiFengModel<T extends LivingEntity> extends AbstractLatexEntityModel<T>{

    public BeiFengModel(@NotNull EntityRendererProvider.Context context, boolean main) {
        super(context.bakeLayer(main?modelLayerLocation:armorLayerLocation));
    }

    public static @NotNull LayerDefinition createBodyLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 11).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 16).addBox(-1.5F, -1.0F, -5.5F, 3.0F, 1.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ear_r1 = left_ear.addOrReplaceChild("ear_r1", CubeListBuilder.create().texOffs(45, 54).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -7.0F, -1.0F, -0.7309F, 0.3586F, 0.1784F));

        PartDefinition ear_inner_r1 = left_ear.addOrReplaceChild("ear_inner_r1", CubeListBuilder.create().texOffs(20, -4).addBox(-0.82F, -4.3F, -4.5F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -9.0F, -2.0F, -1.7746F, 0.3175F, 0.1902F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ear_r2 = right_ear.addOrReplaceChild("ear_r2", CubeListBuilder.create().texOffs(45, 54).mirror().addBox(-1.0F, -6.0F, -1.0F, 2.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -7.0F, -1.0F, -0.7309F, -0.3586F, -0.1784F));

        PartDefinition ear_inner_r2 = right_ear.addOrReplaceChild("ear_inner_r2", CubeListBuilder.create().texOffs(20, -4).mirror().addBox(-1.18F, -4.1F, -3.8F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -9.0F, -2.0F, -1.8182F, -0.3175F, -0.1902F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 21).addBox(-0.5F, -17.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 19).addBox(-0.5F, -20.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 5).addBox(-0.5F, -23.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 2.0F));

        PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, -2.4F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 28).addBox(-2.5F, -2.1F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(-1.3F)), PartPose.offsetAndRotation(0.0F, 8.173F, 15.7001F, -0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, -2.2F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-2.5F, -2.2F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(-0.9F)), PartPose.offsetAndRotation(0.0F, 7.35F, 12.7306F, -0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r3 = tail.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(4, 6).addBox(-0.5F, -2.7F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-2.5F, -2.2F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, 5.7491F, 9.0243F, -0.48F, 0.0F, 0.0F));

        PartDefinition cube_r4 = tail.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, -2.9F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 33).addBox(-2.5F, -2.2F, -3.5F, 5.0F, 5.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 3.5562F, 5.0787F, -0.6109F, 0.0F, 0.0F));

        PartDefinition cube_r5 = tail.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(24, 2).addBox(-0.5F, -3.2F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 16).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 7.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(54, 0).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -22.0F, 0.0F));

        PartDefinition right_arm_elbow = right_arm.addOrReplaceChild("right_arm_elbow", CubeListBuilder.create().texOffs(57, 10).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -2.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(4, 0).addBox(-1.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 4.0F, 0.0F));

        PartDefinition right_arm_lower = right_arm_elbow.addOrReplaceChild("right_arm_lower", CubeListBuilder.create().texOffs(12, 53).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(50, 44).addBox(0.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -22.0F, 0.0F));

        PartDefinition left_arm_elbow = left_arm.addOrReplaceChild("left_arm_elbow", CubeListBuilder.create().texOffs(56, 33).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(4, 3).addBox(0.0F, 0.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 4.0F, 0.0F));

        PartDefinition left_arm_lower = left_arm_elbow.addOrReplaceChild("left_arm_lower", CubeListBuilder.create().texOffs(48, 23).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9F, -13.0F, 0.0F, 0.0F, 0.1309F, 0.1309F));

        PartDefinition right_leg_thigh_r1 = right_leg.addOrReplaceChild("right_leg_thigh_r1", CubeListBuilder.create().texOffs(41, 11).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1305F, -0.9914F, 0.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.1305F, 4.0086F, -1.0F, 0.0F, -0.0873F, 0.0F));

        PartDefinition right_leg_shin_r1 = right_leg_shin.addOrReplaceChild("right_leg_shin_r1", CubeListBuilder.create().texOffs(59, 54).addBox(-2.0F, -4.1F, -1.4F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));

        PartDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));

        PartDefinition right_leg__r1 = right_leg_.addOrReplaceChild("right_leg__r1", CubeListBuilder.create().texOffs(18, 40).addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition right_leg_foot = right_leg_.addOrReplaceChild("right_leg_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg_foot_r1 = right_leg_foot.addOrReplaceChild("right_leg_foot_r1", CubeListBuilder.create().texOffs(59, 61).addBox(-2.1F, 0.1F, -2.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9F, -13.0F, 0.0F, 0.0F, -0.1309F, -0.1309F));

        PartDefinition left_leg_thigh_r1 = left_leg.addOrReplaceChild("left_leg_thigh_r1", CubeListBuilder.create().texOffs(34, 44).addBox(-2.0F, 0.1F, -2.0F, 4.0F, 7.9F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1305F, -0.9914F, 0.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", CubeListBuilder.create(), PartPose.offsetAndRotation(0.1305F, 4.0086F, -1.0F, 0.0F, 0.0873F, 0.0F));

        PartDefinition left_leg_shin_r1 = left_leg_shin.addOrReplaceChild("left_leg_shin_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-2.0F, -4.1F, -1.4F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));

        PartDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));

        PartDefinition left_leg__r1 = left_leg_.addOrReplaceChild("left_leg__r1", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition left_leg_foot = left_leg_.addOrReplaceChild("left_leg_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_leg_foot_r1 = left_leg_foot.addOrReplaceChild("left_leg_foot_r1", CubeListBuilder.create().texOffs(57, 18).addBox(-1.9F, 0.1F, -2.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public static ModelLayerLocation modelLayerLocation = new ModelLayerLocation(AChanged.resourceLoc("bei_feng"),"main");

    public static @NotNull LayerDefinition createArmorLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F))
                .texOffs(52, 0).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.6F))
                .texOffs(55, 0).addBox(-1.5F, -1.0F, -5.5F, 3.0F, 1.0F, 1.5F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, -24.0F, 0.0F));
        head.addOrReplaceChild("ear_r1", CubeListBuilder.create().texOffs(45, 54).mirror().addBox(-1.0F, -6.0F, -1.0F, 2.0F, 8.0F, 5.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -7.0F, -1.0F, -0.7309F, -0.3586F, -0.1784F));
        head.addOrReplaceChild("ear_inner_r1", CubeListBuilder.create().texOffs(20, -4).mirror().addBox(-1.18F, -4.1F, -3.8F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -9.0F, -2.0F, -1.8182F, -0.3175F, -0.1902F));
        head.addOrReplaceChild("ear_r2", CubeListBuilder.create().texOffs(45, 54).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 9.0F, 5.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(3.0F, -7.0F, -1.0F, -0.7309F, 0.3586F, 0.1784F));
        head.addOrReplaceChild("ear_inner_r2", CubeListBuilder.create().texOffs(20, -4).addBox(-0.82F, -4.3F, -4.5F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(3.0F, -9.0F, -2.0F, -1.7746F, 0.3175F, 0.1902F));
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 2.0F));
        tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, -2.4F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.6F))
                .texOffs(42, 0).addBox(-2.5F, -2.1F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(0.0F, 8.173F, 15.7001F, -0.3491F, 0.0F, 0.0F));
        tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, -2.2F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.6F))
                .texOffs(32, 0).addBox(-2.5F, -2.2F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 7.35F, 12.7306F, -0.4363F, 0.0F, 0.0F));
        tail.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(4, 6).addBox(-0.5F, -2.7F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.6F))
                .texOffs(42, 0).addBox(-2.5F, -2.2F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 5.7491F, 9.0243F, -0.48F, 0.0F, 0.0F));
        tail.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, -2.9F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.6F))
                .texOffs(42, 0).addBox(-2.5F, -2.2F, -3.5F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0.0F, 3.5562F, 5.0787F, -0.6109F, 0.0F, 0.0F));
        tail.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(24, 2).addBox(-0.5F, -3.2F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.6F))
                .texOffs(40, 0).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 7.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7418F, 0.0F, 0.0F));
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(-4.0F, -22.0F, 0.0F));
        PartDefinition right_arm_elbow = right_arm.addOrReplaceChild("right_arm_elbow", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(-2.0F, 4.0F, 0.0F));
        right_arm_elbow.addOrReplaceChild("right_arm_lower", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(0.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offset(4.0F, -22.0F, 0.0F));
        PartDefinition left_arm_elbow = left_arm.addOrReplaceChild("left_arm_elbow", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(2.0F, 4.0F, 0.0F));
        left_arm_elbow.addOrReplaceChild("left_arm_lower", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9F, -13.0F, 0.0F, 0.0F, 0.1309F, 0.1309F));
        right_leg.addOrReplaceChild("right_leg_thigh_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(-0.1305F, -0.9914F, 0.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.1305F, 4.0086F, -1.0F, 0.0F, -0.0873F, 0.0F));
        right_leg_shin.addOrReplaceChild("right_leg_shin_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -4.5F, -1.4F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));
        PartDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));
        right_leg_.addOrReplaceChild("right_leg__r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9F, -13.0F, 0.0F, 0.0F, -0.1309F, -0.1309F));
        left_leg.addOrReplaceChild("left_leg_thigh_r1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.1F, -2.0F, 4.0F, 7.9F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(0.1305F, -0.9914F, 0.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", CubeListBuilder.create(), PartPose.offsetAndRotation(0.1305F, 4.0086F, -1.0F, 0.0F, 0.0873F, 0.0F));
        left_leg_shin.addOrReplaceChild("left_leg_shin_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -4.5F, -1.4F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));
        PartDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));
        left_leg_.addOrReplaceChild("left_leg__r1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
        PartDefinition right_foot = root.addOrReplaceChild("right_foot", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9F, -13.0F, 0.0F, 0.0F, 0.1309F, 0.1309F));
        PartDefinition right_leg_shin2 = right_foot.addOrReplaceChild("right_leg_shin2", CubeListBuilder.create(), PartPose.offset(-0.1305F, 4.0086F, -2.0F));
        PartDefinition right_leg_2 = right_leg_shin2.addOrReplaceChild("right_leg_2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));
        right_leg_2.addOrReplaceChild("right_leg__r2", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
        PartDefinition right_leg_foot = right_leg_2.addOrReplaceChild("right_leg_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        right_leg_foot.addOrReplaceChild("right_leg_foot_r1", CubeListBuilder.create().texOffs(0, 26).addBox(-2.1F, 0.1F, -1.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, -0.1309F));
        PartDefinition left_foot = root.addOrReplaceChild("left_foot", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9F, -13.0F, 0.0F, 0.0F, -0.1309F, -0.1309F));
        PartDefinition left_leg_shin2 = left_foot.addOrReplaceChild("left_leg_shin2", CubeListBuilder.create(), PartPose.offset(0.1305F, 4.0086F, -2.0F));
        PartDefinition left_leg_2 = left_leg_shin2.addOrReplaceChild("left_leg_2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));
        left_leg_2.addOrReplaceChild("left_leg__r2", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
        PartDefinition left_leg_foot = left_leg_2.addOrReplaceChild("left_leg_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        left_leg_foot.addOrReplaceChild("left_leg_foot_r1", CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-1.9F, 0.1F, -1.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.1309F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public static ModelLayerLocation armorLayerLocation = new ModelLayerLocation(AChanged.resourceLoc("bei_feng"),"armor");

    private final AnimationState state=new AnimationState();

    @Override
    public void setupAnim(@NotNull T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
        super.setupAnim(p_102618_, p_102619_, p_102620_, p_102621_, p_102622_, p_102623_);
        if(!state.isStarted()) state.start((int) p_102621_);
        animate(state,LatexWolfFemaleModel.ears,p_102621_);
    }
}