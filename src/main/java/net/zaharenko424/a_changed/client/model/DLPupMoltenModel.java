package net.zaharenko424.a_changed.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.cmrs.client.ModelDefinitionCache;
import net.zaharenko424.cmrs.api.AnimationComponent;
import net.zaharenko424.cmrs.api.ModelPropertyRegistry;
import net.zaharenko424.cmrs.client.geom.*;
import net.zaharenko424.cmrs.client.model.PoseTransform;
import net.zaharenko424.cmrs.client.model.UniversalCustomModel;
import net.zaharenko424.cmrs.client.property.*;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.cmrs.util.Int2ObjArrayMap;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;

public class DLPupMoltenModel <E extends LivingEntity> extends UniversalCustomModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(TransfurRegistry.DARK_LATEX_PUP_TF.getId(), "molten");
    private static final ResourceLocation TEXTURE0 = AChanged.textureLoc("entity/dl_pup");
    private static final ResourceLocation TEXTURE1 = AChanged.textureLoc("block/dark_latex_block");

    public DLPupMoltenModel() {
        super(ModelDefinitionCache.getInstance().bake(bodyLayer), Util.make(new ModelPropertyMapImpl(), map -> {
            map.addLast(ModelPropertyRegistry.REMAP_UV.get(), Unit.INSTANCE);
            map.addLast(ModelPropertyRegistry.TEXTURES.get(), new Textures(Util.make(new Int2ObjArrayMap<>(2), m -> {
                m.put(0, Texture.fromAsset(TEXTURE0, 1));
                m.put(1, Texture.fromAsset(TEXTURE1, 1));
            })));
            map.addLast(ModelPropertyRegistry.ITEM_ON_HEAD.get(), new ItemOnHead("head"));
            map.addLast(ModelPropertyRegistry.ITEM_IN_MAW.get(), new ItemInMawLayer("head", new PoseTransform(new Vector3f(0, .1f, -.4f), null, new Vector3f(.4f))));
        }), Util.make(new ArrayList<>(1), list ->
                list.add(new AnimationComponent() {
                    @Override
                    public <E extends LivingEntity> void animate(ModelPart root, E entity, PoseStack poseStack, float partialTick, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
                        if(!entity.isBaby()) return;
                        root.offsetScale(Reusable.VEC3F.get().set(-.5));
                        root.getPart("head").offsetScale(Reusable.VEC3F.get().set(.3));
                        root.y -= 1;
                        poseStack.scale(1.5f, 1.5f, 1.5f);
                        poseStack.translate(0, -1/16f, 0);
                    }
                }))
        );
    }

    public static @NotNull ModelDefinition model(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create()
                .addMesh(new float[]{-7.8f, -2.6f, -3.4309f, -8.3f, -2.5f, 3.2984f, -3.1085f, -2.5f, 7.5884f, -3.2309f, -2.6f, -7.8f, 3.3159f, -2.5f, 8, 8, -2.5f, 3.0926f, 7.8f, -2.6f, -3.5309f, 3.3309f, -2.6f, -7.4f, 1.7811f, -0.6f, 4.4f, 4.3f, -0.6f, 1.8225f, 4.3f, -0.6f, -1.8225f, 1.7811f, -0.6f, -4.4f, -1.7811f, -0.6f, -4.4f, -1.7811f, -0.6f, 4.4f, -4.3f, -0.6f, -1.8225f, -4.3f, -0.6f, 1.8225f, 2.2282f, 0.5f, 2.3f, 2.8284f, 0.5f, 0.8284f, 2.3193f, 0f, -0.7456f, 1.0071f, -0.3f, -2.25f, -1.0071f, -0.3f, -2.25f, -2.1282f, 0.5f, 2.3f, -2.3193f, 0f, -0.7456f, -2.8284f, 0.5f, 0.8284f, -3.9408f, -4, 9.3f, -9.3f, -4, 3.4242f, -8.2667f, -4, -3.9408f, -3.4242f, -4, -9.3f, 3.4242f, -4, 8.7833f, 3.9408f, -4, -8.2667f, 8.7833f, -4, -4.4575f, 9.3f, -4, 3.4242f, -6.1009f, -1.7f, 2.5058f, -5.9613f, -1.7f, -2.5708f, -2.4712f, -1.7f, 6.029f, 5.9547f, -1.7f, 2.4118f, 2.4646f, -1.7f, 6.029f, 5.9547f, -1.7f, -2.6178f, 2.5111f, -1.7f, -5.812f, -2.4712f, -1.7f, -6}, new float[]{20, 10, 15, 22, 7, 18.3431f, 23, 7, 21.6569f, 21, 10, 25, 16, 15, 20, 17, 18, 16.6569f, 18, 18, 13.3431f, 19, 15, 10, 19, 17, 0, 20, 12, 0, 21, 12, 10, 16, 17, 10, 17, 5, 21.9142f, 16, 4.8429f, 17.4152f, 8, 0.7202f, 16, 9, 0.7202f, 22.6226f, 18, 20.1569f, 20, 17, 16.8431f, 20, 9, 15.1863f, 24, 10, 21.8137f, 24, 19, 23.4065f, 0.1669f, 18, 27.9051f, 0, 10, 28.6232f, 4, 11, 22, 4, 20, 23.8137f, 11, 19, 19.1863f, 11, 11, 18.1863f, 15, 12, 24.8137f, 15, 16, 22.8137f, 7, 21, 18.1863f, 7, 13, 17.1863f, 11, 8, 23.8137f, 11, 22, 22, 5.9142f, 20, 21.8429f, 1.4152f, 12, 17.7202f, 0, 14, 17.7202f, 6.6226f, 23, 22.1569f, 16, 22, 18.8431f, 16, 14, 17.1863f, 20, 15, 23.8137f, 20, 21, 27.2167f, 20.1669f, 23, 22.7181f, 20, 15, 22, 24, 13, 28.6232f, 24, 24, 15, 11, 25, 10, 15.6863f, 26, 10, 22.3137f, 27, 15, 27, 28, 7, 0, 24, 0, 0, 27, 0, 16, 29, 7, 16, 25, 3, 7, 24, 9.6274f, 7, 2, 9.6274f, 6, 1, 3, 6, 26, 2, 10, 25, 8.6274f, 10, 1, 8.6274f, 9, 0, 2, 9, 27, 3, 13, 26, 9.6274f, 13, 0, 9.6274f, 12, 3, 3, 12, 24, 0, 1, 28, 6.6274f, 1, 4, 6.6274f, 0, 2, 0, 0, 29, 0, 9, 27, 6.6274f, 9, 3, 6.6274f, 8, 7, 0, 8, 30, 2, 7, 29, 8.6274f, 7, 7, 8.6274f, 6, 6, 2, 6, 31, 3, 5, 30, 9.6274f, 5, 6, 9.6274f, 4, 5, 3, 4, 28, 2, 2, 31, 8.6274f, 2, 5, 8.6274f, 1, 4, 2, 1, 29, 7, 16, 30, 12, 11.3137f, 31, 12, 4.6863f, 28, 7, 0, 32, 33.2218f, 8.6212f, 15, 31, 7, 14, 24, 7, 33, 24, 8.5f, 32, 33.2218f, 8.6212f, 33, 24, 8.5f, 0, 24, 10, 1, 31, 10, 34, 31.0756f, 25.4176f, 13, 29, 24, 15, 22, 24, 32, 22, 25.5f, 34, 31.0756f, 25.4176f, 32, 22, 25.5f, 1, 22, 27, 2, 29, 27, 9, 32, 10, 8, 25, 10, 36, 25, 11.5f, 35, 34.0422f, 11.4429f, 35, 34.0422f, 11.4429f, 36, 25, 11.5f, 4, 25, 13, 5, 32, 13, 10, 30, 15, 9, 23, 15, 35, 23, 16.5f, 37, 32.0445f, 16.5f, 37, 32.0445f, 16.5f, 35, 23, 16.5f, 5, 23, 18, 6, 30, 18, 38, 23.394f, 25.3352f, 11, 22, 24, 10, 15, 24, 37, 15, 25.5f, 38, 23.394f, 25.3352f, 37, 15, 25.5f, 6, 15, 27, 7, 22, 27, 12, 7, 23, 11, 0, 23, 38, 0f, 24.5f, 39, 9.1673f, 24.6731f, 36, 30.96f, 5.5f, 8, 29, 4, 13, 22, 4, 34, 22, 5.5f, 36, 30.96f, 5.5f, 34, 22, 5.5f, 2, 22, 7, 4, 29, 7, 33, 8.8778f, 27.5571f, 14, 7, 26, 12, 0, 26, 39, 0, 27.5f, 33, 8.8778f, 27.5571f, 39, 0, 27.5f, 3, 0, 29, 0, 7, 29, 39, 5.9403f, 0, 38, 0.5846f, 0, 7, 0, 1.4612f, 3, 6.5721f, 1.5906f}, 1, true), PartPose.offset(0, 4, 0));
        root.addOrReplaceChild("head", GroupBuilder.create()
                .addBox(-3f, -2f, -3f, 6, 5, 4, new CubeUV().north(55, 23, 43, 13).south(20, 10, 14, 4).west(14, 8, 10, 4).up(10, 4, 4, 0).east(4, 8, 0, 4))
                .addBox(-3f, 3f, -1f, 2, 2, 1, new CubeUV().north(19, 17, 17, 15).south(22, 17, 20, 15).west(20, 17, 19, 15).up(19, 15, 17, 14).east(17, 17, 16, 15).down(21, 14, 19, 15))
                .addBox(1f, 3f, -1f, 2, 2, 1, new CubeUV().north(19, 17, 17, 15).south(22, 17, 20, 15).west(20, 17, 19, 15).up(19, 15, 17, 14).east(17, 17, 16, 15).down(21, 14, 19, 15))
                .addBox(-1.5f, -1.98f, -6f, 3, 2, 4, new CubeUV().north(58, 4, 52, 0).west(8, 14, 0, 10).up(26, 4, 23, 0).east(60, 10, 52, 6).down(10, 4, 7, 8))
                .addBox(-1f, -3f, -5.5f, 2, 1, 2.5f, new CubeUV().north(64, 32, 62, 31).west(56.5f, 32, 54, 31).east(60.5f, 32, 58, 31).down(50, 29.5f, 52, 32)), PartPose.offsetAndRotation(0, -1.7f, 0, 0.48f, 0, 0));

        return ModelDefinition.create(modelBuilder, 1, 1);
    }
}