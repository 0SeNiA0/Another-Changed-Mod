package net.zaharenko424.a_changed.client.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.animation.FallFlyingAnim;
import net.zaharenko424.a_changed.client.animation.LatexPupAnim;
import net.zaharenko424.cmrs.api.ModelPropertyKeys;
import net.zaharenko424.cmrs.client.ModelDefinitionCache;
import net.zaharenko424.cmrs.client.layer.ItemInMawLayer;
import net.zaharenko424.cmrs.client.layer.ItemOnHead;
import net.zaharenko424.cmrs.client.layer.TridentSpinEffect;
import net.zaharenko424.cmrs.client.layer.VanillaElytra;
import net.zaharenko424.cmrs.client.material.CutOut;
import net.zaharenko424.cmrs.client.material.VanillaTexArmor;
import net.zaharenko424.cmrs.client.model.PartTransform;
import net.zaharenko424.cmrs.client.model.PoseTransform;
import net.zaharenko424.cmrs.client.model.Texture;
import net.zaharenko424.cmrs.client.model.UniversalCustomModel;
import net.zaharenko424.cmrs.client.property.FPArms;
import net.zaharenko424.cmrs.client.property.StringProperty;
import net.zaharenko424.cmrs.client.property.UnitProperty;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class WhiteLatexPupModel <E extends LivingEntity> extends UniversalCustomModel<E> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/wl_pup");

    public WhiteLatexPupModel() {
        super(ModelDefinitionCache.getInstance().bake(DarkLatexPupModel.bodyLayer),
                List.of(Texture.fromAsset(TEXTURE, 1)),
                List.of(new CutOut(0), new VanillaTexArmor(ArmorItem.Type.BODY)),
                List.of(new ItemOnHead("head"),
                        new ItemInMawLayer("head", new PoseTransform(new Vector3f(0, -.15f, -.3f), null, new Vector3f(.4f))),
                        new VanillaElytra(
                                new PoseTransform(new Vector3f(0, .8f, -.4f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 80), null),
                                new PoseTransform(new Vector3f(0, .8f, -.4f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 80), null),
                                new PoseTransform(new Vector3f(0, .5f, -.4f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 25), null), true,
                                new PoseTransform(new Vector3f(0, 1.2f, -.2f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 80), new Vector3f(.75f)),
                                new PoseTransform(new Vector3f(0, 1.2f, -.2f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 80), new Vector3f(.75f)),
                                new PoseTransform(new Vector3f(0, .9f, -.3f), new Quaternionf().rotationX(Mth.DEG_TO_RAD * 25), new Vector3f(.75f))
                        ),
                        new TridentSpinEffect()
                ),
                Map.of(ModelPropertyKeys.REMAP_UV, UnitProperty.INSTANCE,
                        ModelPropertyKeys.HEAD, new StringProperty("head"),
                        ModelPropertyKeys.FP_ARMS, new FPArms(
                                "right_arm", new PartTransform(false, new Vector3f(-6, 1.5f, -2), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * 5, Mth.PI), false, null),
                                "left_arm", new PartTransform(false, new Vector3f(5, 1.5f, 0), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * -5, Mth.PI), false, null)
                        )
                ),
                List.of(LatexPupAnim.getInstance(), FallFlyingAnim.getInstance()),
                .5f);
    }
}
