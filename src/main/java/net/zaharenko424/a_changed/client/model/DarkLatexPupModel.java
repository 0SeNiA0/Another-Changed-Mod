package net.zaharenko424.a_changed.client.model;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.animations.DLPupAnim;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
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
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class DarkLatexPupModel <E extends LivingEntity> extends UniversalCustomModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(TransfurRegistry.DARK_LATEX_PUP_TF.getId(), "main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/dl_pup");

    public DarkLatexPupModel() {
        super(ModelDefinitionCache.getInstance().bake(bodyLayer), Util.make(new ModelPropertyMapImpl(), map -> {
            map.addLast(ModelPropertyRegistry.REMAP_UV.get(), Unit.INSTANCE);
            map.addLast(ModelPropertyRegistry.TEXTURES.get(), new Textures(Util.make(new Int2ObjArrayMap<>(2), m -> {
                m.put(0, Texture.fromAsset(TEXTURE, 1));
            })));
            map.addLast(ModelPropertyRegistry.HEAD.get(), "head");
            map.addLast(ModelPropertyRegistry.ITEM_ON_HEAD.get(), new ItemOnHead("head"));
            map.addLast(ModelPropertyRegistry.FP_ARMS.get(), new FPArms(
                    "right_arm", new PartTransform(false, new Vector3f(-6, 1.5f, -2), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * 5, Mth.PI), false, null),
                    "left_arm", new PartTransform(false, new Vector3f(5, 1.5f, 0), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * -5, Mth.PI), false, null)
            ));
            map.addLast(ModelPropertyRegistry.ITEM_IN_MAW.get(), new ItemInMawLayer("head", new PoseTransform(new Vector3f(0, -.15f, -.3f), null, new Vector3f(.4f))));
            map.addLast(ModelPropertyRegistry.ARMOR.get(), new Armor(Util.make(new Int2ObjectArrayMap<>(), m -> {
                m.put(2, ArmorItem.Type.BODY);
            }), new Int2ObjectArrayMap<>(0)));
            map.addLast(ModelPropertyRegistry.VANILLA_ELYTRA.get(), new VanillaElytra(
                    new PoseTransform(new Vector3f(0, .8f, -.4f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 80), null),
                    new PoseTransform(new Vector3f(0, .8f, -.4f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 80), null),
                    new PoseTransform(new Vector3f(0, .5f, -.4f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 25), null), true,
                    new PoseTransform(new Vector3f(0, 1.2f, -.2f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 80), new Vector3f(.75f)),
                    new PoseTransform(new Vector3f(0, 1.2f, -.2f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 80), new Vector3f(.75f)),
                    new PoseTransform(new Vector3f(0, .9f, -.3f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 25), new Vector3f(.75f))));
            map.addLast(ModelPropertyRegistry.TRIDENT_SPIN_EFFECT.get(), new TridentSpinEffect());
        }), Util.make(new ArrayList<>(2), l -> l.add(new DLPupAnim())));
    }

    public static ModelDefinition model() {
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-3f, -3f, -3f, 6, 6, 4, new CubeUV().west(14, 10, 10, 4).up(10, 4, 4, 0).down(16, 0, 10, 4).north(55, 25, 43, 13).east(4, 10, 0, 4).south(20, 10, 14, 4))
                .addBox(-3f, -3f, -3f, 6, 6, 4, new Vector3f(0.25f), new CubeUV().west(14, 10, 10, 4).up(10, 4, 4, 0).down(16, 0, 10, 4).north(10, 10, 4, 4).east(4, 10, 0, 4).south(20, 10, 14, 4), 2)
                .addBox(-3f, 3f, -1f, 2, 2, 1, new CubeUV().west(20, 17, 19, 15).up(19, 15, 17, 14).down(21, 14, 19, 15).north(19, 17, 17, 15).east(17, 17, 16, 15).south(22, 17, 20, 15))
                .addBox(1f, 3f, -1f, 2, 2, 1, new CubeUV().west(20, 17, 19, 15).up(19, 15, 17, 14).down(21, 14, 19, 15).north(19, 17, 17, 15).east(17, 17, 16, 15).south(22, 17, 20, 15))
                .addBox(-1.5f, -1.98f, -6f, 3, 2, 4, new CubeUV().west(8, 14, 0, 10).up(26, 4, 23, 0).down(10, 4, 7, 8).north(58, 4, 52, 0).east(60, 10, 52, 6))
                .addBox(-1f, -3f, -5.5f, 2, 1, 2.5f, new CubeUV().west(56.5f, 32, 54, 31).down(50, 29.5f, 52, 32).north(64, 32, 62, 31).east(60.5f, 32, 58, 31)), PartPose.offset(0, 10.5f, -6));
        root.addOrReplaceChild("upper_body", GroupBuilder.create()
                .addBox(-4f, -3f, -3f, 8, 6, 7, new CubeUV().west(43, 13, 36, 7).up(36, 7, 28, 0).down(44, 0, 36, 7).north(36, 13, 28, 7).east(28, 13, 21, 7).south(51, 13, 43, 7))
                .addBox(-4f, -3f, -3f, 8, 6, 7, new Vector3f(0.25f), new CubeUV().west(43, 13, 36, 7).up(36, 7, 28, 0).down(44, 0, 36, 7).north(36, 13, 28, 7).east(28, 13, 21, 7).south(51, 13, 43, 7), 2), PartPose.offsetAndRotation(0, 10, -3, -1.5708f, 0, 0));
        root.addOrReplaceChild("body", GroupBuilder.create()
                .addBox(-3f, -7f, -3f, 6, 9, 6, new CubeUV().west(36, 29, 30, 20).up(30, 20, 24, 14).down(36, 14, 30, 20).north(30, 29, 24, 20).east(24, 29, 18, 20).south(42, 29, 36, 20))
                .addBox(-3f, -7f, -3f, 6, 9, 6, new Vector3f(0.25f), new CubeUV().west(36, 29, 30, 20).up(30, 20, 24, 14).down(36, 14, 30, 20).north(30, 29, 24, 20).east(24, 29, 18, 20).south(42, 29, 36, 20), 2), PartPose.offsetAndRotation(0, 10, 2, -1.5708f, 0, 0));
        root.addOrReplaceChild("right_leg_front", GroupBuilder.create()
                .addBox(1f, -8f, -1f, 2, 8, 2, new CubeUV().west(6, 28, 4, 20).up(4, 20, 2, 18).down(6, 18, 4, 20).north(4, 28, 2, 20).east(2, 28, 0, 20).south(8, 28, 6, 20))
                .addBox(1f, -8f, -1f, 2, 8, 2, new Vector3f(0.25f), new CubeUV().west(6, 28, 4, 20).up(4, 20, 2, 18).down(6, 18, 4, 20).north(4, 28, 2, 20).east(2, 28, 0, 20).south(8, 28, 6, 20), 2), PartPose.offset(-0.5f, 8, -4));
        root.addOrReplaceChild("left_leg_front", GroupBuilder.create()
                .addBox(-3f, -8f, -1f, 2, 8, 2, new CubeUV().west(6, 28, 4, 20).up(4, 20, 2, 18).down(6, 18, 4, 20).north(4, 28, 2, 20).east(2, 28, 0, 20).south(8, 28, 6, 20))
                .addBox(-3f, -8f, -1f, 2, 8, 2, new Vector3f(0.25f), new CubeUV().west(6, 28, 4, 20).up(4, 20, 2, 18).down(6, 18, 4, 20).north(4, 28, 2, 20).east(2, 28, 0, 20).south(8, 28, 6, 20), 2), PartPose.offset(0.5f, 8, -4));
        root.addOrReplaceChild("right_leg_back", GroupBuilder.create()
                .addBox(-2f, -8f, -1f, 2, 8, 2, new CubeUV().west(6, 28, 4, 20).up(4, 20, 2, 18).down(6, 18, 4, 20).north(4, 28, 2, 20).east(2, 28, 0, 20).south(8, 28, 6, 20))
                .addBox(-2f, -8f, -1f, 2, 8, 2, new Vector3f(0.25f), new CubeUV().west(6, 28, 4, 20).up(4, 20, 2, 18).down(6, 18, 4, 20).north(4, 28, 2, 20).east(2, 28, 0, 20).south(8, 28, 6, 20), 2), PartPose.offset(2.5f, 8, 7));
        root.addOrReplaceChild("left_leg_back", GroupBuilder.create()
                .addBox(0f, -8f, -1f, 2, 8, 2, new CubeUV().west(6, 28, 4, 20).up(4, 20, 2, 18).down(6, 18, 4, 20).north(4, 28, 2, 20).east(2, 28, 0, 20).south(8, 28, 6, 20))
                .addBox(0f, -8f, -1f, 2, 8, 2, new Vector3f(0.25f), new CubeUV().west(6, 28, 4, 20).up(4, 20, 2, 18).down(6, 18, 4, 20).north(4, 28, 2, 20).east(2, 28, 0, 20).south(8, 28, 6, 20), 2), PartPose.offset(-2.5f, 8, 7));
        root.addOrReplaceChild("tail", GroupBuilder.create()
                .addBox(-1f, -8f, -1f, 2, 8, 2, new CubeUV().west(15, 28, 13, 20).up(13, 20, 11, 18).down(15, 18, 13, 20).north(13, 28, 11, 20).east(11, 28, 9, 20).south(17, 28, 15, 20)), PartPose.offsetAndRotation(0, 12, 8, -0.6283f, 0, 0));

        return ModelDefinition.create(modelBuilder, 1, 1);
    }
}
