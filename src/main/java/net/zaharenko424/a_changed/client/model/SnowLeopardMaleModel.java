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

public class SnowLeopardMaleModel<E extends LivingEntity> extends CustomHumanoidModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(TransfurRegistry.SNOW_LEOPARD_M_TF.getId(), "main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/snow_leopard_male");

    public SnowLeopardMaleModel() {
        super(bodyLayer, TEXTURE);
    }

    AnimationState ears = new AnimationState();
    AnimationState tail = new AnimationState();

    @Override
    public void setupAnim(@NotNull E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch);
        if(!ears.isStarted()) ears.start((int) ageInTicks);
        if(!tail.isStarted()) tail.start((int) ageInTicks);
        KeyframeAnimator.animate(ears, root(), Animations.EAR_ANIM, ageInTicks);
        KeyframeAnimator.animate(tail, root(), Animations.TAIL_CAT, ageInTicks);
    }

    public static ModelDefinition bodyLayer() {
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new CubeUV().up(24, 24, 16, 16).east(16, 20, 8, 12).down(8, 20, 0, 28).north(8, 20, 0, 12).west(24, 16, 16, 8).south(24, 8, 16, 0)));
        head.addOrReplaceChild("right_ear", GroupBuilder.create()
                .addMesh(new float[]{-1, 5, -1, -1, 5, 4, -1, -2, -1, -1, -2, 4, 1, 5, -1, 1, 5, 4, 1, -2, -1, 1, -2, 4, -0.99f, 2, -1, -0.99f, 5, 2, -0.99f, 1.4284f, -0.2527f, 0.99f, 2, -1, 0.99f, 5, 2}, new float[]{3, 33, 19, 1, 33, 12, 0, 28, 12, 2, 28, 19, 6, 33, 26, 4, 33, 19, 5, 28, 19, 7, 28, 26, 0, 12, 52, 1, 12, 57, 5, 14, 57, 4, 14, 52, 2, 18, 55, 0, 18, 48, 4, 16, 48, 6, 16, 55, 7, 40, 56, 5, 40, 49, 1, 38, 49, 3, 38, 56, 8, 28, 40, 9, 28, 36, 12, 26, 36, 11, 26, 40}), PartPose.rotation(-0.4276f, -0.1745f, -0.5236f));
        head.addOrReplaceChild("left_ear", GroupBuilder.create()
                .addMesh(new float[]{1, 5, -1, 1, 5, 4, 1, -2, -1, 1, -2, 4, -1, 5, -1, -1, 5, 4, -1, -2, -1, -1, -2, 4, 0.99f, 2, -1, 0.99f, 5, 2, 0.99f, 1.4284f, -0.2527f, -0.99f, 2, -1, -0.99f, 5, 2}, new float[]{0, 33, 26, 1, 28, 26, 3, 28, 33, 2, 33, 33, 5, 37, 0, 4, 32, 0, 6, 32, 7, 7, 37, 7, 5, 14, 57, 1, 16, 57, 0, 16, 52, 4, 14, 52, 4, 2, 50, 0, 0, 50, 2, 0, 57, 6, 2, 57, 1, 4, 50, 5, 2, 50, 7, 2, 57, 3, 4, 57, 12, 47, 52, 9, 45, 52, 8, 45, 56, 11, 47, 56}), PartPose.rotation(-0.4276f, 0.1745f, 0.5236f));
        head.addOrReplaceChild("maw", GroupBuilder.create()
                .addBox(-2f, -1f, -2f, 4, 2, 2, new CubeUV().up(56, 50, 52, 48).east(20, 46, 18, 44).down(56, 50, 52, 52).north(34, 54, 30, 52).west(32, 48, 30, 46).south(56, 48, 52, 46)), PartPose.offset(0, 2, -4));
        head.addOrReplaceChild("maw_i0", GroupBuilder.create()
                .addBox(-1.5f, -1f, -1.5f, 3, 1, 1.5f, new CubeUV().up(49, 22.5f, 46, 21).east(43.5f, 39, 42, 38).down(49, 35, 46, 36.5f).north(40, 7, 37, 6).west(46.5f, 43, 45, 42).south(44, 6, 41, 5)), PartPose.offset(0, 1, -4));
        head.addOrReplaceChild("armor_head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new Vector3f(0.6f), new CubeUV().up(16, 8, 8, 0).east(8, 16, 0, 8).down(24, 0, 16, 8).north(16, 16, 8, 8).west(24, 16, 16, 8).south(32, 16, 24, 8)).armor());
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new CubeUV().up(40, 11, 32, 7).east(12, 32, 8, 20).down(16, 32, 8, 36).north(8, 12, 0, 0).west(16, 32, 12, 20).south(16, 12, 8, 0)));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addBox(-1.8f, -1.8f, -2f, 3.6f, 3.6f, 5, new CubeUV().up(47.5f, 48, 44, 43).east(13, 47.5f, 8, 44).down(48.5f, 0, 45, 5).north(53.5f, 32.5f, 50, 29).west(18, 47.5f, 13, 44).south(51.5f, 53.5f, 48, 50)), PartPose.rotation(0.6545f, 0, 0));
        GroupDefinition tail0 = tail.addOrReplaceChild("tail0", GroupBuilder.create(), PartPose.rotation(-0.3491f, 0, 0));
        GroupDefinition tail1 = tail0.addOrReplaceChild("tail1", GroupBuilder.create(), PartPose.rotation(-0.3491f, 0, 0));
        GroupDefinition tail2 = tail1.addOrReplaceChild("tail2", GroupBuilder.create(), PartPose.rotation(-0.3054f, 0, 0));
        GroupDefinition tail3 = tail2.addOrReplaceChild("tail3", GroupBuilder.create(), PartPose.rotation(-0.3054f, 0, 0));
        GroupDefinition tail4 = tail3.addOrReplaceChild("tail4", GroupBuilder.create(), PartPose.rotation(-0.3054f, 0, 0));
        tail4.addOrReplaceChild("tail_i0", GroupBuilder.create()
                .addBox(-1.8f, -1.8f, -2f, 3.6f, 3.6f, 4, new CubeUV().up(53.5f, 20, 50, 16).east(8, 53.5f, 4, 50).down(21.5f, 50, 18, 54).north(25.5f, 53.5f, 22, 50).west(54, 15.5f, 50, 12).south(29.5f, 53.5f, 26, 50)), PartPose.offset(0, -2.6299f, 1.2941f));
        tail3.addOrReplaceChild("tail_i1", GroupBuilder.create()
                .addBox(-2f, -2f, 0f, 4, 4, 4, new CubeUV().up(40, 49, 36, 45).east(49, 13, 45, 9).down(49, 38, 45, 42).north(49, 9, 45, 5).west(49, 31, 45, 27).south(49, 27, 45, 23)), PartPose.offset(0, -1.7677f, 1.0632f));
        tail2.addOrReplaceChild("tail_i2", GroupBuilder.create()
                .addBox(-2.1f, -2.1f, 0f, 4.2f, 4.2f, 5, new CubeUV().up(45, 5, 41, 0).east(45, 10, 40, 6).down(45, 10, 41, 15).north(4, 50, 0, 46).west(45, 43, 40, 39).south(8, 50, 4, 46)), PartPose.offset(0, -0.9115f, 0.9525f));
        tail1.addOrReplaceChild("tail_i3", GroupBuilder.create()
                .addBox(-2f, -2f, -2f, 4, 4, 5, new CubeUV().up(45, 28, 41, 23).east(46, 19, 41, 15).down(32, 41, 28, 46).north(50, 17, 46, 13).west(46, 23, 41, 19).south(50, 21, 46, 17)), PartPose.offset(0, -0.3056f, 0.4861f));
        tail0.addOrReplaceChild("tail_i4", GroupBuilder.create()
                .addBox(-1.9f, -1.9f, -3f, 3.8f, 3.8f, 6, new CubeUV().up(41, 6, 37, 0).east(26, 40, 20, 36).down(41, 11, 37, 17).north(22, 50, 18, 46).west(42, 39, 36, 35).south(26, 50, 22, 46)), PartPose.offset(0, 0.1107f, 0.1024f));
        body.addOrReplaceChild("armor_body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new Vector3f(0.6f), new CubeUV().up(40, 11, 32, 7).east(32, 32, 28, 20).down(40, 34, 32, 38).north(28, 32, 20, 20).west(20, 32, 16, 20).south(40, 32, 32, 20)).armor());
        GroupDefinition right_arm = root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new CubeUV().up(30, 50, 26, 46).east(28, 24, 24, 12).down(50, 31, 46, 35).north(28, 12, 24, 0).west(24, 36, 20, 24).south(20, 36, 16, 24)));
        right_arm.addOrReplaceChild("armor_right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().up(48, 20, 44, 16).east(44, 32, 40, 20).north(48, 32, 44, 20).west(52, 32, 48, 20).south(56, 32, 52, 20)).armor());
        GroupDefinition left_arm = root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new CubeUV().up(12, 52, 8, 48).east(4, 40, 0, 28).down(16, 48, 12, 52).north(28, 36, 24, 24).west(8, 40, 4, 28).south(32, 12, 28, 0)));
        left_arm.addOrReplaceChild("armor_left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().up(44, 20, 48, 16).east(52, 32, 48, 20).north(56, 32, 52, 20).west(40, 32, 44, 20).south(48, 32, 44, 20)).armor());
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.rotation(0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create());
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create());
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create());
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new CubeUV().up(45, 33, 41, 28).east(55, 35, 50, 33).down(46, 33, 42, 38).north(57, 8, 53, 6).west(45, 54, 40, 52).south(57, 10, 53, 8))
                .addMesh(new float[]{1.6929f, -6.025f, -2, 1.4f, -6.025f, -2.7071f, 2.4f, -6.025f, -2.2929f, 2.1071f, -6.025f, -3, 1.9f, -6.025f, -2.5f, 1.4f, -6.025f, -2.2929f, 2.1071f, -6.025f, -2, 2.4f, -6.025f, -2.7071f, 1.6929f, -6.025f, -3, 0.4929f, -6.025f, -2, 0.2f, -6.025f, -2.7071f, 1.2f, -6.025f, -2.2929f, 0.9071f, -6.025f, -3, 0.7f, -6.025f, -2.5f, 0.2f, -6.025f, -2.2929f, 0.9071f, -6.025f, -2, 1.2f, -6.025f, -2.7071f, 0.4929f, -6.025f, -3, -0.7071f, -6.025f, -2, -1, -6.025f, -2.7071f, -0f, -6.025f, -2.2929f, -0.2929f, -6.025f, -3, -0.5f, -6.025f, -2.5f, -1, -6.025f, -2.2929f, -0.2929f, -6.025f, -2, -0f, -6.025f, -2.7071f, -0.7071f, -6.025f, -3, -0.915f, -6.025f, 1.025f, -0.725f, -6.025f, -1.325f, 2.315f, -6.025f, 1.025f, 2.125f, -6.025f, -1.325f, 0.7f, -6.025f, -0.3f, -1.2f, -6.025f, -0f, 0.7f, -6.025f, 0.9f, 2.6f, -6.025f, -0f, 0.7f, -6.025f, -1.8f}, new float[]{5, 50.25f, 20.2929f, 1, 50.25f, 20.7071f, 8, 50.5429f, 21, 4, 50.75f, 20.5f, 6, 50.9571f, 20, 0, 50.5429f, 20, 5, 50.25f, 20.2929f, 4, 50.75f, 20.5f, 7, 51.25f, 20.7071f, 2, 51.25f, 20.2929f, 6, 50.9571f, 20, 4, 50.75f, 20.5f, 8, 50.5429f, 21, 3, 50.9571f, 21, 7, 51.25f, 20.7071f, 4, 50.75f, 20.5f, 14, 46.25f, 37.2929f, 10, 46.25f, 37.7071f, 17, 46.5429f, 38, 13, 46.75f, 37.5f, 15, 46.9571f, 37, 9, 46.5428f, 37f, 14, 46.25f, 37.2929f, 13, 46.75f, 37.5f, 16, 47.25f, 37.7071f, 11, 47.25f, 37.293f, 15, 46.9571f, 37.0001f, 13, 46.75f, 37.5f, 17, 46.5429f, 38, 12, 46.9571f, 38, 16, 47.25f, 37.7071f, 13, 46.75f, 37.5f, 23, 32f, 33.7929f, 19, 32f, 34.2071f, 26, 32.2929f, 34.5f, 22, 32.5f, 34f, 24, 32.7071f, 33.5f, 18, 32.2929f, 33.5f, 23, 32f, 33.7929f, 22, 32.5f, 34f, 25, 33f, 34.2071f, 20, 33f, 33.7929f, 24, 32.707f, 33.4999f, 22, 32.5f, 34f, 26, 32.2929f, 34.5f, 21, 32.7071f, 34.5f, 25, 33f, 34.2071f, 22, 32.5f, 34, 32, 49.1f, 40.2f, 28, 49.575f, 41.525f, 35, 51f, 42, 31, 51, 40.5f, 33, 51f, 39.3f, 27, 49.385f, 39.175f, 32, 49.1f, 40.2f, 31, 51f, 40.5f, 34, 52.9f, 40.2f, 29, 52.615f, 39.175f, 33, 51f, 39.3f, 31, 51f, 40.5f, 35, 51f, 42, 30, 52.425f, 41.525f, 34, 52.9f, 40.2f, 31, 51, 40.5f}), PartPose.rotation(0, 0, -7.5f));
        right_foot.addOrReplaceChild("armor_right_foot", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().up(12, 20, 8, 16).east(12, 20, 8, 16).down(12, 16, 8, 20).north(12, 20, 8, 16).west(12, 20, 8, 16).south(12, 20, 8, 16)).armor(), PartPose.rotation(0, 0, -7.5f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().up(52, 50, 48, 46).east(41, 29, 37, 23).down(53, 0, 49, 4).north(41, 23, 37, 17).west(40, 45, 36, 39).south(41, 35, 37, 29)), PartPose.rotation(7.5f, 0, 0));
        right_leg_.addOrReplaceChild("armor_right_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().up(49, 16, 45, 12).east(8, 32, 4, 26).down(49, 16, 45, 20).north(4, 32, 0, 26).west(8, 32, 4, 26).south(4, 32, 0, 26)).armor(), PartPose.rotation(7.5f, 0, 0));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().up(52, 46, 48, 42).east(57, 2, 53, 0).down(48, 48, 44, 52).north(56, 54, 52, 52).west(57, 6, 53, 4).south(57, 4, 53, 2)), PartPose.rotation(20, 0, 0));
        right_leg_shin.addOrReplaceChild("armor_right_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().up(8, 29, 4, 25).east(8, 29, 4, 25).down(49, 8, 45, 12).west(8, 29, 4, 25).south(8, 29, 4, 25)).armor(), PartPose.rotation(20, 0, 0));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().up(34, 52, 30, 48).east(37, 27, 33, 19).down(44, 48, 40, 52).north(37, 19, 33, 11).west(32, 41, 28, 33).south(37, 35, 33, 27)), PartPose.rotation(20, 0, 0));
        right_leg.addOrReplaceChild("armor_right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().up(8, 20, 4, 16).east(12, 29, 8, 20).down(8, 25, 4, 29).north(8, 29, 4, 20).west(12, 29, 8, 20).south(16, 29, 12, 20)).armor(), PartPose.rotation(20, 0, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.rotation(0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create());
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create());
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create());
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new CubeUV().up(36, 48, 32, 43).east(57, 44, 52, 42).down(44, 43, 40, 48).north(57, 28, 53, 26).west(57, 46, 52, 44).south(38, 55, 34, 53))
                .addMesh(new float[]{-1.6929f, -6.025f, -2, -1.4f, -6.025f, -2.7071f, -2.4f, -6.025f, -2.2929f, -2.1071f, -6.025f, -3, -1.9f, -6.025f, -2.5f, -1.4f, -6.025f, -2.2929f, -2.1071f, -6.025f, -2, -2.4f, -6.025f, -2.7071f, -1.6929f, -6.025f, -3, -0.4929f, -6.025f, -2, -0.2f, -6.025f, -2.7071f, -1.2f, -6.025f, -2.2929f, -0.9071f, -6.025f, -3, -0.7f, -6.025f, -2.5f, -0.2f, -6.025f, -2.2929f, -0.9071f, -6.025f, -2, -1.2f, -6.025f, -2.7071f, -0.4929f, -6.025f, -3, 0.7071f, -6.025f, -2, 1, -6.025f, -2.7071f, 0f, -6.025f, -2.2929f, 0.2929f, -6.025f, -3, 0.5f, -6.025f, -2.5f, 1, -6.025f, -2.2929f, 0.2929f, -6.025f, -2, 0f, -6.025f, -2.7071f, 0.7071f, -6.025f, -3, 0.915f, -6.025f, 1.025f, 0.725f, -6.025f, -1.325f, -2.315f, -6.025f, 1.025f, -2.125f, -6.025f, -1.325f, -0.7f, -6.025f, -0.3f, 1.2f, -6.025f, -0f, -0.7f, -6.025f, 0.9f, -2.6f, -6.025f, -0f, -0.7f, -6.025f, -1.8f}, new float[]{8, 32.7071f, 12, 1, 33, 11.7071f, 5, 33, 11.2929f, 4, 32.5f, 11.5f, 5, 33, 11.2929f, 0, 32.7071f, 11, 6, 32.2929f, 11, 4, 32.5f, 11.5f, 6, 32.2929f, 11, 2, 32f, 11.2929f, 7, 32, 11.7071f, 4, 32.5f, 11.5f, 7, 32, 11.7071f, 3, 32.2929f, 12, 8, 32.7071f, 12, 4, 32.5f, 11.5f, 17, 34.9571f, 49, 10, 35.25f, 48.7071f, 14, 35.25f, 48.2929f, 13, 34.75f, 48.5f, 14, 35.25f, 48.2929f, 9, 34.9571f, 48f, 15, 34.5429f, 48, 13, 34.75f, 48.5f, 15, 34.5429f, 48, 11, 34.25f, 48.2929f, 16, 34.25f, 48.707f, 13, 34.75f, 48.5f, 16, 34.25f, 48.7071f, 12, 34.5429f, 49, 17, 34.9571f, 49, 13, 34.75f, 48.5f, 26, 53.9571f, 36.5f, 19, 54.25f, 36.2071f, 23, 54.25f, 35.7929f, 22, 53.75f, 36f, 23, 54.25f, 35.7929f, 18, 53.9571f, 35.5f, 24, 53.5429f, 35.5f, 22, 53.75f, 36f, 24, 53.5429f, 35.5f, 20, 53.25f, 35.7929f, 25, 53.2501f, 36.2071f, 22, 53.75f, 36f, 25, 53.2501f, 36.2071f, 21, 53.5429f, 36.5f, 26, 53.9571f, 36.5f, 22, 53.75f, 36, 35, 10f, 55, 28, 11.425f, 54.525f, 32, 11.9f, 53.2f, 31, 10f, 53.5f, 32, 11.9f, 53.2f, 27, 11.615f, 52.175f, 33, 10, 52.3f, 31, 10, 53.5f, 33, 10, 52.3f, 29, 8.385f, 52.175f, 34, 8.1f, 53.2f, 31, 10, 53.5f, 34, 8.1f, 53.2f, 30, 8.575f, 54.525f, 35, 10f, 55, 31, 10f, 53.5f}), PartPose.rotation(0, 0, 7.5f));
        left_foot.addOrReplaceChild("armor_left_foot", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().up(12, 20, 8, 16).east(12, 20, 8, 16).down(8, 16, 12, 20).north(8, 20, 12, 16).west(12, 16, 8, 20).south(12, 20, 8, 16)).armor(), PartPose.rotation(0, 0, 7.5f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().up(38, 53, 34, 49).east(8, 46, 4, 40).down(53, 35, 49, 39).north(4, 46, 0, 40).west(28, 46, 24, 40).south(24, 46, 20, 40)), PartPose.rotation(7.5f, 0, 0));
        left_leg_.addOrReplaceChild("armor_left_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().up(8, 50, 4, 46).east(8, 32, 4, 26).down(27, 46, 23, 50).north(0, 32, 4, 26).west(4, 32, 8, 26).south(4, 32, 0, 26)).armor(), PartPose.rotation(7.5f, 0, 0));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().up(53, 25, 49, 21).east(57, 22, 53, 20).down(53, 25, 49, 29).north(57, 12, 53, 10).west(57, 26, 53, 24).south(57, 24, 53, 22)), PartPose.rotation(20, 0, 0));
        left_leg_shin.addOrReplaceChild("armor_left_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().up(8, 29, 4, 25).east(8, 29, 4, 25).down(4, 46, 0, 50).north(53, 26, 49, 24).west(4, 29, 8, 25).south(8, 29, 4, 25)).armor(), PartPose.rotation(20, 0, 0));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().up(53, 8, 49, 4).east(12, 44, 8, 36).down(53, 8, 49, 12).north(36, 43, 32, 35).west(20, 44, 16, 36).south(16, 44, 12, 36)), PartPose.rotation(20, 0, 0));
        left_leg.addOrReplaceChild("armor_left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().up(8, 20, 4, 16).east(12, 29, 8, 20).down(4, 25, 8, 29).north(4, 29, 8, 20).west(12, 29, 8, 20).south(16, 29, 12, 20)).armor(), PartPose.rotation(20, 0, 0));

        return ModelDefinition.create(modelBuilder, 128, 128, 2);
    }
}