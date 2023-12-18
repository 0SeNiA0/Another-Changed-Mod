package net.zaharenko424.a_changed.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public class LatexWolfMaleModel<T extends LivingEntity> extends AbstractLatexEntityModel<T> {

    public LatexWolfMaleModel(@NotNull EntityRendererProvider.Context context, boolean main){
        super(context.bakeLayer(main?modelLayerLocation:armorLayerLocation));
    }

    public static @NotNull LayerDefinition createBodyLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(40, 12).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 55).addBox(-1.5F, -1.0F, -5.5F, 3.0F, 1.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ear_inner_r1 = left_ear.addOrReplaceChild("ear_inner_r1", CubeListBuilder.create().texOffs(54, 25).addBox(-0.72F, -4.5F, -0.2F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -9.0F, -2.0F, -1.1266F, 0.3822F, 0.3645F));

        PartDefinition ear_r1 = left_ear.addOrReplaceChild("ear_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -7.0F, -1.0F, -0.4284F, 0.3822F, 0.3645F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ear_inner_r2 = right_ear.addOrReplaceChild("ear_inner_r2", CubeListBuilder.create().texOffs(0, 55).addBox(-1.28F, -4.5F, -0.2F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -9.0F, -2.0F, -1.1266F, -0.3822F, -0.3645F));

        PartDefinition ear_r2 = right_ear.addOrReplaceChild("ear_r2", CubeListBuilder.create().texOffs(40, 20).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -7.0F, -1.0F, -0.4284F, -0.3822F, -0.3645F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 2.0F));

        PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(23, 7).addBox(-2.0F, -2.0F, 1.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 32).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 45).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -22.0F, 0.0F));

        PartDefinition right_arm_elbow = right_arm.addOrReplaceChild("right_arm_elbow", CubeListBuilder.create().texOffs(49, 12).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 4.0F, 0.0F));

        PartDefinition right_arm_lower = right_arm_elbow.addOrReplaceChild("right_arm_lower", CubeListBuilder.create().texOffs(44, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 47).addBox(0.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -22.0F, 0.0F));

        PartDefinition left_arm_elbow = left_arm.addOrReplaceChild("left_arm_elbow", CubeListBuilder.create().texOffs(48, 51).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 4.0F, 0.0F));

        PartDefinition left_arm_lower = left_arm_elbow.addOrReplaceChild("left_arm_lower", CubeListBuilder.create().texOffs(16, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9F, -13.0F, 0.0F, 0.0F, 0.1309F, 0.1309F));

        PartDefinition right_leg_thigh_r1 = right_leg.addOrReplaceChild("right_leg_thigh_r1", CubeListBuilder.create().texOffs(16, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1305F, -0.9914F, 0.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.1305F, 4.0086F, -1.0F, 0.0F, -0.0873F, 0.0F));

        PartDefinition right_leg_shin_r1 = right_leg_shin.addOrReplaceChild("right_leg_shin_r1", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -4.1F, -1.4F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));

        PartDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));

        PartDefinition right_leg__r1 = right_leg_.addOrReplaceChild("right_leg__r1", CubeListBuilder.create().texOffs(24, 20).addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition right_leg_foot = right_leg_.addOrReplaceChild("right_leg_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg_foot_r1 = right_leg_foot.addOrReplaceChild("right_leg_foot_r1", CubeListBuilder.create().texOffs(54, 7).addBox(-2.1F, 0.1F, -2.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9F, -13.0F, 0.0F, 0.0F, -0.1309F, -0.1309F));

        PartDefinition left_leg_thigh_r1 = left_leg.addOrReplaceChild("left_leg_thigh_r1", CubeListBuilder.create().texOffs(32, 33).addBox(-2.0F, 0.1F, -2.0F, 4.0F, 7.9F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1305F, -0.9914F, 0.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", CubeListBuilder.create(), PartPose.offsetAndRotation(0.1305F, 4.0086F, -1.0F, 0.0F, 0.0873F, 0.0F));

        PartDefinition left_leg_shin_r1 = left_leg_shin.addOrReplaceChild("left_leg_shin_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -4.1F, -1.4F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));

        PartDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));

        PartDefinition left_leg__r1 = left_leg_.addOrReplaceChild("left_leg__r1", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition left_leg_foot = left_leg_.addOrReplaceChild("left_leg_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_leg_foot_r1 = left_leg_foot.addOrReplaceChild("left_leg_foot_r1", CubeListBuilder.create().texOffs(49, 20).addBox(-1.9F, 0.1F, -2.8F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public static @NotNull ModelLayerLocation modelLayerLocation=new ModelLayerLocation(AChanged.resourceLoc("latex_wolf_male"),"main");

    public static @NotNull LayerDefinition createArmorLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, -24.0F, 0.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(-4.0F, -22.0F, 0.0F));
        PartDefinition right_arm_elbow = right_arm.addOrReplaceChild("right_arm_elbow", CubeListBuilder.create().texOffs(40, 8).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(-2.0F, 4.0F, 0.0F));
        right_arm_elbow.addOrReplaceChild("right_arm_lower", CubeListBuilder.create().texOffs(40, 6).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(0.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offset(4.0F, -22.0F, 0.0F));
        PartDefinition left_arm_elbow = left_arm.addOrReplaceChild("left_arm_elbow", CubeListBuilder.create().texOffs(40, 8).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(2.0F, 4.0F, 0.0F));
        left_arm_elbow.addOrReplaceChild("left_arm_lower", CubeListBuilder.create().texOffs(40, 6).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9F, -13.0F, 0.0F, 0.0F, 0.1309F, 0.1309F));
        right_leg.addOrReplaceChild("right_leg_thigh_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(-0.1305F, -0.9914F, 0.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", CubeListBuilder.create(), PartPose.offset(-0.1305F, 4.0086F, -1.0F));
        right_leg_shin.addOrReplaceChild("right_leg_shin_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -4.1F, -1.4F, 4.0F, 6.4F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));
        PartDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));
        right_leg_.addOrReplaceChild("right_leg__r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -0.9487F, -1.7832F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9F, -13.0F, 0.0F, 0.0F, -0.1309F, -0.1309F));
        left_leg.addOrReplaceChild("left_leg_thigh_r1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.1F, -2.0F, 4.0F, 7.9F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(0.1305F, -0.9914F, 0.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", CubeListBuilder.create(), PartPose.offset(0.1305F, 4.0086F, -1.0F));
        left_leg_shin.addOrReplaceChild("left_leg_shin_r1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, -4.1F, -1.4F, 4.0F, 6.4F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.9809F, 0.0F, 0.0F));
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

    public static @NotNull ModelLayerLocation armorLayerLocation=new ModelLayerLocation(AChanged.resourceLoc("latex_wolf_male"),"armor");

    private final AnimationState state=new AnimationState();

    @Override
    public void setupAnim(@NotNull T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
        super.setupAnim(p_102618_, p_102619_, p_102620_, p_102621_, p_102622_, p_102623_);
        if(!state.isStarted()) state.start((int) p_102621_);
        animate(state,LatexWolfFemaleModel.ears,p_102621_);
    }
}