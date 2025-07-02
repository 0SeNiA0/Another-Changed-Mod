package net.zaharenko424.a_changed.client.model;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
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
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.cmrs.api.ModelPropertyRegistry;
import net.zaharenko424.cmrs.client.ModelDefinitionCache;
import net.zaharenko424.cmrs.client.animation.KeyframeAnimator;
import net.zaharenko424.cmrs.client.geom.builder.CubeUV;
import net.zaharenko424.cmrs.client.geom.builder.GroupBuilder;
import net.zaharenko424.cmrs.client.geom.builder.GroupDefinition;
import net.zaharenko424.cmrs.client.geom.builder.ModelDefinition;
import net.zaharenko424.cmrs.client.model.PartTransform;
import net.zaharenko424.cmrs.client.model.PoseTransform;
import net.zaharenko424.cmrs.client.model.Texture;
import net.zaharenko424.cmrs.client.model.UniversalCustomModel;
import net.zaharenko424.cmrs.client.property.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BeiFengModel<E extends LivingEntity> extends UniversalCustomModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(TransfurRegistry.BEI_FENG_TF.getId(),"main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/bei_feng");

    public BeiFengModel() {
        super(ModelDefinitionCache.getInstance().bake(bodyLayer), List.of(Texture.fromAsset(TEXTURE, 2)),
                Util.make(new ModelPropertyMapImpl(), map -> {
            map.addLast(ModelPropertyRegistry.REMAP_UV.get(), Unit.INSTANCE);
            map.addLast(ModelPropertyRegistry.CUT_OUT.get(), new CutOut(Util.make(new Int2IntArrayMap(2), m -> {
                m.put(0, 0);
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
        }), .5f);
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
        if(!entity.isInWaterOrBubble()) KeyframeAnimator.applyStatic(root(), Animations.STATIC_TAIL_DRAGON);
    }

    public static ModelDefinition model(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-4f, 0f, -4f, 8, 8, 8, new CubeUV().south(34, 16, 26, 8).west(34, 24, 26, 16).up(34, 32, 26, 24).north(26, 29, 18, 21).east(18, 30, 10, 22).down(26, 29, 18, 37))
                .addBox(-4f, 0f, -4f, 8, 8, 8, new Vector3f(0.6f), new CubeUV().south(32, 16, 24, 8).west(24, 16, 16, 8).up(16, 8, 8, 0).north(16, 16, 8, 8).east(8, 16, 0, 8).down(24, 0, 16, 8), 2), PartPose.offset(0, 24, 0));
        GroupDefinition right_ear = head.addOrReplaceChild("right_ear", GroupBuilder.create(), PartPose.offset(3, 7, -1));
        right_ear.addOrReplaceChild("ear", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2, 9, 5, new CubeUV().south(48, 44, 46, 35).west(43, 9, 38, 0).up(6, 34, 4, 29).north(22, 55, 20, 46).east(22, 46, 17, 37).down(14, 50, 12, 55)), PartPose.rotation(0.7295f, 0.3578f, -0.1745f));
        right_ear.addOrReplaceChild("ear_inner", GroupBuilder.create()
                .addBox(-1f, 2.2f, -5.8f, 2, 1, 8, new Vector3f(-0.01f), new CubeUV().south(54, 46, 52, 45).west(60, 33, 52, 32).up(10, 58, 8, 50).north(54, 45, 52, 44).east(60, 32, 52, 31).down(12, 50, 10, 58)), PartPose.rotation(1.7331f, 0.3578f, -0.1745f));
        GroupDefinition left_ear = head.addOrReplaceChild("left_ear", GroupBuilder.create(), PartPose.offset(-3, 7, -1));
        left_ear.addOrReplaceChild("ear_i0", GroupBuilder.create()
                .addBox(-1f, -2f, -1f, 2, 9, 5, new CubeUV().south(49, 22, 47, 13).west(43, 27, 38, 18).up(16, 55, 14, 50).north(48, 53, 46, 44).east(43, 18, 38, 9).down(52, 30, 50, 35)), PartPose.rotation(0.7295f, -0.3578f, 0.1745f));
        left_ear.addOrReplaceChild("ear_inner_i0", GroupBuilder.create()
                .addBox(-1f, 2.2f, -5.8f, 2, 1, 8, new Vector3f(-0.01f), new CubeUV().south(54, 44, 52, 43).west(60, 31, 52, 30).up(44, 57, 42, 49).north(54, 43, 52, 42).east(16, 5, 8, 4).down(46, 49, 44, 57)), PartPose.rotation(1.7331f, -0.3578f, 0.1745f));
        head.addOrReplaceChild("maw", GroupBuilder.create()
                .addBox(-2f, -1f, -2f, 4, 2, 2, new CubeUV().west(54, 35, 52, 33).up(30, 54, 26, 52).north(56, 12, 52, 10).east(49, 24, 47, 22).down(34, 52, 30, 54)), PartPose.offset(0, 2, -4));
        head.addOrReplaceChild("maw_i0", GroupBuilder.create()
                .addBox(-1.5f, -1f, -1.5f, 3, 1, 1.5f, new CubeUV().west(53.5f, 48, 52, 47).up(46, 26.5f, 43, 25).north(50, 25, 47, 24).east(53.5f, 47, 52, 46).down(34, 50, 31, 51.5f)), PartPose.offset(0, 1, -4));
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 8, 12, 4, new CubeUV().south(26, 21, 18, 9).west(13, 42, 9, 30).up(46, 31, 38, 27).north(18, 22, 10, 10).east(4, 37, 0, 25).down(46, 31, 38, 35))
                .addBox(-0.5f, 10f, 2f, 1, 1, 1.5f, new CubeUV().south(18, 10, 17, 9).west(53.5f, 50, 52, 49).up(18, 35.5f, 17, 34).north(5, 6, 4, 5).east(53.5f, 49, 52, 48).down(9, 36, 8, 37.5f))
                .addBox(-0.5f, 7f, 2f, 1, 1, 1.5f, new CubeUV().south(5, 29, 4, 28).west(53.5f, 52, 52, 51).up(9, 39.5f, 8, 38).north(26, 9, 25, 8).east(53.5f, 51, 52, 50).down(9, 40, 8, 41.5f))
                .addBox(-0.5f, 4f, 2f, 1, 1, 1.5f, new CubeUV().south(18, 37, 17, 36).west(1.5f, 54, 0, 53).up(17, 43.5f, 16, 42).north(10, 30, 9, 29).east(53.5f, 53, 52, 52).down(17, 44, 16, 45.5f))
                .addBox(-4f, 0f, -2f, 8, 12, 4, new Vector3f(0.6f), new CubeUV().south(40, 32, 32, 20).west(20, 32, 16, 20).up(40, 11, 32, 7).north(28, 32, 20, 20).east(32, 32, 28, 20).down(40, 34, 32, 38), 3)
                .addBox(-4f, 0f, -2f, 8, 3, 4, new Vector3f(0.5f), new CubeUV().south(40, 32, 32, 27).west(20, 32, 16, 27).up(40, 11, 32, 7).north(28, 32, 20, 27).east(32, 32, 28, 27).down(40, 34, 32, 38), 4), PartPose.offset(0, 12, 0));
        GroupDefinition tail = body.addOrReplaceChild("tail", GroupBuilder.create()
                .addAnimatedMesh(new float[]{1, 2.4f, 21, 1.25f, 2.4f, 18, 1, 0, 21, 1.25f, -0.4f, 18, -1, 2.4f, 21, -1, 0, 21, -1.25f, -0.4f, 18, 1.65f, 2.4f, 13.75f, 1.65f, -1.05f, 13.75f, -1.25f, 2.4f, 18, -1.65f, 2.4f, 13.75f, -1.65f, -1.05f, 13.75f, 1.95f, 2.4f, 9.25f, 1.95f, -1.55f, 9.25f, -1.95f, -1.55f, 9.25f, 2.25f, 2.4f, 3.75f, 2.25f, -2.1f, 3.75f, -1.95f, 2.4f, 9.25f, -2.25f, -2.1f, 3.75f, 2.4f, 2.4f, -3, 2.4f, -2.4f, -3, -2.25f, 2.4f, 3.75f, -2.4f, 2.4f, -3, -2.4f, -2.4f, -3}, new float[]{3, 7.2358f, 2.8f, 1, 7.2358f, 0, 0, 4.2255f, 0, 2, 4.2255f, 2.4f, 5, 29.2745f, 7.4f, 4, 29.2745f, 5, 9, 26.2642f, 5, 6, 26.2642f, 7.8f, 0, 8.5f, 34, 1, 8.75f, 31, 9, 6.25f, 31, 4, 6.5f, 34, 3, 3.75f, 3.3888f, 2, 3.5f, 0.3622f, 5, 1.5f, 0.3622f, 6, 1.25f, 3.3888f, 2, 36, 54, 0, 36, 52, 4, 34, 52, 5, 34, 54, 8, 11.5047f, 3.45f, 7, 11.5047f, 0, 1, 7.2358f, 0, 3, 7.2358f, 2.8f, 6, 26.2642f, 7.8f, 9, 26.2642f, 5, 10, 21.9953f, 5, 11, 21.9953f, 8.45f, 1, 8.75f, 31, 7, 9.15f, 26.75f, 10, 5.85f, 26.75f, 9, 6.25f, 31, 8, 4.15f, 7.6882f, 3, 3.75f, 3.3888f, 6, 1.25f, 3.3888f, 11, 0.85f, 7.6882f, 13, 16.0147f, 3.95f, 12, 16.0147f, 0, 7, 11.5047f, 0, 8, 11.5047f, 3.45f, 11, 21.9953f, 8.45f, 10, 21.9953f, 5, 17, 17.4853f, 5, 14, 17.4853f, 8.95f, 7, 9.15f, 26.75f, 12, 9.45f, 22.25f, 17, 5.55f, 22.25f, 10, 5.85f, 26.75f, 13, 4.45f, 12.2158f, 8, 4.15f, 7.6882f, 11, 0.85f, 7.6882f, 14, 0.55f, 12.2158f, 16, 21.5229f, 4.5f, 15, 21.5229f, 0, 12, 16.0147f, 0, 13, 16.0147f, 3.95f, 14, 17.4853f, 8.95f, 17, 17.4853f, 5, 21, 11.9771f, 5, 18, 11.9771f, 9.5f, 12, 9.45f, 22.25f, 15, 9.75f, 16.75f, 21, 5.25f, 16.75f, 17, 5.55f, 22.25f, 16, 4.75f, 17.7433f, 13, 4.45f, 12.2158f, 14, 0.55f, 12.2158f, 18, 0.25f, 17.7433f, 20, 28.2745f, 4.8f, 19, 28.2745f, 0, 15, 21.5229f, 0, 16, 21.5229f, 4.5f, 18, 11.9771f, 9.5f, 21, 11.9771f, 5, 22, 5.2255f, 5, 23, 5.2255f, 9.8f, 15, 9.75f, 16.75f, 19, 9.9f, 10, 22, 5.1f, 10, 21, 5.25f, 16.75f, 20, 4.9f, 24.5f, 16, 4.75f, 17.7433f, 18, 0.25f, 17.7433f, 23, 0.1f, 24.5f, 23, 48, 13, 22, 48, 8, 19, 43, 8, 20, 43, 13}, new String[]{"tail0", "tail1", "tail2", "tail3"}, new float[][]{new float[]{12, 1, 13, 1, 14, 1, 17, 1}, new float[]{7, 1, 8, 1, 10, 1, 11, 1}, new float[]{1, 1, 3, 1, 6, 1, 9, 1}, new float[]{0, 1, 2, 1, 4, 1, 5, 1}})
                .addBox(-0.5f, 2.5f, 0f, 1, 1, 1, new CubeUV().south(8, 54, 7, 53).west(54, 13, 53, 12).up(54, 14, 53, 13).north(49, 13, 48, 12).east(7, 54, 6, 53).down(54, 14, 53, 15)), PartPose.offset(0, 0, 2.5f));
        GroupDefinition tail0 = tail.addOrReplaceChild("tail0", GroupBuilder.create()
                .addBox(-0.5f, 2.4f, 2f, 1, 1, 1, new CubeUV().south(54, 17, 53, 16).west(18, 54, 17, 53).up(54, 18, 53, 17).north(54, 16, 53, 15).east(17, 54, 16, 53).down(19, 53, 18, 54)), PartPose.offset(0, 0, 4));
        GroupDefinition tail1 = tail0.addOrReplaceChild("tail1", GroupBuilder.create()
                .addBox(-0.5f, 2.4f, 2f, 1, 1, 1, new CubeUV().south(54, 20, 53, 19).west(54, 21, 53, 20).up(54, 22, 53, 21).north(54, 19, 53, 18).east(20, 54, 19, 53).down(23, 53, 22, 54)), PartPose.offset(0, 0, 5));
        GroupDefinition tail2 = tail1.addOrReplaceChild("tail2", GroupBuilder.create()
                .addBox(-0.5f, 1.9f, 1.5f, 1, 1, 1, new CubeUV().south(54, 24, 53, 23).west(25, 54, 24, 53).up(26, 54, 25, 53).north(54, 23, 53, 22).east(24, 54, 23, 53).down(37, 53, 36, 54)), PartPose.offset(0, 0.5f, 5));
        tail2.addOrReplaceChild("tail3", GroupBuilder.create()
                .addBox(-0.5f, 1.4f, 1f, 1, 1.5f, 1, new CubeUV().south(5, 54.5f, 4, 53).west(6, 54.5f, 5, 53).up(38, 54, 37, 53).north(3, 54.5f, 2, 53).east(4, 54.5f, 3, 53).down(39, 53, 38, 54)), PartPose.offset(0, 0.5f, 4));
        root.addOrReplaceChild("right_arm", GroupBuilder.create()
                .addBox(0f, -10f, -2f, 4, 12, 4, new CubeUV().south(34, 44, 30, 32).west(38, 12, 34, 0).up(51, 4, 47, 0).north(17, 42, 13, 30).east(30, 44, 26, 32).down(51, 4, 47, 8))
                .addBox(1.5f, -4f, 1f, 1, 1, 2.5f, new CubeUV().south(41, 54, 40, 53).west(54.5f, 37, 52, 36).up(1, 2.5f, 0, 0).north(40, 54, 39, 53).east(54.5f, 36, 52, 35).down(1, 3, 0, 5.5f))
                .addBox(1.5f, -5f, 1f, 1, 1, 1.8f, new CubeUV().south(47, 54, 46, 53).west(54, 40, 52, 39).up(30, 5, 29, 3).north(42, 54, 41, 53).east(38, 53, 36, 52).down(18, 30, 17, 32))
                .addBox(0f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().south(56, 32, 52, 20).west(52, 32, 48, 20).up(48, 20, 44, 16).north(48, 32, 44, 20).east(44, 32, 40, 20), 3), PartPose.offset(4, 22, 0));
        root.addOrReplaceChild("left_arm", GroupBuilder.create()
                .addBox(-4f, -10f, -2f, 4, 12, 4, new CubeUV().south(38, 36, 34, 24).west(38, 48, 34, 36).up(52, 12, 48, 8).north(8, 46, 4, 34).east(38, 24, 34, 12).down(38, 48, 34, 52))
                .addBox(-2.5f, -5f, 1f, 1, 1, 1.8f, new CubeUV().south(50, 54, 49, 53).west(54, 42, 52, 41).up(18, 34, 17, 32).north(49, 54, 48, 53).east(54, 41, 52, 40).down(9, 34, 8, 36))
                .addBox(-2.5f, -4f, 1f, 1, 1, 2.5f, new CubeUV().south(48, 54, 47, 53).west(54.5f, 39, 52, 38).up(5, 27.5f, 4, 25).east(54.5f, 38, 52, 37).down(30, 0, 29, 2.5f))
                .addBox(-4f, -10f, -2f, 4, 12, 4, new Vector3f(0.6f), new CubeUV().south(48, 32, 44, 20).west(40, 32, 44, 20).up(44, 20, 48, 16).north(56, 32, 52, 20).east(52, 32, 48, 20), 3), PartPose.offset(-4, 22, 0));
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offsetAndRotation(2, 13, 0, 0f, -0.1309f, 0.1309f));
        GroupDefinition right_leg_shin = right_leg.addOrReplaceChild("right_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition right_leg_ = right_leg_shin.addOrReplaceChild("right_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition right_foot = right_leg_.addOrReplaceChild("right_foot", GroupBuilder.create());
        right_foot.addOrReplaceChild("right_foot_i0", GroupBuilder.create()
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new CubeUV().south(56, 10, 52, 8).west(55, 30, 50, 28).up(50, 30, 46, 25).north(52, 53, 48, 51).east(55, 28, 50, 26).down(50, 30, 46, 35))
                .addBox(-1.3f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().south(12, 20, 8, 16).west(12, 20, 8, 16).up(12, 20, 8, 16).north(12, 20, 8, 16).east(12, 20, 8, 16).down(12, 16, 8, 20), 5), PartPose.rotation(0, 0, -0.1309f));
        right_leg_.addOrReplaceChild("right_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().south(42, 49, 38, 43).west(46, 49, 42, 43).north(47, 19, 43, 13).east(47, 25, 43, 19).down(53, 20, 49, 24))
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().south(8, 32, 4, 26).west(4, 32, 0, 26).up(49, 16, 45, 12).north(8, 32, 4, 26).east(4, 32, 0, 26).down(49, 16, 45, 20), 5), PartPose.rotation(0.1309f, 0, 0));
        right_leg_shin.addOrReplaceChild("right_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().south(55, 2, 51, 0).west(55, 4, 51, 2).up(53, 16, 49, 12).north(8, 5, 4, 3).east(4, 53, 0, 51))
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().south(8, 29, 4, 25).west(8, 29, 4, 25).up(8, 29, 4, 25).east(8, 29, 4, 25).down(49, 8, 45, 12), 4), PartPose.rotation(0.3491f, 0, 0));
        right_leg.addOrReplaceChild("right_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().south(26, 45, 22, 37).west(42, 43, 38, 35).up(52, 39, 48, 35).north(34, 8, 30, 0).east(4, 45, 0, 37).down(52, 39, 48, 43))
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(8, 29, 4, 20).east(12, 29, 8, 20).down(8, 25, 4, 29), 4), PartPose.rotation(0.3491f, 0, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offsetAndRotation(-2, 13, 0, 0f, 0.1309f, -0.1309f));
        GroupDefinition left_leg_shin = left_leg.addOrReplaceChild("left_leg_shin", GroupBuilder.create(), PartPose.offset(0, -6, -2));
        GroupDefinition left_leg_ = left_leg_shin.addOrReplaceChild("left_leg_", GroupBuilder.create(), PartPose.offset(0, -1, 3));
        GroupDefinition left_foot = left_leg_.addOrReplaceChild("left_foot", GroupBuilder.create());
        left_foot.addOrReplaceChild("left_foot_i0", GroupBuilder.create()
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new CubeUV().south(26, 53, 22, 51).west(31, 52, 26, 50).up(8, 51, 4, 46).north(20, 53, 16, 51).east(55, 26, 50, 24).down(20, 46, 16, 51))
                .addBox(-2.7f, -6f, -3.5f, 4f, 2, 5, new Vector3f(0.6f), new CubeUV().south(12, 20, 8, 16).west(12, 16, 8, 20).up(12, 20, 8, 16).north(8, 20, 12, 16).east(12, 20, 8, 16).down(8, 16, 12, 20), 5), PartPose.rotation(0, 0, 0.1309f));
        left_leg_.addOrReplaceChild("left_leg__i0", GroupBuilder.create()
                .addBox(-2f, -5f, -2f, 4, 6, 4, new CubeUV().south(4, 51, 0, 45).west(26, 51, 22, 45).north(30, 50, 26, 44).east(34, 50, 30, 44).down(42, 49, 38, 53))
                .addBox(-2f, -5f, -2f, 4, 6, 4, new Vector3f(0.6f), new CubeUV().south(4, 32, 0, 26).west(0, 32, 4, 26).up(8, 50, 4, 46).north(4, 32, 8, 26).east(4, 32, 0, 26).down(27, 46, 23, 50), 5), PartPose.rotation(0.1309f, 0, 0));
        left_leg_shin.addOrReplaceChild("left_leg_shin_i0", GroupBuilder.create()
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new CubeUV().south(55, 6, 51, 4).west(55, 8, 51, 6).up(53, 20, 49, 16).east(8, 53, 4, 51))
                .addBox(-2f, 0.3f, 1f, 4, 2, 4, new Vector3f(0.59f), new CubeUV().south(8, 29, 4, 25).west(4, 29, 8, 25).up(8, 29, 4, 25).north(53, 26, 49, 24).east(8, 29, 4, 25).down(4, 46, 0, 50), 4), PartPose.rotation(0.3491f, 0, 0));
        left_leg.addOrReplaceChild("left_leg_thigh", GroupBuilder.create()
                .addBox(-2f, -8f, -2f, 4, 8, 4, new CubeUV().south(46, 43, 42, 35).west(47, 8, 43, 0).up(52, 47, 48, 43).north(12, 50, 8, 42).east(16, 50, 12, 42).down(52, 47, 48, 51))
                .addBox(-2f, -8f, -2f, 4, 8, 4, new Vector3f(0.6f), new CubeUV().south(16, 29, 12, 20).west(12, 29, 8, 20).up(8, 20, 4, 16).north(4, 29, 8, 20).east(12, 29, 8, 20).down(4, 25, 8, 29), 4), PartPose.rotation(0.3491f, 0, 0));

        return ModelDefinition.create(modelBuilder, 1, 1);
    }
}