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
                .addBox(-4f, 0f, -4f, 8, 8, 8, new CubeUV().east(16, 20, 8, 12).west(24, 16, 16, 8).north(8, 20, 0, 12).south(24, 8, 16, 0).up(24, 24, 16, 16).down(8, 20, 0, 28))
                .addBox(-2f, 1f, -6f, 4, 2, 2, new CubeUV().east(20, 46, 18, 44).west(32, 48, 30, 46).north(34, 54, 30, 52).south(56, 48, 52, 46).up(56, 50, 52, 48).down(56, 50, 52, 52))
                .addBox(-1.5f, 0f, -5.5f, 3, 1, 1.5f, new CubeUV().east(43.5f, 39, 42, 38).west(46.5f, 43, 45, 42).north(40, 7, 37, 6).south(44, 6, 41, 5).up(49, 22.5f, 46, 21).down(49, 35, 46, 36.5f)), PartPose.offset(0, 24, 0));
        GroupDefinition right_ear = head.addOrReplaceChild("right_ear", GroupBuilder.create(), PartPose.offsetAndRotation(3, 6, -1, -0.4276f, -0.1745f, -0.5236f));
        right_ear.addOrReplaceChild("right_ear_i0", GroupBuilder.create()
                .addMesh(new float[]{-1, 5, -4, -1, 5, 1, -1, -2, -4, -1, -2, 1, 1, 5, -4, 1, 5, 1, 1, -2, -4, 1, -2, 1, -0.99f, 2, -4, -0.99f, 5, -1, -0.99f, 1.4284f, -3.2527f, 0.99f, 2, -4, 0.99f, 5, -1}, new float[]{3, 33, 19, 1, 33, 12, 0, 28, 12, 2, 28, 19, 6, 33, 26, 4, 33, 19, 5, 28, 19, 7, 28, 26, 0, 12, 52, 1, 12, 57, 5, 14, 57, 4, 14, 52, 2, 18, 55, 0, 18, 48, 4, 16, 48, 6, 16, 55, 7, 40, 56, 5, 40, 49, 1, 38, 49, 3, 38, 56, 8, 28, 40, 9, 28, 36, 12, 26, 36, 11, 26, 40}), PartPose.offset(0, 0, 2));
        GroupDefinition left_ear = head.addOrReplaceChild("left_ear", GroupBuilder.create(), PartPose.offsetAndRotation(-3, 7, 1, -0.4276f, 0.1745f, 0.5236f));
        left_ear.addOrReplaceChild("left_ear_i0", GroupBuilder.create()
                .addMesh(new float[]{1, 5, -4, 1, 5, 1, 1, -2, -4, 1, -2, 1, -1, 5, -4, -1, 5, 1, -1, -2, -4, -1, -2, 1, 0.99f, 2, -4, 0.99f, 5, -1, 0.99f, 1.4284f, -3.2527f, -0.99f, 2, -4, -0.99f, 5, -1}, new float[]{0, 33, 26, 1, 28, 26, 3, 28, 33, 2, 33, 33, 5, 37, 0, 4, 32, 0, 6, 32, 7, 7, 37, 7, 5, 14, 57, 1, 16, 57, 0, 16, 52, 4, 14, 52, 4, 2, 50, 0, 0, 50, 2, 0, 57, 6, 2, 57, 1, 4, 50, 5, 2, 50, 7, 2, 57, 3, 4, 57, 12, 47, 52, 9, 45, 52, 8, 45, 56, 11, 47, 56}));
        head.addOrReplaceChild("armor_head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new Vector3f(0.6f), new CubeUV().east(8, 16, 0, 8).west(24, 16, 16, 8).north(16, 16, 8, 8).south(32, 16, 24, 8).up(16, 8, 8, 0).down(24, 0, 16, 8)).armor());
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new CubeUV().east(12, 32, 8, 20).west(16, 32, 12, 20).north(8, 12, 0, 0).south(16, 12, 8, 0).up(40, 11, 32, 7).down(16, 32, 8, 36)), PartPose.offset(0, 12, 0));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addBox(-1.8f, -1.8f, -2f, 3.6f, 3.6f, 5, new CubeUV().east(13, 47.5f, 8, 44).west(18, 47.5f, 13, 44).north(53.5f, 32.5f, 50, 29).south(51.5f, 53.5f, 48, 50).up(47.5f, 48, 44, 43).down(48.5f, 0, 45, 5)), PartPose.offsetAndRotation(0, 0, 2, 0.6545f, 0, 0));
        GroupDefinition tail0 = tail.addOrReplaceChild("tail0", GroupBuilder.create()
                .addBox(-1.9f, -1.7893f, -2.8976f, 3.8f, 3.8f, 6, new CubeUV().east(26, 40, 20, 36).west(42, 39, 36, 35).north(22, 50, 18, 46).south(26, 50, 22, 46).up(41, 6, 37, 0).down(41, 11, 37, 17)), PartPose.offsetAndRotation(0, 0.875f, 5.1f, -0.3491f, 0, 0));
        GroupDefinition tail1 = tail0.addOrReplaceChild("tail1", GroupBuilder.create()
                .addBox(-2f, -2.3056f, -1.5139f, 4, 4, 5, new CubeUV().east(46, 19, 41, 15).west(46, 23, 41, 19).north(50, 17, 46, 13).south(50, 21, 46, 17).up(45, 28, 41, 23).down(32, 41, 28, 46)), PartPose.offsetAndRotation(0, 0.9f, 3.6f, -0.3491f, 0, 0));
        GroupDefinition tail2 = tail1.addOrReplaceChild("tail2", GroupBuilder.create()
                .addBox(-2.1f, -3.0115f, 0.9525f, 4.2f, 4.2f, 5, new CubeUV().east(45, 10, 40, 6).west(45, 43, 40, 39).north(4, 50, 0, 46).south(8, 50, 4, 46).up(45, 5, 41, 0).down(45, 10, 41, 15)), PartPose.offsetAndRotation(0, 0.2f, 1.6f, -0.3054f, 0, 0));
        GroupDefinition tail3 = tail2.addOrReplaceChild("tail3", GroupBuilder.create()
                .addBox(-2f, -3.7677f, 1.0632f, 4, 4, 4, new CubeUV().east(49, 13, 45, 9).west(49, 31, 45, 27).north(49, 9, 45, 5).south(49, 27, 45, 23).up(40, 49, 36, 45).down(49, 38, 45, 42)), PartPose.offsetAndRotation(0, 0.4f, 3.7f, -0.3054f, 0, 0));
        tail3.addOrReplaceChild("tail4", GroupBuilder.create()
                .addBox(-1.8f, -4.4299f, -0.7059f, 3.6f, 3.6f, 4, new CubeUV().east(8, 53.5f, 4, 50).west(54, 15.5f, 50, 12).north(25.5f, 53.5f, 22, 50).south(29.5f, 53.5f, 26, 50).up(53.5f, 20, 50, 16).down(21.5f, 50, 18, 54)), PartPose.offsetAndRotation(0, 0.8f, 4.4f, -0.3054f, 0, 0));
        body.addOrReplaceChild("armor_body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new Vector3f(0.6f), new CubeUV().east(32, 32, 28, 20).west(20, 32, 16, 20).north(28, 32, 20, 20).south(40, 32, 32, 20).up(40, 11, 32, 7).down(40, 34, 32, 38)).armor());
        GroupDefinition right_arm = root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new CubeUV().east(28, 24, 24, 12).west(24, 36, 20, 24).north(28, 12, 24, 0).south(20, 36, 16, 24).up(30, 50, 26, 46).down(50, 31, 46, 35)), PartPose.offset(4, 22, 0));
        right_arm.addOrReplaceChild("armor_right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().east(44, 32, 40, 20).west(52, 32, 48, 20).north(48, 32, 44, 20).south(56, 32, 52, 20).up(48, 20, 44, 16)).armor());
        GroupDefinition left_arm = root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new CubeUV().east(4, 40, 0, 28).west(8, 40, 4, 28).north(28, 36, 24, 24).south(32, 12, 28, 0).up(12, 52, 8, 48).down(16, 48, 12, 52)), PartPose.offset(-4, 22, 0));
        left_arm.addOrReplaceChild("armor_left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().east(52, 32, 48, 20).west(40, 32, 44, 20).north(56, 32, 52, 20).south(48, 32, 44, 20).up(44, 20, 48, 16)).armor());
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2, 13, 0, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create());
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new CubeUV().east(55, 35, 50, 33).west(45, 54, 40, 52).north(57, 8, 53, 6).south(57, 10, 53, 8).up(45, 33, 41, 28).down(46, 33, 42, 38))
                .addMesh(new float[]{-0.915f, -6.025f, 1.025f, -0.725f, -6.025f, -1.325f, 2.315f, -6.025f, 1.025f, 2.125f, -6.025f, -1.325f, 0.7f, -6.025f, -0.3f, -1.2f, -6.025f, 0, 0.7f, -6.025f, 0.9f, 2.6f, -6.025f, 0, 0.7f, -6.025f, -1.8f, -0.5f, -6.025f, -2.5f, -0.7071f, -6.025f, -2, -1, -6.025f, -2.2929f, -1, -6.025f, -2.7071f, -0.7071f, -6.025f, -3, -0.2929f, -6.025f, -3, -0f, -6.025f, -2.7071f, 0, -6.025f, -2.2929f, -0.2929f, -6.025f, -2, 0.7f, -6.025f, -2.5f, 0.4929f, -6.025f, -2, 0.2f, -6.025f, -2.2929f, 0.2f, -6.025f, -2.7071f, 0.4929f, -6.025f, -3, 0.9071f, -6.025f, -3, 1.2f, -6.025f, -2.7071f, 1.2f, -6.025f, -2.2929f, 0.9071f, -6.025f, -2, 1.9f, -6.025f, -2.5f, 1.6929f, -6.025f, -2, 1.4f, -6.025f, -2.2929f, 1.4f, -6.025f, -2.7071f, 1.6929f, -6.025f, -3, 2.1071f, -6.025f, -3, 2.4f, -6.025f, -2.7071f, 2.4f, -6.025f, -2.2929f, 2.1071f, -6.025f, -2}, new float[]{5, 49.1f, 40.2f, 1, 49.575f, 41.525f, 8, 51, 42, 4, 51, 40.5f, 6, 51, 39.3f, 0, 49.385f, 39.175f, 5, 49.1f, 40.2f, 4, 51, 40.5f, 7, 52.9f, 40.2f, 2, 52.615f, 39.175f, 6, 51, 39.3f, 4, 51, 40.5f, 8, 51, 42, 3, 52.425f, 41.525f, 7, 52.9f, 40.2f, 4, 51, 40.5f, 9, 6.75f, 54.5f, 12, 6.25f, 54.2929f, 11, 6.25f, 54.7071f, 10, 6.5429f, 55f, 9, 6.75f, 54.5f, 14, 6.9571f, 54, 13, 6.5428f, 54.0001f, 12, 6.25f, 54.2929f, 9, 6.75f, 54.5f, 16, 7.25f, 54.7071f, 15, 7.25f, 54.2929f, 14, 6.9571f, 54, 17, 6.957f, 55f, 16, 7.2499f, 54.7071f, 9, 6.75f, 54.5f, 10, 6.5429f, 55f, 18, 4.75f, 54.5f, 21, 4.25f, 54.2929f, 20, 4.25f, 54.7071f, 19, 4.5429f, 55, 18, 4.75f, 54.5f, 23, 4.9571f, 54, 22, 4.5429f, 54, 21, 4.25f, 54.2929f, 18, 4.75f, 54.5f, 25, 5.25f, 54.7071f, 24, 5.25f, 54.2929f, 23, 4.9571f, 54, 26, 4.9571f, 55, 25, 5.25f, 54.7071f, 18, 4.75f, 54.5f, 19, 4.5429f, 55f, 27, 53.75f, 39.5f, 30, 53.25f, 39.2929f, 29, 53.25f, 39.7071f, 28, 53.5429f, 40f, 27, 53.75f, 39.5f, 32, 53.9571f, 39, 31, 53.5429f, 39f, 30, 53.25f, 39.2929f, 27, 53.75f, 39.5f, 34, 54.25f, 39.7071f, 33, 54.25f, 39.2929f, 32, 53.9571f, 39f, 35, 53.957f, 40f, 34, 54.25f, 39.7071f, 27, 53.75f, 39.5f, 28, 53.5429f, 40f}), PartPose.rotation(0, 0, -0.1309f));
        right_foot.addOrReplaceChild("armor_right_foot", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().east(12, 20, 8, 16).west(12, 20, 8, 16).north(12, 20, 8, 16).south(12, 20, 8, 16).up(12, 20, 8, 16).down(12, 16, 8, 20)).armor(), PartPose.rotation(0, 0, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().east(41, 29, 37, 23).west(40, 45, 36, 39).north(41, 23, 37, 17).south(41, 35, 37, 29).up(52, 50, 48, 46).down(53, 0, 49, 4)), PartPose.rotation(0.1309f, 0, 0));
        right_leg_.addOrReplaceChild("armor_right_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().east(8, 32, 4, 26).west(8, 32, 4, 26).north(4, 32, 0, 26).south(4, 32, 0, 26).up(49, 16, 45, 12).down(49, 16, 45, 20)).armor(), PartPose.rotation(0.1309f, 0, 0));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().east(57, 2, 53, 0).west(57, 6, 53, 4).north(56, 54, 52, 52).south(57, 4, 53, 2).up(52, 46, 48, 42).down(48, 48, 44, 52)), PartPose.rotation(0.3491f, 0, 0));
        right_leg_shin.addOrReplaceChild("armor_right_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().east(8, 29, 4, 25).west(8, 29, 4, 25).south(8, 29, 4, 25).up(8, 29, 4, 25).down(49, 8, 45, 12)).armor(), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().east(37, 27, 33, 19).west(32, 41, 28, 33).north(37, 19, 33, 11).south(37, 35, 33, 27).up(34, 52, 30, 48).down(44, 48, 40, 52)), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("armor_right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().east(12, 29, 8, 20).west(12, 29, 8, 20).north(8, 29, 4, 20).south(16, 29, 12, 20).up(8, 20, 4, 16).down(8, 25, 4, 29)).armor(), PartPose.rotation(0.3491f, 0, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2, 13, 0, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create());
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new CubeUV().east(57, 44, 52, 42).west(57, 46, 52, 44).north(57, 28, 53, 26).south(38, 55, 34, 53).up(36, 48, 32, 43).down(44, 43, 40, 48))
                .addMesh(new float[]{0.915f, -6.025f, 1.025f, 0.725f, -6.025f, -1.325f, -2.315f, -6.025f, 1.025f, -2.125f, -6.025f, -1.325f, -0.7f, -6.025f, -0.3f, 1.2f, -6.025f, 0, -0.7f, -6.025f, 0.9f, -2.6f, -6.025f, 0, -0.7f, -6.025f, -1.8f, 0.5f, -6.025f, -2.5f, 0.7071f, -6.025f, -2, 1, -6.025f, -2.2929f, 1, -6.025f, -2.7071f, 0.7071f, -6.025f, -3, 0.2929f, -6.025f, -3, 0f, -6.025f, -2.7071f, 0, -6.025f, -2.2929f, 0.2929f, -6.025f, -2, -0.7f, -6.025f, -2.5f, -0.4929f, -6.025f, -2, -0.2f, -6.025f, -2.2929f, -0.2f, -6.025f, -2.7071f, -0.4929f, -6.025f, -3, -0.9071f, -6.025f, -3, -1.2f, -6.025f, -2.7071f, -1.2f, -6.025f, -2.2929f, -0.9071f, -6.025f, -2, -1.9f, -6.025f, -2.5f, -1.6929f, -6.025f, -2, -1.4f, -6.025f, -2.2929f, -1.4f, -6.025f, -2.7071f, -1.6929f, -6.025f, -3, -2.1071f, -6.025f, -3, -2.4f, -6.025f, -2.7071f, -2.4f, -6.025f, -2.2929f, -2.1071f, -6.025f, -2}, new float[]{8, 10, 55, 1, 11.425f, 54.525f, 5, 11.9f, 53.2f, 4, 10, 53.5f, 5, 11.9f, 53.2f, 0, 11.615f, 52.175f, 6, 10, 52.3f, 4, 10, 53.5f, 6, 10, 52.3f, 2, 8.385f, 52.175f, 7, 8.1f, 53.2f, 4, 10, 53.5f, 7, 8.1f, 53.2f, 3, 8.575f, 54.525f, 8, 10, 55, 4, 10, 53.5f, 11, 33f, 33.7071f, 12, 33f, 33.2929f, 9, 32.5f, 33.5f, 10, 32.7071f, 34f, 13, 32.7071f, 33.0001f, 14, 32.2928f, 33f, 9, 32.5f, 33.5f, 12, 33f, 33.2929f, 15, 32f, 33.2929f, 16, 32f, 33.7071f, 9, 32.5f, 33.5f, 14, 32.2929f, 33, 9, 32.5f, 33.5f, 16, 32f, 33.7071f, 17, 32.2929f, 34f, 10, 32.7071f, 34f, 20, 54.25f, 35.7071f, 21, 54.25f, 35.2929f, 18, 53.75f, 35.5f, 19, 53.9571f, 36f, 22, 53.9571f, 35f, 23, 53.5429f, 35f, 18, 53.75f, 35.5f, 21, 54.25f, 35.2929f, 24, 53.25f, 35.2929f, 25, 53.25f, 35.7071f, 18, 53.75f, 35.5f, 23, 53.5429f, 35, 18, 53.75f, 35.5f, 25, 53.25f, 35.7071f, 26, 53.5429f, 36f, 19, 53.9571f, 36f, 29, 54.25f, 37.7071f, 30, 54.25f, 37.2929f, 27, 53.75f, 37.5f, 28, 53.9571f, 38f, 31, 53.9571f, 37f, 32, 53.5429f, 37, 27, 53.75f, 37.5f, 30, 54.25f, 37.2929f, 33, 53.25f, 37.2929f, 34, 53.25f, 37.7071f, 27, 53.75f, 37.5f, 32, 53.5429f, 37f, 27, 53.75f, 37.5f, 34, 53.25f, 37.7071f, 35, 53.543f, 38f, 28, 53.9571f, 38f}), PartPose.rotation(0, 0, 0.1309f));
        left_foot.addOrReplaceChild("armor_left_foot", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().east(12, 20, 8, 16).west(12, 16, 8, 20).north(8, 20, 12, 16).south(12, 20, 8, 16).up(12, 20, 8, 16).down(8, 16, 12, 20)).armor(), PartPose.rotation(0, 0, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().east(8, 46, 4, 40).west(28, 46, 24, 40).north(4, 46, 0, 40).south(24, 46, 20, 40).up(38, 53, 34, 49).down(53, 35, 49, 39)), PartPose.rotation(0.1309f, 0, 0));
        left_leg_.addOrReplaceChild("armor_left_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().east(8, 32, 4, 26).west(4, 32, 8, 26).north(0, 32, 4, 26).south(4, 32, 0, 26).up(8, 50, 4, 46).down(27, 46, 23, 50)).armor(), PartPose.rotation(0.1309f, 0, 0));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().east(57, 22, 53, 20).west(57, 26, 53, 24).north(57, 12, 53, 10).south(57, 24, 53, 22).up(53, 25, 49, 21).down(53, 25, 49, 29)), PartPose.rotation(0.3491f, 0, 0));
        left_leg_shin.addOrReplaceChild("armor_left_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().east(8, 29, 4, 25).west(4, 29, 8, 25).north(53, 26, 49, 24).south(8, 29, 4, 25).up(8, 29, 4, 25).down(4, 46, 0, 50)).armor(), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().east(12, 44, 8, 36).west(20, 44, 16, 36).north(36, 43, 32, 35).south(16, 44, 12, 36).up(53, 8, 49, 4).down(53, 8, 49, 12)), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("armor_left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().east(12, 29, 8, 20).west(12, 29, 8, 20).north(4, 29, 8, 20).south(16, 29, 12, 20).up(8, 20, 4, 16).down(4, 25, 8, 29)).armor(), PartPose.rotation(0.3491f, 0, 0));

        return ModelDefinition.create(modelBuilder, 128, 128, 2);
    }
}