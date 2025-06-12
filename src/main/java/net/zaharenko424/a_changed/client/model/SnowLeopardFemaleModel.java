package net.zaharenko424.a_changed.client.model;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.animations.Animations;
import net.zaharenko424.a_changed.client.animations.FallFlyingAnim;
import net.zaharenko424.a_changed.client.animations.HumanoidAnim;
import net.zaharenko424.a_changed.client.animations.SwimAnim;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
import net.zaharenko424.a_changed.client.cmrs.animation.KeyframeAnimator;
import net.zaharenko424.a_changed.client.cmrs.api.ModelPropertyRegistry;
import net.zaharenko424.a_changed.client.cmrs.geom.CubeUV;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupBuilder;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import net.zaharenko424.a_changed.client.cmrs.model.PartTransform;
import net.zaharenko424.a_changed.client.cmrs.model.PoseTransform;
import net.zaharenko424.a_changed.client.cmrs.model.UniversalCustomModel;
import net.zaharenko424.a_changed.client.cmrs.properties.*;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.client.cmrs.util.Int2ObjArrayMap;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;

public class SnowLeopardFemaleModel<E extends LivingEntity>  extends UniversalCustomModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(TransfurRegistry.SNOW_LEOPARD_F_TF.getId(), "main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/snow_leopard_female");

    public SnowLeopardFemaleModel() {
        super(ModelDefinitionCache.getInstance().bake(bodyLayer), Util.make(new ModelPropertyMapImpl(), map -> {
            map.addLast(ModelPropertyRegistry.REMAP_UV.get(), Unit.INSTANCE);
            map.addLast(ModelPropertyRegistry.TEXTURES.get(), new Textures(Util.make(new Int2ObjArrayMap<>(2), m -> {
                m.put(0, Texture.fromAsset(TEXTURE, 2));
            })));
            map.addLast(ModelPropertyRegistry.HEAD.get(), "head");
            map.addLast(ModelPropertyRegistry.ITEM_ON_HEAD.get(), new ItemOnHead("head"));
            map.addLast(ModelPropertyRegistry.FP_ARMS.get(), new FPArms(
                    "right_arm", new PartTransform(false, new Vector3f(-6, 1.5f, -2), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * 5, Mth.DEG_TO_RAD * 180), true, new Vector3f(-.1f)),
                    "left_arm", new PartTransform(false, new Vector3f(5, 1.5f, 0), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * -5, Mth.DEG_TO_RAD * 180), true, new Vector3f(-.1f))
            ));
            map.addLast(ModelPropertyRegistry.ITEM_IN_HAND.get(), new ItemInHandLayer(
                    "right_arm", new PoseTransform(new Vector3f(1/16f, 0, 0), null, new Vector3f(-1, -1, 1)),
                    "left_arm", new PoseTransform(new Vector3f(-1/16f, 0, 0), null, new Vector3f(-1, -1, 1))
            ));
            map.addLast(ModelPropertyRegistry.ARMOR.get(), new Armor(Util.make(new Int2ObjectArrayMap<>(), m -> {
                m.put(2, ArmorItem.Type.HELMET);
                m.put(3, ArmorItem.Type.CHESTPLATE);
                m.put(4, ArmorItem.Type.LEGGINGS);
                m.put(5, ArmorItem.Type.BOOTS);
            }), new Int2ObjectArrayMap<>(0)));
            map.addLast(ModelPropertyRegistry.VANILLA_ELYTRA.get(), new VanillaElytra());
            map.addLast(ModelPropertyRegistry.TRIDENT_SPIN_EFFECT.get(), new TridentSpinEffect());
        }), Util.make(new ArrayList<>(4), l -> {
            l.add(HumanoidAnim.getInstance());
            l.add(FallFlyingAnim.getInstance());
            l.add(SwimAnim.getInstance());
        }));
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
        if(!entity.isInWaterOrBubble()) KeyframeAnimator.applyStatic(root(), Animations.STATIC_TAIL_LEO);
    }

    public static ModelDefinition model(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new CubeUV().south(24, 8, 16, 0).west(24, 16, 16, 8).up(24, 24, 16, 16).north(8, 20, 0, 12).east(16, 20, 8, 12).down(8, 20, 0, 28))
                .addBox(-4f, 0f, -4f, 8, 8, 8, new Vector3f(0.6f), new CubeUV().south(32, 16, 24, 8).west(24, 16, 16, 8).up(16, 8, 8, 0).north(16, 16, 8, 8).east(8, 16, 0, 8).down(24, 0, 16, 8), 2), PartPose.offset(0, 24, 0));
        head.addOrReplaceChild("right_ear", GroupBuilder.create()
                .addMesh(new float[]{-1, 5, -1, -1, 5, 4, -1, -2, -1, -1, -2, 4, 1, 5, -1, 1, 5, 4, 1, -2, -1, 1, -2, 4, -0.99f, 2, -1, -0.99f, 5, 2, 0.99f, 2, -1, 0.99f, 5, 2}, new float[]{3, 33, 19, 1, 33, 12, 0, 28, 12, 2, 28, 19, 6, 33, 26, 4, 33, 19, 5, 28, 19, 7, 28, 26, 0, 53, 33, 1, 53, 38, 5, 55, 38, 4, 55, 33, 2, 18, 55, 0, 18, 48, 4, 16, 48, 6, 16, 55, 7, 40, 56, 5, 40, 49, 1, 38, 49, 3, 38, 56, 8, 28, 40, 9, 28, 36, 11, 26, 36, 10, 26, 40}), PartPose.offsetAndRotation(3, 6, -1, -0.4276f, -0.1745f, -0.5236f));
        head.addOrReplaceChild("left_ear", GroupBuilder.create()
                .addMesh(new float[]{1, 5, -1, 1, 5, 4, 1, -2, -1, 1, -2, 4, -1, 5, -1, -1, 5, 4, -1, -2, -1, -1, -2, 4, 0.99f, 2, -1, 0.99f, 5, 2, -0.99f, 2, -1, -0.99f, 5, 2}, new float[]{0, 33, 26, 1, 28, 26, 3, 28, 33, 2, 33, 33, 5, 37, 0, 4, 32, 0, 6, 32, 7, 7, 37, 7, 5, 34, 58, 1, 36, 58, 0, 36, 53, 4, 34, 53, 4, 2, 50, 0, 0, 50, 2, 0, 57, 6, 2, 57, 1, 4, 50, 5, 2, 50, 7, 2, 57, 3, 4, 57, 11, 38, 53, 9, 36, 53, 8, 36, 57, 10, 38, 57}), PartPose.offsetAndRotation(-3, 6, -1, -0.4276f, 0.1745f, 0.5236f));
        head.addOrReplaceChild("maw", GroupBuilder.create()
                .addBox(-2f, -1f, -2f, 4, 2, 2, new CubeUV().south(58, 17, 54, 15).west(32, 48, 30, 46).up(58, 19, 54, 17).north(8, 56, 4, 54).east(20, 46, 18, 44).down(22, 54, 18, 56)), PartPose.offset(0, 2, -4));
        head.addOrReplaceChild("maw_i0", GroupBuilder.create()
                .addBox(-1.5f, -1f, -1.5f, 3, 1, 1.5f, new CubeUV().south(44, 6, 41, 5).west(46.5f, 43, 45, 42).up(49, 22.5f, 46, 21).north(40, 7, 37, 6).east(43.5f, 39, 42, 38).down(49, 35, 46, 36.5f)), PartPose.offset(0, 1, -4));
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new CubeUV().south(16, 12, 8, 0).west(16, 32, 12, 20).up(40, 11, 32, 7).north(8, 12, 0, 0).east(12, 32, 8, 20).down(16, 32, 8, 36))
                .addBox(-4f, 0f, -2f, 8, 12, 4, new Vector3f(0.6f), new CubeUV().south(40, 32, 32, 20).west(20, 32, 16, 20).up(40, 11, 32, 7).north(28, 32, 20, 20).east(32, 32, 28, 20).down(40, 34, 32, 38), 3)
                .addBox(-4f, 0f, -2f, 8, 3, 4, new Vector3f(0.5f), new CubeUV().south(40, 32, 32, 27).west(20, 32, 16, 27).up(40, 11, 32, 7).north(28, 32, 20, 27).east(32, 32, 28, 27).down(40, 34, 32, 38), 4), PartPose.offset(0, 12, 0));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addAnimatedMesh(new float[]{1.7f, 1.7f, 21.675f, 2.0884f, 2.0311f, 19.0096f, 1.7f, -1.7f, 21.675f, 2.0884f, -1.9689f, 19.0096f, -1.7f, 1.7f, 21.675f, -2.0884f, 2.0311f, 19.0096f, -1.7f, -1.7f, 21.675f, -2.0884f, -1.9689f, 19.0096f, 2.25f, 2.1904f, 15.9388f, 2.25f, -2.1063f, 15.9388f, -2.25f, 2.1904f, 15.9388f, -2.25f, -2.1063f, 15.9388f, 2.15f, 2.1181f, 11.4502f, 2.15f, -2.1786f, 11.4502f, -2.15f, 2.1181f, 11.4502f, 2.05f, 1.9825f, 7.2252f, 2.05f, -2.1175f, 7.2252f, -2.05f, 1.9825f, 7.2252f, -2.15f, -2.1786f, 11.4502f, -2.05f, -2.1175f, 7.2252f, 1.85f, 1.7978f, 2.3321f, 1.85f, -1.9022f, 2.3321f, -1.85f, 1.7978f, 2.3321f, -1.85f, -1.9022f, 2.3321f, 1.8f, 1.7549f, -2.0191f, 1.8f, -1.8451f, -2.0191f, -1.8f, 1.7549f, -2.0191f, -1.8f, -1.8451f, -2.0191f}, new float[]{3, 8, 53.5f, 1, 8, 50, 0, 4, 50, 2, 4, 53.5f, 6, 54, 15.5f, 4, 54, 12, 5, 50, 12, 7, 50, 15.5f, 0, 50, 16, 1, 50, 20, 5, 53.5f, 20, 4, 53.5f, 16, 3, 18, 54, 2, 18, 50, 6, 21.5f, 50, 7, 21.5f, 54, 2, 29.5f, 53.5f, 0, 29.5f, 50, 4, 26, 50, 6, 26, 53.5f, 9, 49, 13, 8, 49, 9, 1, 45, 9, 3, 45, 13, 7, 49, 31, 5, 49, 27, 10, 45, 27, 11, 45, 31, 1, 36, 45, 8, 36, 49, 10, 40, 49, 5, 40, 45, 9, 45, 42, 3, 45, 38, 7, 49, 38, 11, 49, 42, 13, 45, 10, 12, 45, 6, 8, 40, 6, 9, 40, 10, 11, 45, 43, 10, 45, 39, 14, 40, 39, 18, 40, 43, 8, 41, 0, 12, 41, 5, 14, 45, 5, 10, 45, 0, 13, 41, 15, 9, 41, 10, 11, 45, 10, 18, 45, 15, 16, 46, 19, 15, 46, 15, 12, 41, 15, 13, 41, 19, 18, 46, 23, 14, 46, 19, 17, 41, 19, 19, 41, 23, 12, 41, 23, 15, 41, 28, 17, 45, 28, 14, 45, 23, 16, 28, 46, 13, 28, 41, 18, 32, 41, 19, 32, 46, 21, 26, 40, 20, 26, 36, 15, 20, 36, 16, 20, 40, 19, 42, 39, 17, 42, 35, 22, 36, 35, 23, 36, 39, 15, 37, 0, 20, 37, 6, 22, 41, 6, 17, 41, 0, 21, 37, 17, 16, 37, 11, 19, 41, 11, 23, 41, 17, 25, 13, 47.5f, 24, 13, 44, 20, 8, 44, 21, 8, 47.5f, 23, 18, 47.5f, 22, 18, 44, 26, 13, 44, 27, 13, 47.5f, 20, 44, 43, 24, 44, 48, 26, 47.5f, 48, 22, 47.5f, 43, 25, 45, 5, 21, 45, 0, 23, 48.5f, 0, 27, 48.5f, 5, 27, 53.5f, 32.5f, 26, 53.5f, 29, 24, 50, 29, 25, 50, 32.5f}, new String[]{"tail0", "tail1", "tail2", "tail3", "tail4"}, new float[][]{new float[]{15, 1, 16, 1, 17, 1, 19, 1}, new float[]{12, 1, 13, 1, 14, 1, 18, 1}, new float[]{8, 1, 9, 1, 10, 1, 11, 1}, new float[]{1, 1, 3, 1, 5, 1, 7, 1}, new float[]{0, 1, 2, 1, 4, 1, 6, 1}}), PartPose.offset(0, 0, 2));
        GroupDefinition tail0 = tail.addOrReplaceChild("tail0", GroupBuilder.create(), PartPose.offset(0, 0, 2.3f));
        GroupDefinition tail1 = tail0.addOrReplaceChild("tail1", GroupBuilder.create(), PartPose.offset(0, 0, 4.9f));
        GroupDefinition tail2 = tail1.addOrReplaceChild("tail2", GroupBuilder.create(), PartPose.offset(0, 0, 4.2f));
        GroupDefinition tail3 = tail2.addOrReplaceChild("tail3", GroupBuilder.create(), PartPose.offset(0, 0, 4.5f));
        tail3.addOrReplaceChild("tail4", GroupBuilder.create(), PartPose.offset(0, 0, 3.1f));
        GroupDefinition armor_body = body.addOrReplaceChild("armor_body", GroupBuilder.create(), PartPose.offsetAndRotation(0, 8.3f, -2, 0.48f, 0, 0));
        armor_body.addOrReplaceChild("cube", GroupBuilder.create()
                .addBox(-3.7f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new Vector3f(0.6f), new CubeUV().south(22.5f, 51.5f, 19, 48).west(37, 28, 34, 24.5f).up(52.5f, 3, 49, 0).north(12, 20, 8, 16).east(38, 28, 35, 24.5f).down(12, 16, 8, 20), 3), PartPose.offset(-0f, 0, 0));
        armor_body.addOrReplaceChild("cube_i0", GroupBuilder.create()
                .addBox(0.2f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new Vector3f(0.6f), new CubeUV().south(50.5f, 31.5f, 47, 28).west(37, 28, 34, 24.5f).up(38.5f, 51, 35, 48).north(12, 20, 8, 16).east(37, 28, 34, 24.5f).down(12, 16, 8, 20), 3), PartPose.offset(-0f, 0, 0));
        body.addOrReplaceChild("cube_i1", GroupBuilder.create()
                .addBox(-1.8f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new CubeUV().south(15.5f, 55.5f, 12, 52).west(55, 49.5f, 52, 46).up(55.5f, 53, 52, 50).north(11.5f, 55.5f, 8, 52).east(47, 55.5f, 44, 52).down(56.5f, 0, 53, 3)), PartPose.offsetAndRotation(2, 8.3f, -2, 0.48f, 0, 0));
        body.addOrReplaceChild("cube_i2", GroupBuilder.create()
                .addBox(-1.7f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new CubeUV().south(55.5f, 42.5f, 52, 39).west(56, 10.5f, 53, 7).up(56.5f, 23, 53, 20).north(33.5f, 55.5f, 30, 52).east(56, 6.5f, 53, 3).down(56.5f, 23, 53, 26)), PartPose.offsetAndRotation(-2, 8.3f, -2, 0.48f, 0, 0));
        root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new CubeUV().south(20, 36, 16, 24).west(24, 36, 20, 24).up(30, 50, 26, 46).north(28, 12, 24, 0).east(28, 24, 24, 12).down(50, 31, 46, 35))
                .addBox(0f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().south(56, 32, 52, 20).west(52, 32, 48, 20).up(48, 20, 44, 16).north(48, 32, 44, 20).east(44, 32, 40, 20), 3), PartPose.offset(4, 22, 0));
        root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new CubeUV().south(32, 12, 28, 0).west(8, 40, 4, 28).up(12, 52, 8, 48).north(28, 36, 24, 24).east(4, 40, 0, 28).down(16, 48, 12, 52))
                .addBox(-4f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().south(48, 32, 44, 20).west(40, 32, 44, 20).up(44, 20, 48, 16).north(56, 32, 52, 20).east(52, 32, 48, 20), 3), PartPose.offset(-4, 22, 0));
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2, 13, 0, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create());
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new CubeUV().south(59, 34, 55, 32).west(57, 55, 52, 53).up(45, 33, 41, 28).north(51, 56, 47, 54).east(58, 28, 53, 26).down(46, 33, 42, 38))
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().south(12, 20, 8, 16).west(12, 20, 8, 16).up(12, 20, 8, 16).north(12, 20, 8, 16).east(12, 20, 8, 16).down(12, 16, 8, 20), 5)
                .addMesh(new float[]{1.6929f, -6.025f, -2, 1.4f, -6.025f, -2.7071f, 2.4f, -6.025f, -2.2929f, 2.1071f, -6.025f, -3, 1.9f, -6.025f, -2.5f, 1.4f, -6.025f, -2.2929f, 2.1071f, -6.025f, -2, 2.4f, -6.025f, -2.7071f, 1.6929f, -6.025f, -3, 0.4929f, -6.025f, -2, 0.2f, -6.025f, -2.7071f, 1.2f, -6.025f, -2.2929f, 0.9071f, -6.025f, -3, 0.7f, -6.025f, -2.5f, 0.2f, -6.025f, -2.2929f, 0.9071f, -6.025f, -2, 1.2f, -6.025f, -2.7071f, 0.4929f, -6.025f, -3, -0.7071f, -6.025f, -2, -1, -6.025f, -2.7071f, -0f, -6.025f, -2.2929f, -0.2929f, -6.025f, -3, -0.5f, -6.025f, -2.5f, -1, -6.025f, -2.2929f, -0.2929f, -6.025f, -2, -0f, -6.025f, -2.7071f, -0.7071f, -6.025f, -3, -0.915f, -6.025f, 1.025f, -0.725f, -6.025f, -1.325f, 2.315f, -6.025f, 1.025f, 2.125f, -6.025f, -1.325f, 0.7f, -6.025f, -0.3f, -1.2f, -6.025f, -0f, 0.7f, -6.025f, 0.9f, 2.6f, -6.025f, -0f, 0.7f, -6.025f, -1.8f}, new float[]{5, 49.25f, 41.2929f, 1, 49.25f, 41.7071f, 8, 49.5429f, 42, 4, 49.75f, 41.5f, 6, 49.9571f, 41, 0, 49.5429f, 41, 5, 49.25f, 41.2929f, 4, 49.75f, 41.5f, 7, 50.25f, 41.7071f, 2, 50.25f, 41.2929f, 6, 49.9571f, 41, 4, 49.75f, 41.5f, 8, 49.5429f, 42, 3, 49.9571f, 42, 7, 50.25f, 41.7071f, 4, 49.75f, 41.5f, 14, 46.25f, 37.2929f, 10, 46.25f, 37.7071f, 17, 46.5429f, 38, 13, 46.75f, 37.5f, 15, 46.9571f, 37, 9, 46.5428f, 37f, 14, 46.25f, 37.2929f, 13, 46.75f, 37.5f, 16, 47.25f, 37.7071f, 11, 47.25f, 37.293f, 15, 46.9571f, 37.0001f, 13, 46.75f, 37.5f, 17, 46.5429f, 38, 12, 46.9571f, 38, 16, 47.25f, 37.7071f, 13, 46.75f, 37.5f, 23, 32f, 33.7929f, 19, 32f, 34.2071f, 26, 32.2929f, 34.5f, 22, 32.5f, 34f, 24, 32.7071f, 33.5f, 18, 32.2929f, 33.5f, 23, 32f, 33.7929f, 22, 32.5f, 34f, 25, 33f, 34.2071f, 20, 33f, 33.7929f, 24, 32.707f, 33.4999f, 22, 32.5f, 34f, 26, 32.2929f, 34.5f, 21, 32.7071f, 34.5f, 25, 33f, 34.2071f, 22, 32.5f, 34, 32, 40.1f, 53.2f, 28, 40.575f, 54.525f, 35, 42f, 55, 31, 42, 53.5f, 33, 42f, 52.3f, 27, 40.385f, 52.175f, 32, 40.1f, 53.2f, 31, 42f, 53.5f, 34, 43.9f, 53.2f, 29, 43.615f, 52.175f, 33, 42f, 52.3f, 31, 42f, 53.5f, 35, 42f, 55, 30, 43.425f, 54.525f, 34, 43.9f, 53.2f, 31, 42, 53.5f}), PartPose.rotation(0, 0, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().south(41, 35, 37, 29).west(40, 45, 36, 39).up(52, 50, 48, 46).north(41, 23, 37, 17).east(41, 29, 37, 23).down(53, 0, 49, 4))
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().south(4, 32, 0, 26).west(8, 32, 4, 26).up(49, 16, 45, 12).north(4, 32, 0, 26).east(8, 32, 4, 26).down(49, 16, 45, 20), 5), PartPose.rotation(0.1309f, 0, 0));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().south(58, 30, 54, 28).west(58, 32, 54, 30).up(52, 46, 48, 42).north(26, 56, 22, 54).east(30, 56, 26, 54).down(48, 48, 44, 52))
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().south(8, 29, 4, 25).west(8, 29, 4, 25).up(8, 29, 4, 25).east(8, 29, 4, 25).down(49, 8, 45, 12), 4), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().south(37, 35, 33, 27).west(32, 41, 28, 33).up(34, 52, 30, 48).north(37, 19, 33, 11).east(37, 27, 33, 19).down(44, 48, 40, 52))
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(8, 29, 4, 20).east(12, 29, 8, 20).down(8, 25, 4, 29), 4), PartPose.rotation(0.3491f, 0, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2, 13, 0, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create());
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new CubeUV().south(55, 57, 51, 55).west(59, 15, 54, 13).up(36, 48, 32, 43).north(59, 50, 55, 48).east(59, 13, 54, 11).down(44, 43, 40, 48))
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().south(12, 20, 8, 16).west(12, 16, 8, 20).up(12, 20, 8, 16).north(8, 20, 12, 16).east(12, 20, 8, 16).down(8, 16, 12, 20), 5)
                .addMesh(new float[]{-1.6929f, -6.025f, -2, -1.4f, -6.025f, -2.7071f, -2.4f, -6.025f, -2.2929f, -2.1071f, -6.025f, -3, -1.9f, -6.025f, -2.5f, -1.4f, -6.025f, -2.2929f, -2.1071f, -6.025f, -2, -2.4f, -6.025f, -2.7071f, -1.6929f, -6.025f, -3, -0.4929f, -6.025f, -2, -0.2f, -6.025f, -2.7071f, -1.2f, -6.025f, -2.2929f, -0.9071f, -6.025f, -3, -0.7f, -6.025f, -2.5f, -0.2f, -6.025f, -2.2929f, -0.9071f, -6.025f, -2, -1.2f, -6.025f, -2.7071f, -0.4929f, -6.025f, -3, 0.7071f, -6.025f, -2, 1, -6.025f, -2.7071f, 0f, -6.025f, -2.2929f, 0.2929f, -6.025f, -3, 0.5f, -6.025f, -2.5f, 1, -6.025f, -2.2929f, 0.2929f, -6.025f, -2, 0f, -6.025f, -2.7071f, 0.7071f, -6.025f, -3, 0.915f, -6.025f, 1.025f, 0.725f, -6.025f, -1.325f, -2.315f, -6.025f, 1.025f, -2.125f, -6.025f, -1.325f, -0.7f, -6.025f, -0.3f, 1.2f, -6.025f, -0f, -0.7f, -6.025f, 0.9f, -2.6f, -6.025f, -0f, -0.7f, -6.025f, -1.8f}, new float[]{8, 32.7071f, 12, 1, 33, 11.7071f, 5, 33, 11.2929f, 4, 32.5f, 11.5f, 5, 33, 11.2929f, 0, 32.7071f, 11, 6, 32.2929f, 11, 4, 32.5f, 11.5f, 6, 32.2929f, 11, 2, 32f, 11.2929f, 7, 32, 11.7071f, 4, 32.5f, 11.5f, 7, 32, 11.7071f, 3, 32.2929f, 12, 8, 32.7071f, 12, 4, 32.5f, 11.5f, 17, 34.9571f, 49, 10, 35.25f, 48.7071f, 14, 35.25f, 48.2929f, 13, 34.75f, 48.5f, 14, 35.25f, 48.2929f, 9, 34.9571f, 48f, 15, 34.5429f, 48, 13, 34.75f, 48.5f, 15, 34.5429f, 48, 11, 34.25f, 48.2929f, 16, 34.25f, 48.707f, 13, 34.75f, 48.5f, 16, 34.25f, 48.7071f, 12, 34.5429f, 49, 17, 34.9571f, 49, 13, 34.75f, 48.5f, 26, 49.9571f, 40.5f, 19, 50.25f, 40.2071f, 23, 50.25f, 39.7929f, 22, 49.75f, 40f, 23, 50.25f, 39.7929f, 18, 49.9571f, 39.5f, 24, 49.5429f, 39.5f, 22, 49.75f, 40f, 24, 49.5429f, 39.5f, 20, 49.25f, 39.7929f, 25, 49.2501f, 40.2071f, 22, 49.75f, 40f, 25, 49.2501f, 40.2071f, 21, 49.5429f, 40.5f, 26, 49.9571f, 40.5f, 22, 49.75f, 40, 35, 54f, 46, 28, 55.425f, 45.525f, 32, 55.9f, 44.2f, 31, 54f, 44.5f, 32, 55.9f, 44.2f, 27, 55.615f, 43.175f, 33, 54, 43.3f, 31, 54, 44.5f, 33, 54, 43.3f, 29, 52.385f, 43.175f, 34, 52.1f, 44.2f, 31, 54, 44.5f, 34, 52.1f, 44.2f, 30, 52.575f, 45.525f, 35, 54f, 46, 31, 54f, 44.5f}), PartPose.rotation(0, 0, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().south(24, 46, 20, 40).west(28, 46, 24, 40).up(38, 53, 34, 49).north(4, 46, 0, 40).east(8, 46, 4, 40).down(53, 35, 49, 39))
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().south(4, 32, 0, 26).west(4, 32, 8, 26).up(8, 50, 4, 46).north(0, 32, 4, 26).east(8, 32, 4, 26).down(27, 46, 23, 50), 5), PartPose.rotation(0.1309f, 0, 0));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().south(44, 57, 40, 55).west(59, 48, 55, 46).up(53, 25, 49, 21).north(59, 36, 55, 34).east(59, 38, 55, 36).down(53, 25, 49, 29))
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().south(8, 29, 4, 25).west(4, 29, 8, 25).up(8, 29, 4, 25).north(53, 26, 49, 24).east(8, 29, 4, 25).down(4, 46, 0, 50), 4), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().south(16, 44, 12, 36).west(20, 44, 16, 36).up(53, 8, 49, 4).north(36, 43, 32, 35).east(12, 44, 8, 36).down(53, 8, 49, 12))
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(4, 29, 8, 20).east(12, 29, 8, 20).down(4, 25, 8, 29), 4), PartPose.rotation(0.3491f, 0, 0));

        return ModelDefinition.create(modelBuilder, 1, 1);
    }
}