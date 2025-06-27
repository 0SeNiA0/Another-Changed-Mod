package net.zaharenko424.a_changed.client.model;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.animation.FallFlyingAnim;
import net.zaharenko424.a_changed.client.animation.LatexPupAnim;
import net.zaharenko424.cmrs.api.ModelPropertyRegistry;
import net.zaharenko424.cmrs.client.ModelDefinitionCache;
import net.zaharenko424.cmrs.client.model.PartTransform;
import net.zaharenko424.cmrs.client.model.PoseTransform;
import net.zaharenko424.cmrs.client.model.Texture;
import net.zaharenko424.cmrs.client.model.UniversalCustomModel;
import net.zaharenko424.cmrs.client.property.*;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class WhiteLatexPupModel <E extends LivingEntity> extends UniversalCustomModel<E> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/wl_pup");

    public WhiteLatexPupModel() {
        super(ModelDefinitionCache.getInstance().bake(DarkLatexPupModel.bodyLayer), List.of(Texture.fromAsset(TEXTURE, 1)),
                Util.make(new ModelPropertyMapImpl(), map -> {
            map.addLast(ModelPropertyRegistry.REMAP_UV.get(), Unit.INSTANCE);
            map.addLast(ModelPropertyRegistry.CUT_OUT.get(), new CutOut(Util.make(new Int2IntArrayMap(2), m -> {
                m.put(0, 0);
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
        }), Util.make(new ArrayList<>(2), l -> {
            l.add(LatexPupAnim.getInstance());
            l.add(FallFlyingAnim.getInstance());
        }), .5f);
    }
}
