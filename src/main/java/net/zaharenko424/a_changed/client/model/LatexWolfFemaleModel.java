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
import net.zaharenko424.a_changed.util.Int2ObjArrayMap;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;

public class LatexWolfFemaleModel<E extends LivingEntity>  extends UniversalCustomModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(AChanged.resourceLoc("latex_wolf_female"),"main");

    public LatexWolfFemaleModel(ResourceLocation texture) {
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
                .addMesh(new float[]{-1, 5, 4, -1, 5, -1, -1, -2, 4, -1, -2, -1, 1, 5, 4, 1, 5, -1, 1, -2, 4, 1, -2, -1, -0.99f, 2, 4, -0.99f, 5, 1, 0.99f, 2, 4, 0.99f, 5, 1}, new float[]{0, 39, 32, 1, 34, 32, 3, 34, 39, 2, 39, 39, 5, 13, 36, 4, 8, 36, 6, 8, 43, 7, 13, 43, 5, 50, 20, 1, 48, 20, 0, 48, 25, 4, 50, 25, 4, 24, 47, 0, 22, 47, 2, 22, 54, 6, 24, 54, 1, 26, 47, 5, 24, 47, 7, 24, 54, 3, 26, 54, 11, 45, 50, 9, 43, 50, 8, 43, 54, 10, 45, 54}), PartPose.offsetAndRotation(3, 7, -1, 0.4276f, 0.384f, -0.3665f));
        head.addOrReplaceChild("left_ear", GroupBuilder.create()
                .addMesh(new float[]{1, 5, 4, 1, 5, -1, 1, -2, 4, 1, -2, -1, -1, 5, 4, -1, 5, -1, -1, -2, 4, -1, -2, -1, 0.99f, 2, 4, 0.99f, 5, 1, -0.99f, 2, 4, -0.99f, 5, 1}, new float[]{3, 29, 39, 1, 29, 32, 0, 24, 32, 2, 24, 39, 6, 34, 39, 4, 34, 32, 5, 29, 32, 7, 29, 39, 0, 19, 48, 1, 19, 53, 5, 17, 53, 4, 17, 48, 2, 47, 53, 0, 47, 46, 4, 45, 46, 6, 45, 53, 7, 22, 54, 5, 22, 47, 1, 20, 47, 3, 20, 54, 8, 43, 54, 9, 43, 50, 11, 41, 50, 10, 41, 54}), PartPose.offsetAndRotation(-3, 7, -1, 0.4276f, -0.384f, 0.3665f));
        head.addOrReplaceChild("maw", GroupBuilder.create()
                .addBox(-2f, -1f, -2f, 4, 2, 2, new CubeUV().west(53, 7, 51, 5).up(54, 24, 50, 22).north(54, 22, 50, 20).east(45, 40, 43, 38).down(41, 50, 37, 52)), PartPose.offset(0, 2, -4));
        head.addOrReplaceChild("maw_i0", GroupBuilder.create()
                .addBox(-1.5f, -1f, -1.5f, 3, 1, 1.5f, new CubeUV().west(48.5f, 38, 47, 37).up(47, 26.5f, 44, 25).north(47, 28, 44, 27).east(38.5f, 40, 37, 39).down(54, 3, 51, 4.5f)), PartPose.offset(0, 1, -4));
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4.1f, 0f, -2.1f, 8.2f, 12, 4.2f, new CubeUV().south(16, 12, 8, 0).west(20, 36, 16, 24).up(44, 12, 36, 8).north(8, 12, 0, 0).east(28, 20, 24, 8).down(44, 12, 36, 16))
                .addBox(-4f, 0f, -2f, 8, 12, 4, new Vector3f(0.6f), new CubeUV().south(40, 32, 32, 20).west(20, 32, 16, 20).up(40, 11, 32, 7).north(28, 32, 20, 20).east(32, 32, 28, 20).down(40, 34, 32, 38), 3)
                .addBox(-4f, 0f, -2f, 8, 3, 4, new Vector3f(0.5f), new CubeUV().south(40, 32, 32, 27).west(20, 32, 16, 27).up(40, 11, 32, 7).north(28, 32, 20, 27).east(32, 32, 28, 27).down(40, 34, 32, 38), 4), PartPose.offset(0, 12, 0));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addAnimatedMesh(new float[]{1, 1, 13, 1.8f, 1.8f, 11, 1, -1, 13, 1.8f, -1.8f, 11, -1, 1, 13, -1.8f, 1.8f, 11, -1, -1, 13, -1.8f, -1.8f, 11, 2, 2, 8, 2, -2, 8, -2, 2, 8, -2, -2, 8, 1.5f, 1.5f, 2, 1.5f, 1.5f, -2, 1.5f, -1.5f, 2, 1.5f, -1.5f, -2, -1.5f, 1.5f, 2, -1.5f, 1.5f, -2, -1.5f, -1.5f, 2, -1.5f, -1.5f, -2, -1.7f, 1.7f, 4, 1.7f, 1.7f, 4, 1.7f, -1.7f, 4, -1.7f, -1.7f, 4}, new float[]{3, 26.313f, 3.8f, 1, 26.313f, 0.2f, 0, 24.159f, 1, 2, 24.159f, 3, 6, 39.341f, 7, 4, 39.341f, 5, 5, 37.187f, 4.2f, 7, 37.187f, 7.8f, 0, 15, 35.1821f, 1, 15.8f, 33.028f, 5, 12.2f, 33.028f, 4, 13, 35.1821f, 3, 11.8f, 22.472f, 2, 11, 20.3179f, 6, 9, 20.3179f, 7, 8.2f, 22.472f, 2, 53, 9, 0, 53, 7, 4, 51, 7, 6, 51, 9, 9, 29.3197f, 4, 8, 29.3197f, 0, 1, 26.313f, 0.2f, 3, 26.313f, 3.8f, 7, 37.187f, 7.8f, 5, 37.187f, 4.2f, 10, 34.1802f, 4, 11, 34.1802f, 8, 1, 15.8f, 33.028f, 8, 16, 30.0213f, 10, 12, 30.0213f, 5, 12.2f, 33.028f, 9, 12, 25.4787f, 3, 11.8f, 22.472f, 7, 8.2f, 22.472f, 11, 8, 25.4787f, 15, 39.341f, 3.5f, 13, 39.341f, 0.5f, 12, 35.341f, 0.5f, 14, 35.341f, 3.5f, 18, 28.159f, 7.5f, 16, 28.159f, 4.5f, 17, 24.159f, 4.5f, 19, 24.159f, 7.5f, 12, 15.5f, 24, 13, 15.5f, 20, 17, 12.5f, 20, 16, 12.5f, 24, 15, 11.5f, 35.5f, 14, 11.5f, 31.5f, 18, 8.5f, 31.5f, 19, 8.5f, 35.5f, 19, 24, 39, 17, 24, 36, 13, 21, 36, 15, 21, 39, 20, 30.1689f, 4.3f, 23, 30.1689f, 7.7f, 11, 34.1802f, 8, 10, 34.1802f, 4, 21, 15.7f, 26.01f, 20, 12.3f, 26.01f, 10, 12, 30.0213f, 8, 16, 30.0213f, 22, 33.3311f, 3.7f, 21, 33.3311f, 0.3f, 8, 29.3197f, 0, 9, 29.3197f, 4, 23, 8.3f, 29.49f, 22, 11.7f, 29.49f, 9, 12, 25.4787f, 11, 8, 25.4787f, 21, 15.7f, 26.01f, 12, 15.5f, 24, 16, 12.5f, 24, 20, 12.3f, 26.01f, 23, 30.1689f, 7.7f, 20, 30.1689f, 4.3f, 16, 28.159f, 4.5f, 18, 28.159f, 7.5f, 21, 33.3311f, 0.3f, 22, 33.3311f, 3.7f, 14, 35.341f, 3.5f, 12, 35.341f, 0.5f, 22, 11.7f, 29.49f, 23, 8.3f, 29.49f, 18, 8.5f, 31.5f, 14, 11.5f, 31.5f}, new String[]{"tail_1", "tail_0"}, new float[][]{new float[]{0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1}, new float[]{8, 1, 9, 1, 10, 1, 11, 1}}), PartPose.offset(0, 0, 2));
        GroupDefinition tail_0 = tail.addOrReplaceChild("tail_0", GroupBuilder.create(), PartPose.offset(0, 0, 4));
        tail_0.addOrReplaceChild("tail_1", GroupBuilder.create(), PartPose.offset(0, 0, 4));
        GroupDefinition armor_body = body.addOrReplaceChild("armor_body", GroupBuilder.create(), PartPose.offsetAndRotation(0, 8.3f, -2, 0.48f, 0, 0));
        armor_body.addOrReplaceChild("armor_cube", GroupBuilder.create()
                .addBox(-3.7f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new Vector3f(0.6f), new CubeUV().south(22.5f, 51.5f, 19, 48).west(37, 28, 34, 24.5f).up(52.5f, 3, 49, 0).north(12, 20, 8, 16).east(38, 28, 35, 24.5f).down(12, 16, 8, 20), 3), PartPose.offset(-0f, 0, 0));
        armor_body.addOrReplaceChild("armor_cube_i0", GroupBuilder.create()
                .addBox(0.2f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new Vector3f(0.6f), new CubeUV().south(50.5f, 31.5f, 47, 28).west(37, 28, 34, 24.5f).up(38.5f, 51, 35, 48).north(12, 20, 8, 16).east(37, 28, 34, 24.5f).down(12, 16, 8, 20), 3), PartPose.offset(-0f, 0, 0));
        body.addOrReplaceChild("cube", GroupBuilder.create()
                .addBox(-1.8f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new CubeUV().south(29.5f, 50.5f, 26, 47).west(33, 50.5f, 30, 47).up(50.5f, 49, 47, 46).north(50.5f, 28.5f, 47, 25).east(43, 31.5f, 40, 28).down(51.5f, 0, 48, 3)), PartPose.offsetAndRotation(2, 8.3f, -2, 0.48f, 0, 0));
        body.addOrReplaceChild("cube_i0", GroupBuilder.create()
                .addBox(-1.7f, -1.8f, -1.5f, 3.5f, 3.5f, 3, new CubeUV().south(50.5f, 36.5f, 47, 33).west(51, 10.5f, 48, 7).up(11.5f, 51, 8, 48).north(50.5f, 32.5f, 47, 29).east(51, 6.5f, 48, 3).down(51.5f, 11, 48, 14)), PartPose.offsetAndRotation(-2, 8.3f, -2, 0.48f, 0, 0));
        root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new CubeUV().south(4, 40, 0, 28).west(8, 40, 4, 28).up(48, 9, 44, 5).north(24, 36, 20, 24).east(28, 32, 24, 20).down(48, 9, 44, 13))
                .addBox(0f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().south(56, 32, 52, 20).west(52, 32, 48, 20).up(48, 20, 44, 16).north(48, 32, 44, 20).east(44, 32, 40, 20), 3), PartPose.offset(4, 22, 0));
        root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new CubeUV().south(36, 20, 32, 8).west(36, 32, 32, 20).up(16, 48, 12, 44).north(32, 20, 28, 8).east(32, 32, 28, 20).down(48, 13, 44, 17))
                .addBox(-4f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().south(48, 32, 44, 20).west(44, 32, 40, 20).up(44, 20, 48, 16).north(56, 32, 52, 20).east(52, 32, 48, 20), 3), PartPose.offset(-4, 22, 0));
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2, 13, 0, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create());
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new CubeUV().south(16, 52, 12, 50).west(53, 20, 48, 18).up(47, 38, 43, 33).north(8, 52, 4, 50).east(53, 18, 48, 16).down(48, 0, 44, 5))
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().south(12, 20, 8, 16).west(12, 20, 8, 16).up(12, 20, 8, 16).north(12, 20, 8, 16).east(12, 20, 8, 16).down(8, 20, 12, 16), 5), PartPose.rotation(0, 0, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().south(44, 6, 40, 0).west(8, 46, 4, 40).up(4, 50, 0, 46).north(37, 45, 33, 39).east(4, 46, 0, 40).down(8, 46, 4, 50))
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().south(4, 32, 0, 26).west(8, 32, 4, 26).up(49, 16, 45, 12).north(4, 32, 0, 26).east(8, 32, 4, 26).down(49, 16, 45, 20), 5), PartPose.rotation(0.1309f, 0, 0));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().south(37, 51, 33, 49).west(53, 39, 49, 37).up(49, 42, 45, 38).east(44, 8, 40, 6))
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().south(8, 25, 4, 29).west(8, 25, 4, 29).up(8, 25, 4, 29).east(8, 25, 4, 29), 4), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().south(21, 44, 17, 36).west(40, 32, 36, 24).up(20, 48, 16, 44).north(17, 44, 13, 36).east(40, 24, 36, 16).down(48, 17, 44, 21))
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(8, 29, 4, 20).east(12, 29, 8, 20).down(8, 25, 4, 29), 4), PartPose.rotation(0.3491f, 0, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2, 13, 0, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create());
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new CubeUV().south(4, 52, 0, 50).west(53, 16, 48, 14).up(12, 48, 8, 43).north(51, 51, 47, 49).east(17, 50, 12, 48).down(47, 28, 43, 33))
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().south(12, 20, 8, 16).west(12, 16, 8, 20).up(12, 20, 8, 16).north(8, 20, 12, 16).east(12, 20, 8, 16).down(8, 16, 12, 20), 5), PartPose.rotation(0, 0, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().south(41, 46, 37, 40).west(45, 46, 41, 40).up(41, 50, 37, 46).north(44, 22, 40, 16).east(44, 28, 40, 22).down(45, 46, 41, 50))
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().south(4, 32, 0, 26).west(4, 32, 8, 26).north(0, 32, 4, 26).east(8, 32, 4, 26).down(27, 46, 23, 50), 5), PartPose.rotation(0.1309f, 0, 0));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().south(53, 43, 49, 41).west(53, 45, 49, 43).up(49, 46, 45, 42).east(53, 41, 49, 39))
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().south(8, 25, 4, 29).west(4, 25, 8, 29).up(8, 25, 4, 29).east(8, 25, 4, 29), 4), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().south(33, 47, 29, 39).west(43, 40, 39, 32).up(48, 25, 44, 21).north(25, 47, 21, 39).east(29, 47, 25, 39).down(37, 45, 33, 49))
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(4, 29, 8, 20).east(12, 29, 8, 20).down(4, 25, 8, 29), 4), PartPose.rotation(0.3491f, 0, 0));

        return ModelDefinition.create(modelBuilder, 1, 1);
    }
}