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

public class YufengDragonModel <E extends LivingEntity> extends CustomHumanoidModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(TransfurRegistry.YUFENG_DRAGON_TF.getId(), "main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/yufeng_dragon");

    public YufengDragonModel() {
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
        KeyframeAnimator.animate(tail, root(), Animations.TAIL_DRAGON, ageInTicks);
    }

    public static ModelDefinition bodyLayer() {
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new CubeUV().up(24, 24, 16, 16).east(16, 20, 8, 12).down(8, 20, 0, 28).north(8, 20, 0, 12).west(24, 16, 16, 8).south(24, 8, 16, 0)), PartPose.offset(0, 24, 0));
        head.addOrReplaceChild("right_ear", GroupBuilder.create()
                .addMesh(new float[]{-1, 5, 4, -1, 5, -1, -1, -2, 4, -1, -2, -1, 1, 5, 4, 1, 5, -1, 1, -2, 4, 1, -2, -1, -0.99f, 2, 4, -0.99f, 5, 1, 0.99f, 2, 4, 0.99f, 5, 1}, new float[]{0, 33, 12, 1, 28, 12, 3, 28, 19, 2, 33, 19, 5, 33, 19, 4, 28, 19, 6, 28, 26, 7, 33, 26, 5, 43, 25, 1, 41, 25, 0, 41, 30, 4, 43, 30, 4, 14, 37, 0, 12, 37, 2, 12, 44, 6, 14, 44, 1, 10, 50, 5, 8, 50, 7, 8, 57, 3, 10, 57, 11, 58, 17, 9, 56, 17, 8, 56, 21, 10, 58, 21}), PartPose.offsetAndRotation(3, 7, 0, 0.4276f, 0.384f, -0.3665f));
        head.addOrReplaceChild("left_ear", GroupBuilder.create()
                .addMesh(new float[]{1, 5, 4, 1, 5, -1, 1, -2, 4, 1, -2, -1, -1, 5, 4, -1, 5, -1, -1, -2, 4, -1, -2, -1, 0.99f, 2, 4, 0.99f, 5, 1, -0.99f, 2, 4, -0.99f, 5, 1}, new float[]{3, 33, 33, 1, 33, 26, 0, 28, 26, 2, 28, 33, 6, 37, 7, 4, 37, 0, 5, 32, 0, 7, 32, 7, 0, 51, 38, 1, 51, 33, 5, 49, 33, 4, 49, 38, 2, 12, 57, 0, 12, 50, 4, 10, 50, 6, 10, 57, 7, 26, 57, 5, 26, 50, 1, 24, 50, 3, 24, 57, 8, 58, 51, 9, 58, 47, 11, 56, 47, 10, 56, 51}), PartPose.offsetAndRotation(-3, 7, 0, 0.4276f, -0.384f, 0.3665f));
        head.addOrReplaceChild("right_horn", GroupBuilder.create()
                .addMesh(new float[]{0.1569f, 0.3567f, 1.1197f, -0.2477f, 0.8515f, 0.4602f, 0.8938f, 0.2441f, 0.5832f, 0.4892f, 0.7389f, -0.0763f, -0.198f, 0.6274f, 0.901f, 0.6092f, 0.1979f, 0.9881f, 0.8442f, 0.4682f, 0.1423f, 0.037f, 0.8977f, 0.0553f, 0.6149f, 1.8943f, 1.0331f, 0.7573f, 1.9174f, 0.8306f, 0.6398f, 1.7823f, 1.2535f, 1.2851f, 2.539f, 1.4253f, 0.8172f, 1.6469f, 1.3628f, 1.0434f, 1.5676f, 1.297f, 1.1857f, 1.5907f, 1.0946f, 1.1609f, 1.7027f, 0.8742f, 0.9834f, 1.838f, 0.7648f, -1.1124f, -0.6909f, -0.273f, -0.7803f, -0.637f, -0.7453f, -0.2527f, -0.8222f, -0.8989f, 0.1614f, -1.138f, -0.6438f, 0.2193f, -1.3995f, -0.1294f, -0.1128f, -1.4533f, 0.3429f, -0.6404f, -1.2681f, 0.4965f, -1.0545f, -0.9523f, 0.2414f}, new float[]{10, 63, 18.6113f, 8, 62.8563f, 18.4086f, 9, 62.6113f, 18.3668f, 11, 62.6834f, 18.6834f, 13, 62.7554f, 19, 12, 62.9582f, 18.8563f, 10, 63, 18.6113f, 11, 62.6834f, 18.6834f, 15, 62.3668f, 18.7554f, 14, 62.5104f, 18.9582f, 13, 62.7554f, 19, 11, 62.6834f, 18.6834f, 9, 62.6113f, 18.3668f, 16, 62.4086f, 18.5104f, 15, 62.3668f, 18.7554f, 11, 62.6834f, 18.6834f, 8, 42.2175f, 59.3429f, 10, 42, 59.4632f, 4, 42.5995f, 60.805f, 1, 43.0346f, 60.5646f, 9, 15.3936f, 32.0967f, 8, 15.1472f, 32.1292f, 1, 15.2156f, 33.5972f, 7, 15.7084f, 33.5322f, 12, 60.7229f, 47, 13, 60.4885f, 47.0825f, 5, 60.8571f, 48.5052f, 0, 61.326f, 48.3403f, 10, 59.2084f, 24, 12, 59, 24.1356f, 0, 59.6947f, 25.4306f, 4, 60.1113f, 25.1596f, 14, 61.907f, 13.1132f, 15, 61.6857f, 13, 6, 60.9082f, 14.2472f, 2, 61.3507f, 14.4735f, 13, 52, 61.0247f, 14, 51.7527f, 61, 2, 51.483f, 62.4447f, 5, 51.9776f, 62.4942f, 16, 62.8717f, 21.3428f, 9, 62.6443f, 21.2425f, 7, 61.9392f, 22.5319f, 3, 62.3939f, 22.7327f, 15, 60.2031f, 3.5872f, 16, 59.9955f, 3.4505f, 3, 59.0866f, 4.6054f, 6, 59.5018f, 4.8787f, 1, 15.2156f, 33.5972f, 17, 15.4251f, 35.5f, 18, 16, 35.4241f, 7, 15.7084f, 33.5322f, 7, 61.9392f, 22.5319f, 18, 61.1283f, 24.2658f, 19, 61.6588f, 24.5f, 3, 62.3939f, 22.7327f, 3, 59.0866f, 4.6054f, 19, 58, 6.1812f, 20, 58.4844f, 6.5f, 6, 59.5018f, 4.8787f, 6, 60.9082f, 14.2472f, 20, 60, 15.9322f, 21, 60.5162f, 16.1963f, 2, 61.3507f, 14.4735f, 2, 51.483f, 62.4447f, 21, 51.2511f, 64.3447f, 22, 51.8281f, 64.4025f, 5, 51.9776f, 62.4942f, 5, 60.8571f, 48.5052f, 22, 61.4529f, 50.3243f, 23, 62, 50.1319f, 0, 61.3261f, 48.3403f, 0, 59.6947f, 25.4306f, 23, 60.7035f, 27.0573f, 24, 61.1896f, 26.7411f, 4, 60.1114f, 25.1595f, 4, 42.5995f, 60.805f, 24, 43.4889f, 62.5f, 17, 43.9964f, 62.2195f, 1, 43.0345f, 60.5646f}), PartPose.offset(3.8f, 8.4f, -2));
        head.addOrReplaceChild("left_horn", GroupBuilder.create()
                .addMesh(new float[]{-0.1026f, 0.4893f, 0.8872f, 0.302f, 0.984f, 0.2276f, -0.8395f, 0.3767f, 0.3507f, -0.4349f, 0.8715f, -0.3089f, 0.2523f, 0.76f, 0.6685f, -0.5549f, 0.3305f, 0.7555f, -0.7899f, 0.6007f, -0.0902f, 0.0173f, 1.0302f, -0.1772f, -0.5606f, 2.0269f, 0.8005f, -0.703f, 2.05f, 0.5981f, -0.5855f, 1.9149f, 1.021f, -1.2308f, 2.6715f, 1.1927f, -0.7629f, 1.7795f, 1.1303f, -0.989f, 1.7001f, 1.0645f, -1.1314f, 1.7232f, 0.8621f, -1.1065f, 1.8353f, 0.6416f, -0.9291f, 1.9706f, 0.5323f, 1.1667f, -0.5583f, -0.5055f, 0.8346f, -0.5044f, -0.9778f, 0.307f, -0.6896f, -1.1314f, -0.1071f, -1.0055f, -0.8763f, -0.165f, -1.2669f, -0.3619f, 0.1671f, -1.3208f, 0.1104f, 0.6947f, -1.1355f, 0.264f, 1.1088f, -0.8197f, 0.0089f}, new float[]{9, 62.3887f, 19.3668f, 8, 62.1437f, 19.4086f, 10, 62, 19.6113f, 11, 62.3166f, 19.6834f, 10, 62, 19.6113f, 12, 62.0418f, 19.8563f, 13, 62.2446f, 20, 11, 62.3166f, 19.6834f, 13, 62.2446f, 20, 14, 62.4896f, 19.9582f, 15, 62.6332f, 19.7554f, 11, 62.3166f, 19.6834f, 15, 62.6332f, 19.7554f, 16, 62.5914f, 19.5104f, 9, 62.3887f, 19.3668f, 11, 62.3166f, 19.6834f, 4, 61.4005f, 4.805f, 10, 62, 3.4632f, 8, 61.7825f, 3.3429f, 1, 60.9654f, 4.5646f, 1, 45.7844f, 6.5972f, 8, 45.8528f, 5.1292f, 9, 45.6064f, 5.0967f, 7, 45.2916f, 6.5322f, 5, 62.1429f, 52.5052f, 13, 62.5115f, 51.0825f, 12, 62.2771f, 51, 0, 61.674f, 52.3403f, 0, 60.8053f, 60.4306f, 12, 61.5f, 59.1356f, 10, 61.2916f, 59, 4, 60.3887f, 60.1596f, 6, 57.0918f, 60.2472f, 15, 56.3143f, 59, 14, 56.093f, 59.1132f, 2, 56.6493f, 60.4735f, 2, 62.517f, 7.4447f, 14, 62.2473f, 6, 13, 62, 6.0247f, 5, 62.0224f, 7.4942f, 7, 62.0608f, 1.5319f, 9, 61.3557f, 0.2425f, 16, 61.1283f, 0.3428f, 3, 61.6061f, 1.7327f, 3, 59.4134f, 48.6054f, 16, 58.5045f, 47.4505f, 15, 58.2969f, 47.5872f, 6, 58.9982f, 48.8787f, 18, 45, 8.4241f, 17, 45.5749f, 8.5f, 1, 45.7844f, 6.5972f, 7, 45.2916f, 6.5322f, 19, 62.3412f, 3.5f, 18, 62.8717f, 3.2658f, 7, 62.0608f, 1.5319f, 3, 61.6061f, 1.7327f, 20, 60.0156f, 50.5f, 19, 60.5f, 50.1812f, 3, 59.4134f, 48.6054f, 6, 58.9982f, 48.8787f, 21, 57.4838f, 62.1963f, 20, 58, 61.9322f, 6, 57.0918f, 60.2472f, 2, 56.6493f, 60.4735f, 22, 62.1719f, 9.4025f, 21, 62.7489f, 9.3447f, 2, 62.517f, 7.4447f, 5, 62.0224f, 7.4942f, 23, 61, 54.1319f, 22, 61.5471f, 54.3243f, 5, 62.1429f, 52.5052f, 0, 61.6739f, 52.3403f, 24, 59.3104f, 61.7411f, 23, 59.7965f, 62.0573f, 0, 60.8053f, 60.4306f, 4, 60.3886f, 60.1595f, 17, 60.0036f, 6.2195f, 24, 60.5111f, 6.5f, 4, 61.4005f, 4.805f, 1, 60.9655f, 4.5646f}), PartPose.offset(-3.8f, 8.4f, -2));
        head.addOrReplaceChild("maw", GroupBuilder.create()
                .addBox(-2f, -1f, -2f, 4, 2, 2, new CubeUV().up(58, 43, 54, 41).east(39, 7, 37, 5).down(60, 15, 56, 17).north(58, 39, 54, 37).west(49, 27, 47, 25).south(58, 41, 54, 39)), PartPose.offset(0, 2, -4));
        head.addOrReplaceChild("maw_i0", GroupBuilder.create()
                .addBox(-1.5f, -1f, -1.5f, 3, 1, 1.5f, new CubeUV().up(64, 46.5f, 61, 45).east(53.5f, 16, 52, 15).down(65, 4, 62, 5.5f).north(41, 26, 38, 25).west(55.5f, 16, 54, 15).south(27, 45, 24, 44)), PartPose.offset(0, 1, -4));
        head.addOrReplaceChild("armor_head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new Vector3f(0.6f), new CubeUV().up(16, 8, 8, 0).east(8, 16, 0, 8).down(24, 0, 16, 8).north(16, 16, 8, 8).west(24, 16, 16, 8).south(32, 16, 24, 8)).armor());
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new CubeUV().up(41, 30, 33, 26).east(12, 32, 8, 20).down(36, 33, 28, 37).north(8, 12, 0, 0).west(16, 32, 12, 20).south(16, 12, 8, 0)), PartPose.offset(0, 12, 0));
        GroupDefinition wings = body.addOrReplaceChild("wings", GroupBuilder.create()
                .addBox(-1f, -3f, -1f, 2, 5, 1, new CubeUV().up(50, 17, 48, 16).east(43, 20, 42, 15).down(51, 38, 49, 39).north(56, 5, 54, 0).west(59, 22, 58, 17).south(56, 10, 54, 5)), PartPose.offset(0, 8, 3));
        wings.addOrReplaceChild("right", GroupBuilder.create()
                .addBox(7.0319f, -9.9557f, 0.5664f, 1, 17, 1, new CubeUV().up(28, 37, 27, 36).east(30, 62, 29, 45).down(36, 37, 35, 38).north(29, 62, 28, 45).west(32, 62, 31, 45).south(31, 62, 30, 45))
                .addBox(8.0319f, -8.9557f, 0.5664f, 1, 14, 1, new CubeUV().up(44, 28, 43, 27).east(27, 64, 26, 50).down(44, 28, 43, 29).north(47, 14, 46, 0).west(52, 47, 51, 33).south(28, 64, 27, 50))
                .addBox(9.0319f, -6.9557f, 0.5664f, 1, 9, 1, new CubeUV().up(44, 30, 43, 29).east(56, 37, 55, 28).down(45, 35, 44, 36).north(55, 37, 54, 28).west(54, 64, 53, 55).south(53, 64, 52, 55))
                .addBox(5.0319f, -6.9557f, 0.5664f, 1, 14, 1, new CubeUV().up(47, 15, 46, 14).east(52, 61, 51, 47).down(51, 16, 50, 17).north(48, 65, 47, 51).west(50, 65, 49, 51).south(49, 65, 48, 51))
                .addBox(6.0319f, -8.9557f, 0.5664f, 1, 17, 1, new CubeUV().up(53, 15, 52, 14).east(34, 62, 33, 45).down(57, 9, 56, 10).north(33, 62, 32, 45).west(1, 63, 0, 46).south(35, 62, 34, 45))
                .addBox(4.0319f, -5.9557f, 0.5664f, 1, 12, 1, new CubeUV().up(57, 44, 56, 43).east(54, 12, 53, 0).down(58, 36, 57, 37).north(47, 64, 46, 52).west(14, 65, 13, 53).south(13, 65, 12, 53))
                .addBox(3.0319f, -5.9557f, 0.5664f, 1, 11, 1, new CubeUV().up(59, 4, 58, 3).east(20, 64, 19, 53).down(59, 7, 58, 8).north(19, 64, 18, 53).west(22, 64, 21, 53).south(21, 64, 20, 53))
                .addBox(2.0319f, -4.9557f, 0.5664f, 1, 8, 1, new CubeUV().up(59, 50, 58, 49).east(58, 8, 57, 0).down(59, 50, 58, 51).north(57, 59, 56, 51).west(10, 65, 9, 57).south(9, 65, 8, 57))
                .addBox(1.0319f, -4.9557f, 0.5664f, 1f, 7, 1, new CubeUV().up(60, 7, 59, 6).east(45, 64, 44, 57).down(60, 7, 59, 8).north(15, 44, 14, 37).west(59, 43, 58, 36).south(46, 64, 45, 57))
                .addBox(0.0319f, -3.9557f, 0.0664f, 1f, 6, 1, new CubeUV().up(60, 15, 59, 14).east(61, 42, 60, 36).down(60, 26, 59, 27).north(61, 23, 60, 17).west(62, 12, 61, 6).south(59, 66, 58, 60)), PartPose.rotation(-0.0443f, -0.1744f, 0.0077f));
        wings.addOrReplaceChild("left", GroupBuilder.create()
                .addBox(-8.1318f, -10f, -0.324f, 1f, 17, 1, new CubeUV().up(60, 28, 59, 27).east(3, 63, 2, 46).down(44, 59, 43, 60).north(2, 63, 1, 46).west(5, 63, 4, 46).south(4, 63, 3, 46))
                .addBox(-9.1318f, -9f, -0.324f, 1, 14, 1, new CubeUV().up(60, 48, 59, 47).east(53, 14, 52, 0).down(61, 7, 60, 8).north(51, 65, 50, 51).west(36, 66, 35, 52).south(53, 47, 52, 33))
                .addBox(-10.1318f, -7f, -0.324f, 1, 9, 1, new CubeUV().up(61, 24, 60, 23).east(56, 64, 55, 55).down(61, 24, 60, 25).north(55, 64, 54, 55).west(57, 37, 56, 28).south(57, 9, 56, 0))
                .addBox(-6.1318f, -7f, -0.324f, 1, 14, 1, new CubeUV().up(61, 43, 60, 42).east(38, 66, 37, 52).down(61, 57, 60, 58).north(37, 66, 36, 52).west(40, 66, 39, 52).south(39, 66, 38, 52))
                .addBox(-7.1318f, -9f, -0.324f, 1, 17, 1, new CubeUV().up(61, 59, 60, 58).east(7, 63, 6, 46).down(62, 12, 61, 13).north(6, 63, 5, 46).west(48, 17, 47, 0).south(8, 63, 7, 46))
                .addBox(-5.1318f, -6f, -0.324f, 1, 12, 1, new CubeUV().up(62, 26, 61, 25).east(16, 65, 15, 53).down(62, 58, 61, 59).north(15, 65, 14, 53).west(18, 65, 17, 53).south(17, 65, 16, 53))
                .addBox(-4.1318f, -6f, -0.324f, 1, 11, 1, new CubeUV().up(63, 1, 62, 0).east(54, 33, 53, 22).down(63, 10, 62, 11).north(23, 64, 22, 53).west(54, 44, 53, 33).south(24, 64, 23, 53))
                .addBox(-3.1318f, -5f, -0.324f, 1, 8, 1, new CubeUV().up(63, 12, 62, 11).east(12, 65, 11, 57).down(63, 12, 62, 13).north(11, 65, 10, 57).west(26, 65, 25, 57).south(25, 65, 24, 57))
                .addBox(-2.1318f, -5f, -0.324f, 1, 7, 1, new CubeUV().up(63, 14, 62, 13).east(60, 43, 59, 36).down(63, 14, 62, 15).north(60, 24, 59, 17).west(42, 66, 41, 59).south(41, 66, 40, 59))
                .addBox(-1.1318f, -4f, -0.824f, 1, 6, 1, new CubeUV().up(63, 16, 62, 15).east(62, 33, 61, 27).down(63, 16, 62, 17).north(62, 22, 61, 16).west(62, 45, 61, 39).south(62, 39, 61, 33)), PartPose.rotation(-0.0443f, 0.1744f, -0.0077f));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addBox(-2.4f, -2.4f, -3f, 4.8f, 4.8f, 7, new CubeUV().up(38, 19, 33, 12).east(39, 12, 32, 7).down(38, 19, 33, 26).north(42, 5, 37, 0).west(15, 37, 8, 32).south(43, 25, 38, 20)), PartPose.offsetAndRotation(0, 0, 2, 0.7418f, 0, 0));
        GroupDefinition tail_0 = tail.addOrReplaceChild("tail0", GroupBuilder.create()
                .addBox(-2.1f, -1.8f, -0.5f, 4.2f, 4.2f, 6, new CubeUV().up(4, 46, 0, 40).east(41, 42, 35, 38).down(8, 40, 4, 46).north(49, 39, 45, 35).west(45, 9, 39, 5).south(51, 21, 47, 17)), PartPose.offsetAndRotation(0, 0, 4, -0.1309f, 0, 0));
        GroupDefinition tail_1 = tail_0.addOrReplaceChild("tail1", GroupBuilder.create()
                .addBox(-1.8f, -1.3f, 0f, 3.6f, 3.7f, 5, new CubeUV().up(11.5f, 50, 8, 45).east(49, 30.5f, 44, 27).down(27.5f, 45, 24, 50).north(55.5f, 50.5f, 52, 47).west(49, 34.5f, 44, 31).south(55.5f, 54.5f, 52, 51)), PartPose.offsetAndRotation(0, 0, 5, -0.1309f, 0, 0));
        GroupDefinition tail_2 = tail_1.addOrReplaceChild("tail2", GroupBuilder.create()
                .addBox(-1.5f, -1.3f, -0.5f, 3, 3.2f, 4.5f, new CubeUV().up(43, 56.5f, 40, 52).east(55.5f, 19, 51, 16).down(46, 52, 43, 56.5f).north(36, 33, 33, 30).west(55.5f, 22, 51, 19).south(42, 12, 39, 9)), PartPose.offsetAndRotation(0, 0.5f, 5, -0.0873f, 0, 0));
        GroupDefinition tail_3 = tail_2.addOrReplaceChild("tail3", GroupBuilder.create()
                .addBox(-1f, -1f, 0f, 2, 2.4f, 3, new CubeUV().up(47, 42, 45, 39).east(60, 59.5f, 57, 57).down(53, 22, 51, 25).north(63, 57.5f, 61, 55).west(61, 2.5f, 58, 0).south(63, 63.5f, 61, 61)), PartPose.offsetAndRotation(0, 0.5f, 4, -0.0873f, 0, 0));
        tail_3.addOrReplaceChild("armor_tail_3", GroupBuilder.create()
                .addBox(-1f, -1f, 0f, 2, 2.4f, 3, new Vector3f(0.6f), new CubeUV().up(8, 29, 4, 25).east(8, 29, 4, 25).down(8, 25, 4, 29).west(8, 29, 4, 25).south(8, 29, 4, 25)).armor());
        tail_2.addOrReplaceChild("armor_tail_2", GroupBuilder.create()
                .addBox(-1.5f, -1.3f, -0.5f, 3, 3.2f, 4.5f, new Vector3f(0.6f), new CubeUV().up(8, 29, 4, 25).east(8, 29, 4, 25).down(8, 25, 4, 29).west(8, 29, 4, 25).south(8, 29, 4, 25)).armor());
        tail_1.addOrReplaceChild("armor_tail_1", GroupBuilder.create()
                .addBox(-1.8f, -1.3f, 0f, 3.6f, 3.7f, 5, new Vector3f(0.6f), new CubeUV().up(8, 29, 4, 24).east(8, 29, 4, 24).down(8, 24, 4, 29).west(8, 29, 4, 24).south(8, 29, 4, 25)).armor());
        tail_0.addOrReplaceChild("armor_tail_0", GroupBuilder.create()
                .addBox(-2.1f, -1.8f, -0.5f, 4.2f, 4.2f, 6, new Vector3f(0.6f), new CubeUV().up(8, 29, 4, 24).east(8, 29, 4, 24).down(8, 24, 4, 29).west(8, 29, 4, 24).south(8, 29, 4, 25)).armor());
        tail.addOrReplaceChild("armor_tail", GroupBuilder.create()
                .addBox(-2.4f, -2.4f, -3f, 4.8f, 4.8f, 7, new Vector3f(0.6f), new CubeUV().up(8, 29, 4, 23).east(8, 29, 4, 23).down(8, 23, 4, 29).west(8, 29, 4, 23).south(8, 29, 4, 25)).armor());
        body.addOrReplaceChild("armor_body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new Vector3f(0.6f), new CubeUV().up(40, 11, 32, 7).east(32, 32, 28, 20).down(40, 34, 32, 38).north(28, 32, 20, 20).west(20, 32, 16, 20).south(40, 32, 32, 20)).armor());
        GroupDefinition right_arm = root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new CubeUV().up(51, 25, 47, 21).east(28, 24, 24, 12).down(51, 39, 47, 43).north(28, 12, 24, 0).west(24, 36, 20, 24).south(20, 36, 16, 24)), PartPose.offset(4, 22, 0));
        right_arm.addOrReplaceChild("armor_right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().up(48, 20, 44, 16).east(44, 32, 40, 20).north(48, 32, 44, 20).west(52, 32, 48, 20).south(56, 32, 52, 20)).armor());
        GroupDefinition left_arm = root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new CubeUV().up(51, 47, 47, 43).east(4, 40, 0, 28).down(51, 47, 47, 51).north(28, 36, 24, 24).west(8, 40, 4, 28).south(32, 12, 28, 0)), PartPose.offset(-4, 22, 0));
        left_arm.addOrReplaceChild("armor_left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().up(44, 20, 48, 16).east(52, 32, 48, 20).north(56, 32, 52, 20).west(40, 32, 44, 20).south(48, 32, 44, 20)).armor());
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2, 13, 0, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create());
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new CubeUV().up(46, 5, 42, 0).east(59, 12, 54, 10).down(16, 44, 12, 49).north(61, 34, 57, 32).west(59, 24, 54, 22).south(61, 36, 57, 34))
                .addMesh(new float[]{1.6929f, -6.025f, -2, 1.4f, -6.025f, -2.7071f, 2.4f, -6.025f, -2.2929f, 2.1071f, -6.025f, -3, 1.9f, -6.025f, -2.5f, 1.4f, -6.025f, -2.2929f, 2.1071f, -6.025f, -2, 2.4f, -6.025f, -2.7071f, 1.6929f, -6.025f, -3, 0.4929f, -6.025f, -2, 0.2f, -6.025f, -2.7071f, 1.2f, -6.025f, -2.2929f, 0.9071f, -6.025f, -3, 0.7f, -6.025f, -2.5f, 0.2f, -6.025f, -2.2929f, 0.9071f, -6.025f, -2, 1.2f, -6.025f, -2.7071f, 0.4929f, -6.025f, -3, -0.7071f, -6.025f, -2, -1, -6.025f, -2.7071f, -0f, -6.025f, -2.2929f, -0.2929f, -6.025f, -3, -0.5f, -6.025f, -2.5f, -1, -6.025f, -2.2929f, -0.2929f, -6.025f, -2, -0f, -6.025f, -2.7071f, -0.7071f, -6.025f, -3, -0.915f, -6.025f, 1.025f, -0.725f, -6.025f, -1.325f, 2.315f, -6.025f, 1.025f, 2.125f, -6.025f, -1.325f, 0.7f, -6.025f, -0.3f, -1.2f, -6.025f, -0f, 0.7f, -6.025f, 0.9f, 2.6f, -6.025f, -0f, 0.7f, -6.025f, -1.8f}, new float[]{5, 57.25f, 14.2929f, 1, 57.25f, 14.7071f, 8, 57.5429f, 15, 4, 57.75f, 14.5f, 6, 57.9571f, 14, 0, 57.5429f, 14, 5, 57.25f, 14.2929f, 4, 57.75f, 14.5f, 7, 58.25f, 14.7071f, 2, 58.25f, 14.2929f, 6, 57.9571f, 14, 4, 57.75f, 14.5f, 8, 57.5429f, 15, 3, 57.9571f, 15, 7, 58.25f, 14.7071f, 4, 57.75f, 14.5f, 14, 54.25f, 43.2929f, 10, 54.25f, 43.7071f, 17, 54.5429f, 44, 13, 54.75f, 43.5f, 15, 54.9571f, 43, 9, 54.5428f, 43f, 14, 54.25f, 43.2929f, 13, 54.75f, 43.5f, 16, 55.25f, 43.7071f, 11, 55.25f, 43.293f, 15, 54.9571f, 43.0001f, 13, 54.75f, 43.5f, 17, 54.5429f, 44, 12, 54.9571f, 44, 16, 55.25f, 43.7071f, 13, 54.75f, 43.5f, 23, 40f, 36.7929f, 19, 40f, 37.2071f, 26, 40.2929f, 37.5f, 22, 40.5f, 37f, 24, 40.7071f, 36.5f, 18, 40.2929f, 36.5f, 23, 40f, 36.7929f, 22, 40.5f, 37f, 25, 41f, 37.2071f, 20, 41f, 36.7929f, 24, 40.707f, 36.4999f, 22, 40.5f, 37f, 26, 40.2929f, 37.5f, 21, 40.7071f, 37.5f, 25, 41f, 37.2071f, 22, 40.5f, 37, 32, 53.1f, 13.2f, 28, 53.575f, 14.525f, 35, 55f, 15, 31, 55, 13.5f, 33, 55f, 12.3f, 27, 53.385f, 12.175f, 32, 53.1f, 13.2f, 31, 55f, 13.5f, 34, 56.9f, 13.2f, 29, 56.615f, 12.175f, 33, 55f, 12.3f, 31, 55f, 13.5f, 35, 55f, 15, 30, 56.425f, 14.525f, 34, 56.9f, 13.2f, 31, 55, 13.5f}), PartPose.rotation(0, 0, -0.1309f));
        right_foot.addOrReplaceChild("armor_right_foot", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().up(12, 20, 8, 16).east(12, 20, 8, 16).down(12, 16, 8, 20).north(12, 20, 8, 16).west(12, 20, 8, 16).south(12, 20, 8, 16)).armor(), PartPose.rotation(0, 0, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().up(39, 52, 35, 48).east(45, 42, 41, 36).down(43, 48, 39, 52).north(44, 36, 40, 30).west(39, 48, 35, 42).south(46, 15, 42, 9)), PartPose.rotation(0.1309f, 0, 0));
        right_leg_.addOrReplaceChild("armor_right_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().up(49, 16, 45, 12).east(8, 32, 4, 26).down(49, 16, 45, 20).north(4, 32, 0, 26).west(8, 32, 4, 26).south(4, 32, 0, 26)).armor(), PartPose.rotation(0.1309f, 0, 0));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().up(52, 12, 48, 8).east(61, 14, 57, 12).down(52, 12, 48, 16).north(61, 10, 57, 8).west(61, 32, 57, 30).south(61, 30, 57, 28)), PartPose.rotation(0.3491f, 0, 0));
        right_leg_shin.addOrReplaceChild("armor_right_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().up(8, 29, 4, 25).east(8, 29, 4, 25).down(49, 8, 45, 12).west(8, 29, 4, 25).south(8, 29, 4, 25)).armor(), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().up(52, 4, 48, 0).east(23, 44, 19, 36).down(52, 4, 48, 8).north(19, 44, 15, 36).west(40, 38, 36, 30).south(27, 44, 23, 36)), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("armor_right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().up(8, 20, 4, 16).east(12, 29, 8, 20).down(8, 25, 4, 29).north(8, 29, 4, 20).west(12, 29, 8, 20).south(16, 29, 12, 20)).armor(), PartPose.rotation(0.3491f, 0, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2, 13, 0, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create());
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new CubeUV().up(20, 49, 16, 44).east(59, 26, 54, 24).down(24, 44, 20, 49).north(61, 55, 57, 53).west(59, 28, 54, 26).south(61, 57, 57, 55))
                .addMesh(new float[]{-1.6929f, -6.025f, -2, -1.4f, -6.025f, -2.7071f, -2.4f, -6.025f, -2.2929f, -2.1071f, -6.025f, -3, -1.9f, -6.025f, -2.5f, -1.4f, -6.025f, -2.2929f, -2.1071f, -6.025f, -2, -2.4f, -6.025f, -2.7071f, -1.6929f, -6.025f, -3, -0.4929f, -6.025f, -2, -0.2f, -6.025f, -2.7071f, -1.2f, -6.025f, -2.2929f, -0.9071f, -6.025f, -3, -0.7f, -6.025f, -2.5f, -0.2f, -6.025f, -2.2929f, -0.9071f, -6.025f, -2, -1.2f, -6.025f, -2.7071f, -0.4929f, -6.025f, -3, 0.7071f, -6.025f, -2, 1, -6.025f, -2.7071f, 0f, -6.025f, -2.2929f, 0.2929f, -6.025f, -3, 0.5f, -6.025f, -2.5f, 1, -6.025f, -2.2929f, 0.2929f, -6.025f, -2, 0f, -6.025f, -2.7071f, 0.7071f, -6.025f, -3, 0.915f, -6.025f, 1.025f, 0.725f, -6.025f, -1.325f, -2.315f, -6.025f, 1.025f, -2.125f, -6.025f, -1.325f, -0.7f, -6.025f, -0.3f, 1.2f, -6.025f, -0f, -0.7f, -6.025f, 0.9f, -2.6f, -6.025f, -0f, -0.7f, -6.025f, -1.8f}, new float[]{8, 62.7071f, 18, 1, 63, 17.7071f, 5, 63, 17.2929f, 4, 62.5f, 17.5f, 5, 63, 17.2929f, 0, 62.7071f, 17, 6, 62.2929f, 17, 4, 62.5f, 17.5f, 6, 62.2929f, 17, 2, 62f, 17.2929f, 7, 62, 17.7071f, 4, 62.5f, 17.5f, 7, 62, 17.7071f, 3, 62.2929f, 18, 8, 62.7071f, 18, 4, 62.5f, 17.5f, 17, 56.9571f, 22, 10, 57.25f, 21.7071f, 14, 57.25f, 21.2929f, 13, 56.75f, 21.5f, 14, 57.25f, 21.2929f, 9, 56.9571f, 21f, 15, 56.5429f, 21, 13, 56.75f, 21.5f, 15, 56.5429f, 21, 11, 56.25f, 21.2929f, 16, 56.25f, 21.707f, 13, 56.75f, 21.5f, 16, 56.25f, 21.7071f, 12, 56.5429f, 22, 17, 56.9571f, 22, 13, 56.75f, 21.5f, 26, 59.9571f, 11.5f, 19, 60.25f, 11.2071f, 23, 60.25f, 10.7929f, 22, 59.75f, 11f, 23, 60.25f, 10.7929f, 18, 59.9571f, 10.5f, 24, 59.5429f, 10.5f, 22, 59.75f, 11f, 24, 59.5429f, 10.5f, 20, 59.25f, 10.7929f, 25, 59.2501f, 11.2071f, 22, 59.75f, 11f, 25, 59.2501f, 11.2071f, 21, 59.5429f, 11.5f, 26, 59.9571f, 11.5f, 22, 59.75f, 11f, 35, 55f, 47, 28, 56.425f, 46.525f, 32, 56.9f, 45.2f, 31, 55f, 45.5f, 32, 56.9f, 45.2f, 27, 56.615f, 44.175f, 33, 55, 44.3f, 31, 55, 45.5f, 33, 55, 44.3f, 29, 53.385f, 44.175f, 34, 53.1f, 45.2f, 31, 55, 45.5f, 34, 53.1f, 45.2f, 30, 53.575f, 46.525f, 35, 55f, 47, 31, 55f, 45.5f}), PartPose.rotation(0, 0, 0.1309f));
        left_foot.addOrReplaceChild("armor_left_foot", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().up(12, 20, 8, 16).east(12, 20, 8, 16).down(8, 16, 12, 20).north(8, 20, 12, 16).west(12, 16, 8, 20).south(12, 20, 8, 16)).armor(), PartPose.rotation(0, 0, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().up(53, 29, 49, 25).east(47, 21, 43, 15).down(53, 29, 49, 33).north(43, 48, 39, 42).west(47, 48, 43, 42).south(47, 27, 43, 21)), PartPose.rotation(0.1309f, 0, 0));
        left_leg_.addOrReplaceChild("armor_left_leg_", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().up(8, 50, 4, 46).east(8, 32, 4, 26).down(27, 46, 23, 50).north(0, 32, 4, 26).west(4, 32, 8, 26).south(4, 32, 0, 26)).armor(), PartPose.rotation(0.1309f, 0, 0));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().up(20, 53, 16, 49).east(61, 45, 57, 43).down(24, 49, 20, 53).north(44, 59, 40, 57).west(61, 53, 57, 51).south(61, 47, 57, 45)), PartPose.rotation(0.3491f, 0, 0));
        left_leg_shin.addOrReplaceChild("armor_left_leg_shin", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().up(8, 29, 4, 25).east(8, 29, 4, 25).down(4, 46, 0, 50).north(53, 26, 49, 24).west(4, 29, 8, 25).south(8, 29, 4, 25)).armor(), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().up(47, 52, 43, 48).east(31, 45, 27, 37).down(16, 49, 12, 53).north(12, 45, 8, 37).west(42, 20, 38, 12).south(35, 45, 31, 37)), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("armor_left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().up(8, 20, 4, 16).east(12, 29, 8, 20).down(4, 25, 8, 29).north(4, 29, 8, 20).west(12, 29, 8, 20).south(16, 29, 12, 20)).armor(), PartPose.rotation(0.3491f, 0, 0));

        return ModelDefinition.create(modelBuilder, 256, 256, 2);
    }
}