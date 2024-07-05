package net.zaharenko424.a_changed.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.animation.Animations;
import net.zaharenko424.a_changed.client.cmrs.animation.KeyframeAnimator;
import net.zaharenko424.a_changed.client.cmrs.geom.CubeUV;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupBuilder;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import net.zaharenko424.a_changed.client.cmrs.model.CustomHumanoidModel;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BeiFengModel<E extends LivingEntity> extends CustomHumanoidModel<E> {

    public static ModelLayerLocation bodyLayer = new ModelLayerLocation(TransfurRegistry.BEI_FENG_TF.getId(),"main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/bei_feng");

    public BeiFengModel() {
        super(bodyLayer, TEXTURE);
    }

    AnimationState ears = new AnimationState();
    AnimationState tail = new AnimationState();

    @Override
    public void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch);
        if(!ears.isStarted()) ears.start((int) ageInTicks);
        if(!tail.isStarted()) tail.start((int) ageInTicks);
        KeyframeAnimator.animate(ears, root(), Animations.EAR_ANIM, ageInTicks);
        KeyframeAnimator.animate(tail, root(), Animations.TAIL_DRAGON, ageInTicks);
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new CubeUV().east(16, 20, 8, 12).south(24, 8, 16, 0).west(24, 16, 16, 8).up(24, 24, 16, 16).north(8, 20, 0, 12).down(8, 20, 0, 28))
                .addBox(-2f, 1f, -6f, 4, 2, 2, new CubeUV().east(42, 14, 40, 12).south(51, 52, 47, 50).west(55, 29, 53, 27).up(55, 18, 51, 16).north(45, 8, 41, 6).down(55, 18, 51, 20))
                .addBox(-1.5f, 0f, -5.5f, 3, 1, 1.5f, new CubeUV().east(54.5f, 39, 53, 38).south(41, 28, 38, 27).west(54.5f, 40, 53, 39).up(56, 6.5f, 53, 5).north(40, 9, 37, 8).down(29, 53, 26, 54.5f)), PartPose.offset(0, 24, 0));
        GroupDefinition right_ear = head.addOrReplaceChild("right_ear", GroupBuilder.create(), PartPose.offset(3, 7, -1));
        right_ear.addOrReplaceChild("ear", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2, 9, 5, new CubeUV().east(33, 21, 28, 12).south(40, 53, 38, 44).west(33, 30, 28, 21).up(6, 55, 4, 50).north(38, 53, 36, 44).down(8, 50, 6, 55)), PartPose.rotation(0.7295f, 0.3578f, -0.1745f));
        right_ear.addOrReplaceChild("ear_inner", GroupBuilder.create()
                .addBox(-1f, 2.2f, -5.8f, 2, 1, 8, new Vector3f(-0.01f), new CubeUV().east(55, 31, 53, 29).south(33, 55, 31, 53).west(55, 33, 53, 31).up(35, 55, 33, 53).north(31, 55, 29, 53).down(55, 33, 53, 35)), PartPose.rotation(1.7331f, 0.3578f, -0.1745f));
        GroupDefinition left_ear = head.addOrReplaceChild("left_ear", GroupBuilder.create(), PartPose.offset(-3, 7, -1));
        left_ear.addOrReplaceChild("ear_i0", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2, 9, 5, new CubeUV().east(33, 39, 28, 30).south(12, 54, 10, 45).west(37, 9, 32, 0).up(52, 12, 50, 7).north(10, 54, 8, 45).down(14, 50, 12, 55)), PartPose.rotation(0.7295f, -0.3578f, 0.1745f));
        left_ear.addOrReplaceChild("ear_inner_i0", GroupBuilder.create()
                .addBox(-1f, 2.2f, -5.8f, 2, 1, 8, new Vector3f(-0.01f), new CubeUV().east(53, 33, 55, 31).south(31, 55, 33, 53).west(53, 31, 55, 29).up(33, 55, 35, 53).north(29, 55, 31, 53).down(53, 33, 55, 35)), PartPose.rotation(1.7331f, -0.3578f, 0.1745f));
        head.addOrReplaceChild("armor_head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new Vector3f(0.6f), new CubeUV().east(8, 16, 0, 8).south(32, 16, 24, 8).west(24, 16, 16, 8).up(16, 8, 8, 0).north(16, 16, 8, 8).down(24, 0, 16, 8)).armor());
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new CubeUV().east(12, 32, 8, 20).south(16, 12, 8, 0).west(16, 32, 12, 20).up(41, 32, 33, 28).north(8, 12, 0, 0).down(41, 32, 33, 36))
                .addBox(-0.5f, 10f, 2f, 1, 1, 1.5f, new CubeUV().east(41.5f, 54, 40, 53).south(41, 45, 40, 44).west(54.5f, 41, 53, 40).up(20, 51.5f, 19, 50).north(16, 36, 15, 35).down(54, 41, 53, 42.5f))
                .addBox(-0.5f, 7f, 2f, 1, 1, 1.5f, new CubeUV().east(43.5f, 54, 42, 53).south(55, 3, 54, 2).west(54.5f, 44, 53, 43).up(45, 54.5f, 44, 53).north(45, 46, 44, 45).down(46, 53, 45, 54.5f))
                .addBox(-0.5f, 4f, 2f, 1, 1, 1.5f, new CubeUV().east(55.5f, 1, 54, 0).south(17, 55, 16, 54).west(55.5f, 2, 54, 1).up(47, 54.5f, 46, 53).north(16, 55, 15, 54).down(9, 54, 8, 55.5f)), PartPose.offset(0, 12, 0));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addBox(-2.4f, -2.4f, -3f, 4.8f, 4.8f, 7, new CubeUV().east(15, 37, 8, 32).south(32, 44, 27, 39).west(40, 14, 33, 9).up(38, 21, 33, 14).north(43, 27, 38, 22).down(38, 21, 33, 28))
                .addBox(-0.5f, 2.5f, 0f, 1, 1, 1, new CubeUV().east(36, 55, 35, 54).south(37, 55, 36, 54).west(38, 55, 37, 54).up(39, 55, 38, 54).north(18, 55, 17, 54).down(40, 54, 39, 55)), PartPose.offsetAndRotation(0, 0, 2, 0.7418f, 0, 0));
        GroupDefinition tail_0 = tail.addOrReplaceChild("tail_0", GroupBuilder.create()
                .addBox(-2.1f, -1.8f, -0.5f, 4.2f, 4.2f, 6, new CubeUV().east(6, 44, 0, 40).south(49, 42, 45, 38).west(46, 12, 40, 8).up(45, 6, 41, 0).north(49, 38, 45, 34).down(45, 27, 41, 33))
                .addBox(-0.5f, 2.4f, 2f, 1, 1, 1, new CubeUV().east(42, 55, 41, 54).south(55, 42, 54, 41).west(43, 55, 42, 54).up(55, 43, 54, 42).north(41, 55, 40, 54).down(44, 54, 43, 55)), PartPose.offsetAndRotation(0, 0, 4, -0.1309f, 0, 0));
        GroupDefinition tail_1 = tail_0.addOrReplaceChild("tail_1", GroupBuilder.create()
                .addBox(-1.8f, -1.3f, 0f, 3.6f, 3.7f, 5, new CubeUV().east(50, 3.5f, 45, 0).south(52.5f, 43.5f, 49, 40).west(50, 7.5f, 45, 4).up(48.5f, 29, 45, 24).north(43.5f, 52.5f, 40, 49).down(48.5f, 29, 45, 34))
                .addBox(-0.5f, 2.4f, 2f, 1, 1, 1, new CubeUV().east(55, 46, 54, 45).south(48, 55, 47, 54).west(49, 55, 48, 54).up(50, 55, 49, 54).north(55, 45, 54, 44).down(51, 54, 50, 55)), PartPose.offsetAndRotation(0, 0, 5, -0.1309f, 0, 0));
        GroupDefinition tail_2 = tail_1.addOrReplaceChild("tail_2", GroupBuilder.create()
                .addBox(-1.5f, -1.3f, -0.5f, 3, 3.2f, 4.5f, new CubeUV().east(4, 53, 0, 50).south(47, 53, 44, 50).west(54, 3, 50, 0).up(15, 41, 12, 37).north(15, 44, 12, 41).down(53, 3, 50, 7))
                .addBox(-0.5f, 1.9f, 1.5f, 1, 1, 1, new CubeUV().east(53, 55, 52, 54).south(54, 55, 53, 54).west(55, 55, 54, 54).up(1, 56, 0, 55).north(52, 55, 51, 54).down(2, 55, 1, 56)), PartPose.offsetAndRotation(0, 0.5f, 5, -0.0873f, 0, 0));
        GroupDefinition tail_3 = tail_2.addOrReplaceChild("tail_3", GroupBuilder.create()
                .addBox(-1f, -1f, 0f, 2, 2.4f, 3, new CubeUV().east(21, 55.5f, 18, 53).south(55, 26.5f, 53, 24).west(24, 55.5f, 21, 53).up(8, 43, 6, 40).north(26, 55.5f, 24, 53).down(45, 24, 43, 27))
                .addBox(-0.5f, 1.4f, 1f, 1, 1.5f, 1, new CubeUV().east(11, 55.5f, 10, 54).south(12, 55.5f, 11, 54).west(15, 55.5f, 14, 54).up(3, 56, 2, 55).north(10, 55.5f, 9, 54).down(56, 2, 55, 3)), PartPose.offsetAndRotation(0, 0.5f, 4, -0.0873f, 0, 0));
        tail_3.addOrReplaceChild("armor_tail_3", GroupBuilder.create()
                .addBox(-1f, -1f, 0f, 2, 2.4f, 3, new Vector3f(0.6f), new CubeUV().east(8, 29, 4, 25).south(8, 29, 4, 25).west(8, 29, 4, 25).up(8, 29, 4, 25).down(8, 25, 4, 29)).armor());
        tail_2.addOrReplaceChild("armor_tail_2", GroupBuilder.create()
                .addBox(-1.5f, -1.3f, -0.5f, 3, 3.2f, 4.5f, new Vector3f(0.6f), new CubeUV().east(8, 29, 4, 25).south(8, 29, 4, 25).west(8, 29, 4, 25).up(8, 29, 4, 25).down(8, 25, 4, 29)).armor());
        tail_1.addOrReplaceChild("armor_tail_1", GroupBuilder.create()
                .addBox(-1.8f, -1.3f, 0f, 3.6f, 3.7f, 5, new Vector3f(0.6f), new CubeUV().east(8, 29, 4, 24).south(8, 29, 4, 25).west(8, 29, 4, 24).up(8, 29, 4, 24).down(8, 24, 4, 29)).armor());
        tail_0.addOrReplaceChild("armor_tail_0", GroupBuilder.create()
                .addBox(-2.1f, -1.8f, -0.5f, 4.2f, 4.2f, 6, new Vector3f(0.6f), new CubeUV().east(8, 29, 4, 24).south(8, 29, 4, 25).west(8, 29, 4, 24).up(8, 29, 4, 24).down(8, 24, 4, 29)).armor());
        tail.addOrReplaceChild("armor_tail", GroupBuilder.create()
                .addBox(-2.4f, -2.4f, -3f, 4.8f, 4.8f, 7, new Vector3f(0.6f), new CubeUV().east(8, 29, 4, 23).south(8, 29, 4, 25).west(8, 29, 4, 23).up(8, 29, 4, 23).down(8, 23, 4, 29)).armor());
        body.addOrReplaceChild("armor_body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new Vector3f(0.6f), new CubeUV().east(32, 32, 28, 20).south(40, 32, 32, 20).west(20, 32, 16, 20).up(40, 11, 32, 7).north(28, 32, 20, 20).down(40, 34, 32, 38)).armor());
        GroupDefinition right_arm = root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new CubeUV().east(28, 24, 24, 12).south(20, 36, 16, 24).west(24, 36, 20, 24).up(44, 49, 40, 45).north(28, 12, 24, 0).down(49, 42, 45, 46))
                .addBox(1.5f, -4f, 1f, 1, 1, 2.5f, new CubeUV().east(54.5f, 12, 52, 11).south(5, 56, 4, 55).west(37.5f, 54, 35, 53).up(33, 11.5f, 32, 9).north(4, 56, 3, 55).down(16, 32, 15, 34.5f))
                .addBox(1.5f, -5f, 1f, 1, 1, 1.8f, new CubeUV().east(8, 44, 6, 43).south(7, 56, 6, 55).west(20, 53, 18, 52).up(43, 20, 42, 18).north(6, 56, 5, 55).down(43, 20, 42, 22)), PartPose.offset(4, 22, 0));
        right_arm.addOrReplaceChild("armor_right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().east(44, 32, 40, 20).south(56, 32, 52, 20).west(52, 32, 48, 20).up(48, 20, 44, 16).north(48, 32, 44, 20)).armor());
        GroupDefinition left_arm = root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new CubeUV().east(4, 40, 0, 28).south(32, 12, 28, 0).west(8, 40, 4, 28).up(50, 12, 46, 8).north(28, 36, 24, 24).down(50, 12, 46, 16))
                .addBox(-2.5f, -5f, 1f, 1, 1, 1.8f, new CubeUV().east(55, 38, 53, 37).south(56, 12, 55, 11).west(40, 54, 38, 53).up(33, 44, 32, 42).north(8, 56, 7, 55).down(47, 16, 46, 18))
                .addBox(-2.5f, -4f, 1f, 1, 1, 2.5f, new CubeUV().east(55.5f, 36, 53, 35).south(56, 13, 55, 12).west(55.5f, 37, 53, 36).up(28, 38.5f, 27, 36).north(13, 56, 12, 55).down(33, 39, 32, 41.5f)), PartPose.offset(-4, 22, 0));
        left_arm.addOrReplaceChild("armor_left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().east(52, 32, 48, 20).south(48, 32, 44, 20).west(40, 32, 44, 20).up(44, 20, 48, 16).north(56, 32, 52, 20)).armor());
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2, 13, 0, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create());
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new CubeUV().east(54, 46, 49, 44).south(18, 54, 14, 52).west(55, 14, 50, 12).up(24, 49, 20, 44).north(56, 11, 52, 9).down(28, 44, 24, 49)), PartPose.rotation(0, 0, -0.1309f));
        right_foot.addOrReplaceChild("armor_right_foot", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().east(12, 20, 8, 16).south(12, 20, 8, 16).west(12, 20, 8, 16).up(12, 20, 8, 16).north(12, 20, 8, 16).down(12, 16, 8, 20)).armor(), PartPose.rotation(0, 0, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().east(45, 45, 41, 39).south(46, 18, 42, 12).west(47, 24, 43, 18).up(24, 53, 20, 49).north(45, 39, 41, 33).down(28, 49, 24, 53)), PartPose.rotation(0.1309f, 0, 0));
        right_leg_.addOrReplaceChild("armor_right_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().east(4, 32, 0, 26).south(8, 32, 4, 26).west(4, 32, 0, 26).up(49, 16, 45, 12).north(8, 32, 4, 26).down(49, 16, 45, 20)).armor(), PartPose.rotation(0.1309f, 0, 0));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().east(55, 24, 51, 22).south(55, 52, 51, 50).west(56, 9, 52, 7).up(51, 24, 47, 20).north(55, 22, 51, 20).down(52, 46, 48, 50)), PartPose.rotation(0.3491f, 0, 0));
        right_leg_shin.addOrReplaceChild("armor_right_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().east(8, 29, 4, 25).south(8, 29, 4, 25).west(8, 29, 4, 25).up(8, 29, 4, 25).down(49, 8, 45, 12)).armor(), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().east(23, 44, 19, 36).south(27, 44, 23, 36).west(37, 44, 33, 36).up(48, 50, 44, 46).north(19, 44, 15, 36).down(51, 16, 47, 20)), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("armor_right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().east(12, 29, 8, 20).south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(8, 29, 4, 20).down(8, 25, 4, 29)).armor(), PartPose.rotation(0.3491f, 0, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2, 13, 0, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create());
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new CubeUV().east(19, 52, 14, 50).south(57, 5, 53, 3).west(55, 16, 50, 14).up(32, 49, 28, 44).north(4, 55, 0, 53).down(36, 44, 32, 49)), PartPose.rotation(0, 0, 0.1309f));
        left_foot.addOrReplaceChild("armor_left_foot", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().east(12, 20, 8, 16).south(12, 20, 8, 16).west(12, 16, 8, 20).up(12, 20, 8, 16).north(8, 20, 12, 16).down(8, 16, 12, 20)).armor(), PartPose.rotation(0, 0, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().east(8, 50, 4, 44).south(16, 50, 12, 44).west(20, 50, 16, 44).up(53, 36, 49, 32).north(4, 50, 0, 44).down(53, 36, 49, 40)), PartPose.rotation(0.1309f, 0, 0));
        left_leg_.addOrReplaceChild("armor_left_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().east(4, 32, 0, 26).south(4, 32, 0, 26).west(0, 32, 4, 26).up(8, 50, 4, 46).north(4, 32, 8, 26).down(27, 46, 23, 50)).armor(), PartPose.rotation(0.1309f, 0, 0));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().east(51, 54, 47, 52).south(56, 50, 52, 48).west(55, 54, 51, 52).up(53, 32, 49, 28).north(56, 48, 52, 46).down(36, 49, 32, 53)), PartPose.rotation(0.3491f, 0, 0));
        left_leg_shin.addOrReplaceChild("armor_left_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().east(8, 29, 4, 25).south(8, 29, 4, 25).west(4, 29, 8, 25).up(8, 29, 4, 25).north(53, 26, 49, 24).down(4, 46, 0, 50)).armor(), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().east(12, 45, 8, 37).south(41, 44, 37, 36).west(42, 22, 38, 14).up(53, 28, 49, 24).north(41, 8, 37, 0).down(32, 49, 28, 53)), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("armor_left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().east(12, 29, 8, 20).south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(4, 29, 8, 20).down(4, 25, 8, 29)).armor(), PartPose.rotation(0.3491f, 0, 0));

        return ModelDefinition.create(modelBuilder, 128, 128, 2);
    }
}