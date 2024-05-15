package net.zaharenko424.a_changed.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;
import net.zaharenko424.a_changed.client.cmrs.animation.Animations;
import net.zaharenko424.a_changed.client.cmrs.geom.CubeUV;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupBuilder;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LatexWolfFemaleModel<E extends LivingEntity> extends CustomHumanoidModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(AChanged.resourceLoc("latex_wolf_female"),"main");

    public LatexWolfFemaleModel(ResourceLocation texture) {
        super(bodyLayer, texture);
    }

    AnimationState ears = new AnimationState();
    AnimationState tail = new AnimationState();

    @Override
    public void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch);
        if(!ears.isStarted()) ears.start((int) ageInTicks);
        if(!tail.isStarted()) tail.start((int) ageInTicks);
        animate(ears, Animations.EAR_ANIM,ageInTicks);
        animate(tail,Animations.TAIL_DEF,ageInTicks);
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new CubeUV().east(16, 20, 8, 12).south(24, 8, 16, 0).west(24, 16, 16, 8).up(24, 24, 16, 16).north(8, 20, 0, 12).down(8, 20, 0, 28))
                .addBox(-2f, 1f, -6f, 4, 2, 2, new CubeUV().east(25, 46, 23, 44).south(49, 51, 45, 49).west(47, 26, 45, 24).up(53, 49, 49, 47).north(53, 24, 49, 22).down(53, 49, 49, 51))
                .addBox(-1.5f, 0f, -5.5f, 3, 1, 1.5f, new CubeUV().east(36.5f, 53, 35, 52).south(21, 53, 18, 52).west(53.5f, 37, 52, 36).up(28, 37.5f, 25, 36).north(40, 34, 37, 33).down(15, 50, 12, 51.5f)), PartPose.offset(0, 24, 0));
        GroupDefinition right_ear = head.addOrReplaceChild("right_ear", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2, 7, 5, new CubeUV().east(37, 7, 32, 0).south(31, 53, 29, 46).west(13, 39, 8, 32).up(51, 11, 49, 6).north(29, 53, 27, 46).down(10, 49, 8, 54)), PartPose.offsetAndRotation(3, 7, -1, 0.4276f, 0.384f, -0.3665f));
        right_ear.addOrReplaceChild("right_ear_inner", GroupBuilder.create()
                .addBox(-1f, 3.3f, -2.7f, 2, 1, 4, new Vector3f(-0.01f), new CubeUV().east(36, 12, 32, 11).south(23, 53, 21, 52).west(41, 7, 37, 6).up(2, 54, 0, 50).north(47, 27, 45, 26).down(4, 50, 2, 54)), PartPose.rotation(0.7854f, 0, 0));
        GroupDefinition left_ear = head.addOrReplaceChild("left_ear", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2, 7, 5, new CubeUV().east(37, 27, 32, 20).south(35, 53, 33, 46).west(37, 34, 32, 27).up(12, 54, 10, 49).north(33, 53, 31, 46).down(51, 11, 49, 16)), PartPose.offsetAndRotation(-3, 7, -1, 0.4276f, -0.384f, 0.3665f));
        left_ear.addOrReplaceChild("left_ear_inner", GroupBuilder.create()
                .addBox(-1f, 3.3f, -2.7f, 2, 1, 4, new Vector3f(-0.01f), new CubeUV().east(39, 52, 35, 51).south(27, 53, 25, 52).west(49, 52, 45, 51).up(6, 54, 4, 50).north(25, 53, 23, 52).down(8, 50, 6, 54)), PartPose.rotation(0.7854f, 0, 0));
        head.addOrReplaceChild("armor_head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new Vector3f(0.6f), new CubeUV().east(8, 16, 0, 8).south(32, 16, 24, 8).west(24, 16, 16, 8).up(16, 8, 8, 0).north(16, 16, 8, 8).down(24, 0, 16, 8)).armor());
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4.1f, 0f, -2.1f, 8.2f, 12, 4.2f, new CubeUV().east(12, 32, 8, 20).south(16, 12, 8, 0).west(16, 32, 12, 20).up(40, 11, 32, 7).north(8, 12, 0, 0).down(40, 34, 32, 38)), PartPose.offset(0, 12, 0));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addBox(-1.5f, -1.5f, -2f, 3, 3, 6, new CubeUV().east(47, 30, 41, 27).south(52, 47, 49, 44).west(47, 33, 41, 30).up(44, 44, 41, 38).north(52, 44, 49, 41).down(15, 44, 12, 50)), PartPose.offsetAndRotation(0, 0, 2, 0.48f, 0, 0));
        GroupDefinition tail_0 = tail.addOrReplaceChild("tail_0", GroupBuilder.create()
                .addBox(-2f, -2f, -1f, 4, 4, 9, new CubeUV().east(37, 16, 28, 12).south(23, 48, 19, 44).west(37, 20, 28, 16).up(32, 29, 28, 20).north(19, 48, 15, 44).down(32, 29, 28, 38))
                .addBox(-1f, -1f, 7f, 2, 2, 2, new CubeUV().east(51, 53, 49, 51).south(53, 53, 51, 51).west(14, 54, 12, 52).up(16, 54, 14, 52).north(37, 48, 35, 46).down(18, 52, 16, 54)), PartPose.offsetAndRotation(0, 0, 4, 0.1309f, 0, 0));
        tail_0.addOrReplaceChild("armor_tail_0", GroupBuilder.create()
                .addBox(-2f, -2f, -1f, 4, 4, 9, new Vector3f(0.6f), new CubeUV().east(8, 29, 4, 23).south(8, 29, 4, 25).west(8, 29, 4, 23).up(8, 29, 4, 23).north(8, 29, 4, 25).down(8, 23, 4, 29))
                .addBox(-1f, -1f, 7f, 2, 2, 2, new Vector3f(0.6f), new CubeUV().east(8, 29, 4, 25).south(8, 25, 4, 29).west(8, 29, 4, 25).up(8, 29, 4, 25).down(8, 25, 4, 29)).armor());
        tail.addOrReplaceChild("armor_tail", GroupBuilder.create()
                .addBox(-1.5f, -1.5f, -2f, 3, 3, 6, new Vector3f(0.6f), new CubeUV().east(8, 29, 4, 24).west(8, 29, 4, 24).up(8, 29, 4, 24).down(8, 24, 4, 29)).armor());
        GroupDefinition armor_body = body.addOrReplaceChild("armor_body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new Vector3f(0.6f), new CubeUV().east(32, 32, 28, 20).south(40, 32, 32, 20).west(20, 32, 16, 20).up(40, 11, 32, 7).north(28, 32, 20, 20).down(40, 34, 32, 38)).armor());
        armor_body.addOrReplaceChild("cube", GroupBuilder.create()
                .addBox(-1.7f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new Vector3f(0.6f), new CubeUV().east(38, 28, 35, 24.5f).south(22.5f, 51.5f, 19, 48).west(37, 28, 34, 24.5f).up(52.5f, 3, 49, 0).north(12, 20, 8, 16).down(12, 16, 8, 20))
                .addBox(2.2f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new Vector3f(0.6f), new CubeUV().east(37, 28, 34, 24.5f).south(50.5f, 31.5f, 47, 28).west(37, 28, 34, 24.5f).up(38.5f, 51, 35, 48).north(12, 20, 8, 16).down(12, 16, 8, 20)), PartPose.offsetAndRotation(-2, 8.3f, -2, 0.48f, 0, 0));
        body.addOrReplaceChild("cube_i0", GroupBuilder.create()
                .addBox(-1.8f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new CubeUV().east(16, 35.5f, 13, 32).south(50.5f, 31.5f, 47, 28).west(51, 35.5f, 48, 32).up(38.5f, 51, 35, 48).north(50.5f, 27.5f, 47, 24).down(51.5f, 36, 48, 39))
                .addBox(-5.7f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new CubeUV().east(42, 51.5f, 39, 48).south(22.5f, 51.5f, 19, 48).west(45, 51.5f, 42, 48).up(52.5f, 3, 49, 0).north(18.5f, 51.5f, 15, 48).down(52.5f, 3, 49, 6)), PartPose.offsetAndRotation(2, 8.3f, -2, 0.48f, 0, 0));
        GroupDefinition right_arm = root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new CubeUV().east(28, 24, 24, 12).south(20, 36, 16, 24).west(24, 36, 20, 24).up(48, 37, 44, 33).north(28, 12, 24, 0).down(41, 44, 37, 48)), PartPose.offset(4, 22, 0));
        right_arm.addOrReplaceChild("armor_right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().east(44, 32, 40, 20).south(56, 32, 52, 20).west(52, 32, 48, 20).up(48, 20, 44, 16).north(48, 32, 44, 20)).armor());
        GroupDefinition left_arm = root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new CubeUV().east(4, 40, 0, 28).south(32, 12, 28, 0).west(8, 40, 4, 28).up(48, 41, 44, 37).north(28, 36, 24, 24).down(45, 44, 41, 48)), PartPose.offset(-4, 22, 0));
        left_arm.addOrReplaceChild("armor_left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().east(52, 32, 48, 20).south(48, 32, 44, 20).west(44, 32, 40, 20).up(44, 20, 48, 16).north(56, 32, 52, 20)).armor());
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2, 13, 0, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create());
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new CubeUV().east(53, 41, 48, 39).south(55, 16, 51, 14).west(54, 18, 49, 16).up(44, 38, 40, 33).north(55, 14, 51, 12).down(45, 12, 41, 17)), PartPose.rotation(0, 0, -0.1309f));
        right_foot.addOrReplaceChild("armor_right_foot", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().east(12, 20, 8, 16).south(12, 20, 8, 16).west(12, 20, 8, 16).up(12, 20, 8, 16).north(12, 20, 8, 16).down(8, 20, 12, 16)).armor(), PartPose.rotation(0, 0, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().east(41, 33, 37, 27).south(41, 44, 37, 38).west(12, 45, 8, 39).up(49, 16, 45, 12).north(41, 6, 37, 0).down(49, 16, 45, 20)), PartPose.rotation(0.1309f, 0, 0));
        right_leg_.addOrReplaceChild("armor_right_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().east(8, 32, 4, 26).south(4, 32, 0, 26).west(8, 32, 4, 26).up(49, 16, 45, 12).north(4, 32, 0, 26).down(49, 16, 45, 20)).armor(), PartPose.rotation(0.1309f, 0, 0));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().east(55, 8, 51, 6).south(55, 10, 51, 8).west(55, 12, 51, 10).up(12, 49, 8, 45).north(27, 52, 23, 50).down(49, 8, 45, 12)), PartPose.rotation(0.3491f, 0, 0));
        right_leg_shin.addOrReplaceChild("armor_right_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().east(8, 25, 4, 29).south(8, 25, 4, 29).west(8, 25, 4, 29).up(8, 25, 4, 29)).armor(), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().east(21, 44, 17, 36).south(25, 44, 21, 36).west(41, 19, 37, 11).up(49, 4, 45, 0).north(17, 44, 13, 36).down(49, 4, 45, 8)), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("armor_right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().east(12, 29, 8, 20).south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(8, 29, 4, 20).down(8, 25, 4, 29)).armor(), PartPose.rotation(0.3491f, 0, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2, 13, 0, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create());
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new CubeUV().east(54, 20, 49, 18).south(55, 36, 51, 34).west(54, 22, 49, 20).up(45, 22, 41, 17).north(55, 34, 51, 32).down(45, 22, 41, 27)), PartPose.rotation(0, 0, 0.1309f));
        left_foot.addOrReplaceChild("armor_left_foot", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().east(12, 20, 8, 16).south(12, 20, 8, 16).west(12, 16, 8, 20).up(12, 20, 8, 16).north(8, 20, 12, 16).down(8, 16, 12, 20)).armor(), PartPose.rotation(0, 0, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().east(8, 46, 4, 40).south(45, 6, 41, 0).west(45, 12, 41, 6).up(8, 50, 4, 46).north(4, 46, 0, 40).down(27, 46, 23, 50)), PartPose.rotation(0.1309f, 0, 0));
        left_leg_.addOrReplaceChild("armor_left_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().east(8, 32, 4, 26).south(4, 32, 0, 26).west(4, 32, 8, 26).up(8, 50, 4, 46).north(0, 32, 4, 26).down(27, 46, 23, 50)).armor(), PartPose.rotation(0.1309f, 0, 0));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().east(55, 28, 51, 26).south(55, 30, 51, 28).west(55, 32, 51, 30).up(49, 49, 45, 45).north(55, 26, 51, 24).down(4, 46, 0, 50)), PartPose.rotation(0.3491f, 0, 0));
        left_leg_shin.addOrReplaceChild("armor_left_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().east(8, 25, 4, 29).south(8, 25, 4, 29).west(4, 25, 8, 29).up(8, 25, 4, 29)).armor(), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().east(29, 46, 25, 38).south(33, 46, 29, 38).west(37, 46, 33, 38).up(49, 24, 45, 20).north(41, 27, 37, 19).down(49, 41, 45, 45)), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("armor_left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().east(12, 29, 8, 20).south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(4, 29, 8, 20).down(4, 25, 8, 29)).armor(), PartPose.rotation(0.3491f, 0, 0));

        return ModelDefinition.create(modelBuilder, 128, 128, 2);
    }
}