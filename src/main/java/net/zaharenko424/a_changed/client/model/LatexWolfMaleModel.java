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
import net.zaharenko424.a_changed.client.animation.Animations;
import net.zaharenko424.a_changed.client.animation.FallFlyingAnim;
import net.zaharenko424.a_changed.client.animation.HumanoidAnim;
import net.zaharenko424.a_changed.client.animation.SwimAnim;
import net.zaharenko424.cmrs.client.ModelDefinitionCache;
import net.zaharenko424.cmrs.client.animation.KeyframeAnimator;
import net.zaharenko424.cmrs.api.ModelPropertyRegistry;
import net.zaharenko424.cmrs.client.geom.CubeUV;
import net.zaharenko424.cmrs.client.geom.GroupBuilder;
import net.zaharenko424.cmrs.client.geom.GroupDefinition;
import net.zaharenko424.cmrs.client.geom.ModelDefinition;
import net.zaharenko424.cmrs.client.model.PartTransform;
import net.zaharenko424.cmrs.client.model.PoseTransform;
import net.zaharenko424.cmrs.client.model.UniversalCustomModel;
import net.zaharenko424.cmrs.client.property.*;
import net.zaharenko424.cmrs.util.Int2ObjArrayMap;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;

public class LatexWolfMaleModel<E extends LivingEntity>  extends UniversalCustomModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(AChanged.resourceLoc("latex_wolf_male"),"main");

    public LatexWolfMaleModel(ResourceLocation texture) {
        super(ModelDefinitionCache.getInstance().bake(bodyLayer), Util.make(new ModelPropertyMapImpl(), map -> {
            map.addLast(ModelPropertyRegistry.REMAP_UV.get(), Unit.INSTANCE);
            map.addLast(ModelPropertyRegistry.TEXTURES.get(), new Textures(Util.make(new Int2ObjArrayMap<>(2), m -> {
                m.put(0, Texture.fromAsset(texture, 2));
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
        KeyframeAnimator.animate(tail, root(), Animations.TAIL_DEF, ageInTicks);
        if(!entity.isInWaterOrBubble()) KeyframeAnimator.applyStatic(root(), Animations.STATIC_TAIL);
    }

    public static ModelDefinition model(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new CubeUV().south(24, 8, 16, 0).west(24, 16, 16, 8).up(24, 24, 16, 16).north(8, 20, 0, 12).east(16, 20, 8, 12).down(8, 20, 0, 28))
                .addBox(-4f, 0f, -4f, 8, 8, 8, new Vector3f(0.6f), new CubeUV().south(32, 16, 24, 8).west(24, 16, 16, 8).up(16, 8, 8, 0).north(16, 16, 8, 8).east(8, 16, 0, 8).down(24, 0, 16, 8), 2), PartPose.offset(0, 24, 0));
        head.addOrReplaceChild("right_ear", GroupBuilder.create()
                .addMesh(new float[]{-1, 5, 4, -1, 5, -1, -1, -2, 4, -1, -2, -1, 1, 5, 4, 1, 5, -1, 1, -2, 4, 1, -2, -1, -0.99f, 2, 4, -0.99f, 5, 1, 0.99f, 2, 4, 0.99f, 5, 1}, new float[]{0, 39, 32, 1, 34, 32, 3, 34, 39, 2, 39, 39, 5, 13, 36, 4, 8, 36, 6, 8, 43, 7, 13, 43, 5, 49, 31, 1, 47, 31, 0, 47, 36, 4, 49, 36, 4, 24, 47, 0, 22, 47, 2, 22, 54, 6, 24, 54, 1, 26, 47, 5, 24, 47, 7, 24, 54, 3, 26, 54, 11, 18, 48, 9, 16, 48, 8, 16, 52, 10, 18, 52}), PartPose.offsetAndRotation(3, 7, -1, 0.4276f, 0.384f, -0.3665f));
        head.addOrReplaceChild("left_ear", GroupBuilder.create()
                .addMesh(new float[]{1, 5, 4, 1, 5, -1, 1, -2, 4, 1, -2, -1, -1, 5, 4, -1, 5, -1, -1, -2, 4, -1, -2, -1, 0.99f, 2, 4, 0.99f, 5, 1, -0.99f, 2, 4, -0.99f, 5, 1}, new float[]{3, 29, 39, 1, 29, 32, 0, 24, 32, 2, 24, 39, 6, 34, 39, 4, 34, 32, 5, 29, 32, 7, 29, 39, 0, 33, 47, 1, 33, 52, 5, 31, 52, 4, 31, 47, 2, 47, 53, 0, 47, 46, 4, 45, 46, 6, 45, 53, 7, 22, 54, 5, 22, 47, 1, 20, 47, 3, 20, 54, 8, 42, 32, 9, 42, 28, 11, 40, 28, 10, 40, 32}), PartPose.offsetAndRotation(-3, 7, -1, 0.4276f, -0.384f, 0.3665f));
        head.addOrReplaceChild("maw", GroupBuilder.create()
                .addBox(-2f, -1f, -2f, 4, 2, 2, new CubeUV().west(20, 50, 18, 48).up(52, 14, 48, 12).north(16, 50, 12, 48).east(45, 40, 43, 38).down(52, 14, 48, 16)), PartPose.offset(0, 2, -4));
        head.addOrReplaceChild("maw_i0", GroupBuilder.create()
                .addBox(-1.5f, -1f, -1.5f, 3, 1, 1.5f, new CubeUV().west(49.5f, 23, 48, 22).up(51, 17.5f, 48, 16).north(47, 28, 44, 27).east(38.5f, 40, 37, 39).down(51, 18, 48, 19.5f)), PartPose.offset(0, 1, -4));
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new CubeUV().south(16, 12, 8, 0).west(20, 36, 16, 24).up(44, 12, 36, 8).north(8, 12, 0, 0).east(28, 20, 24, 8).down(44, 12, 36, 16))
                .addBox(-4f, 0f, -2f, 8, 12, 4, new Vector3f(0.6f), new CubeUV().south(40, 32, 32, 20).west(20, 32, 16, 20).up(40, 11, 32, 7).north(28, 32, 20, 20).east(32, 32, 28, 20).down(40, 34, 32, 38), 3)
                .addBox(-4f, 0f, -2f, 8, 3, 4, new Vector3f(0.5f), new CubeUV().south(40, 32, 32, 27).west(20, 32, 16, 27).up(40, 11, 32, 7).north(28, 32, 20, 27).east(32, 32, 28, 27).down(40, 34, 32, 38), 4), PartPose.offset(0, 12, 0));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addAnimatedMesh(new float[]{1, 1, 13, 1.8f, 1.8f, 11, 1, -1, 13, 1.8f, -1.8f, 11, -1, 1, 13, -1.8f, 1.8f, 11, -1, -1, 13, -1.8f, -1.8f, 11, 2, 2, 8, 2, -2, 8, -2, 2, 8, -2, -2, 8, 1.5f, 1.5f, 2, 1.5f, 1.5f, -2, 1.5f, -1.5f, 2, 1.5f, -1.5f, -2, -1.5f, 1.5f, 2, -1.5f, 1.5f, -2, -1.5f, -1.5f, 2, -1.5f, -1.5f, -2, -1.7f, 1.7f, 4, 1.7f, 1.7f, 4, 1.7f, -1.7f, 4, -1.7f, -1.7f, 4}, new float[]{3, 26.313f, 3.8f, 1, 26.313f, 0.2f, 0, 24.159f, 1, 2, 24.159f, 3, 6, 39.341f, 7, 4, 39.341f, 5, 5, 37.187f, 4.2f, 7, 37.187f, 7.8f, 0, 11, 35.1821f, 1, 11.8f, 33.028f, 5, 8.2f, 33.028f, 4, 9, 35.1821f, 3, 15.8f, 22.472f, 2, 15, 20.3179f, 6, 13, 20.3179f, 7, 12.2f, 22.472f, 2, 50, 22, 0, 50, 20, 4, 48, 20, 6, 48, 22, 9, 29.3197f, 4, 8, 29.3197f, 0, 1, 26.313f, 0.2f, 3, 26.313f, 3.8f, 7, 37.187f, 7.8f, 5, 37.187f, 4.2f, 10, 34.1802f, 4, 11, 34.1802f, 8, 1, 11.8f, 33.028f, 8, 12, 30.0213f, 10, 8, 30.0213f, 5, 8.2f, 33.028f, 9, 16, 25.4787f, 3, 15.8f, 22.472f, 7, 12.2f, 22.472f, 11, 12, 25.4787f, 15, 39.341f, 3.5f, 13, 39.341f, 0.5f, 12, 35.341f, 0.5f, 14, 35.341f, 3.5f, 18, 28.159f, 7.5f, 16, 28.159f, 4.5f, 17, 24.159f, 4.5f, 19, 24.159f, 7.5f, 12, 11.5f, 24, 13, 11.5f, 20, 17, 8.5f, 20, 16, 8.5f, 24, 15, 15.5f, 35.5f, 14, 15.5f, 31.5f, 18, 12.5f, 31.5f, 19, 12.5f, 35.5f, 19, 24, 39, 17, 24, 36, 13, 21, 36, 15, 21, 39, 20, 30.1689f, 4.3f, 23, 30.1689f, 7.7f, 11, 34.1802f, 8, 10, 34.1802f, 4, 21, 11.7f, 26.01f, 20, 8.3f, 26.01f, 10, 8, 30.0213f, 8, 12, 30.0213f, 22, 33.3311f, 3.7f, 21, 33.3311f, 0.3f, 8, 29.3197f, 0, 9, 29.3197f, 4, 23, 12.3f, 29.49f, 22, 15.7f, 29.49f, 9, 16, 25.4787f, 11, 12, 25.4787f, 21, 11.7f, 26.01f, 12, 11.5f, 24, 16, 8.5f, 24, 20, 8.3f, 26.01f, 23, 30.1689f, 7.7f, 20, 30.1689f, 4.3f, 16, 28.159f, 4.5f, 18, 28.159f, 7.5f, 21, 33.3311f, 0.3f, 22, 33.3311f, 3.7f, 14, 35.341f, 3.5f, 12, 35.341f, 0.5f, 22, 15.7f, 29.49f, 23, 12.3f, 29.49f, 18, 12.5f, 31.5f, 14, 15.5f, 31.5f}, new String[]{"tail_1", "tail_0"}, new float[][]{new float[]{0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1}, new float[]{8, 1, 9, 1, 10, 1, 11, 1}}), PartPose.offset(0, 0, 2));
        GroupDefinition tail_0 = tail.addOrReplaceChild("tail_0", GroupBuilder.create(), PartPose.offset(0, 0, 4));
        tail_0.addOrReplaceChild("tail_1", GroupBuilder.create(), PartPose.offset(0, 0, 4));
        root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new CubeUV().south(4, 40, 0, 28).west(8, 40, 4, 28).up(48, 9, 44, 5).north(24, 36, 20, 24).east(28, 32, 24, 20).down(48, 9, 44, 13))
                .addBox(0f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().south(56, 32, 52, 20).west(52, 32, 48, 20).up(48, 20, 44, 16).north(48, 32, 44, 20).east(44, 32, 40, 20), 3), PartPose.offset(4, 22, 0));
        root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new CubeUV().south(36, 20, 32, 8).west(36, 32, 32, 20).up(16, 48, 12, 44).north(32, 20, 28, 8).east(32, 32, 28, 20).down(48, 13, 44, 17))
                .addBox(-4f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().south(48, 32, 44, 20).west(40, 32, 44, 20).up(44, 20, 48, 16).north(56, 32, 52, 20).east(52, 32, 48, 20), 3), PartPose.offset(-4, 22, 0));
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2, 13, 0, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create());
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new CubeUV().south(52, 12, 48, 10).west(52, 31, 47, 29).up(47, 38, 43, 33).north(52, 10, 48, 8).east(52, 29, 47, 27).down(48, 0, 44, 5))
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().south(12, 20, 8, 16).west(12, 20, 8, 16).up(12, 20, 8, 16).north(12, 20, 8, 16).east(12, 20, 8, 16).down(12, 16, 8, 20), 5), PartPose.rotation(0, 0, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().south(44, 6, 40, 0).west(8, 46, 4, 40).up(4, 50, 0, 46).north(37, 45, 33, 39).east(4, 46, 0, 40).down(8, 46, 4, 50))
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().south(4, 32, 0, 26).west(8, 32, 4, 26).up(49, 16, 45, 12).north(4, 32, 0, 26).east(8, 32, 4, 26).down(49, 16, 45, 20), 5), PartPose.rotation(0.1309f, 0, 0));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().south(51, 38, 47, 36).west(51, 48, 47, 46).up(49, 42, 45, 38).east(44, 8, 40, 6))
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().south(8, 29, 4, 25).west(8, 29, 4, 25).up(8, 29, 4, 25).east(8, 29, 4, 25).down(49, 8, 45, 12), 4), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().south(21, 44, 17, 36).west(40, 32, 36, 24).up(20, 48, 16, 44).north(17, 44, 13, 36).east(40, 24, 36, 16).down(48, 17, 44, 21))
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(8, 29, 4, 20).east(12, 29, 8, 20).down(8, 25, 4, 29), 4), PartPose.rotation(0.3491f, 0, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2, 13, 0, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create());
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new CubeUV().south(12, 50, 8, 48).west(31, 49, 26, 47).up(12, 48, 8, 43).north(52, 8, 48, 6).east(49, 27, 44, 25).down(47, 28, 43, 33))
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().south(12, 20, 8, 16).west(12, 16, 8, 20).up(12, 20, 8, 16).north(8, 20, 12, 16).east(12, 20, 8, 16).down(8, 16, 12, 20), 5), PartPose.rotation(0, 0, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().south(41, 46, 37, 40).west(45, 46, 41, 40).up(41, 50, 37, 46).north(44, 22, 40, 16).east(44, 28, 40, 22).down(45, 46, 41, 50))
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().south(4, 32, 0, 26).west(4, 32, 8, 26).up(8, 50, 4, 46).north(0, 32, 4, 26).east(8, 32, 4, 26).down(27, 46, 23, 50), 5), PartPose.rotation(0.1309f, 0, 0));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().south(52, 4, 48, 2).west(52, 6, 48, 4).up(49, 46, 45, 42).east(52, 2, 48, 0))
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().south(8, 29, 4, 25).west(4, 29, 8, 25).up(8, 29, 4, 25).north(53, 26, 49, 24).east(8, 29, 4, 25).down(4, 46, 0, 50), 4), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().south(33, 47, 29, 39).west(43, 40, 39, 32).up(48, 25, 44, 21).north(25, 47, 21, 39).east(29, 47, 25, 39).down(37, 45, 33, 49))
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(4, 29, 8, 20).east(12, 29, 8, 20).down(4, 25, 8, 29), 4), PartPose.rotation(0.3491f, 0, 0));

        return ModelDefinition.create(modelBuilder, 1, 1);
    }
}