package net.zaharenko424.a_changed.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.util.Mth;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupBuilder;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.entity.RoombaEntity;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

public class RoombaModel extends EntityModel<RoombaEntity> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(EntityRegistry.ROOMBA_ENTITY.getId(), "main");

    private final ModelPart root;
    private final ModelPart brushRight;
    private final ModelPart brushLeft;

    public RoombaModel(){
        root = ModelDefinitionCache.INSTANCE.bake(bodyLayer).getChild("root");
        brushRight = root.getChild("brush_right");
        brushLeft = root.getChild("brush_left");
    }

    @Override
    public void setupAnim(@NotNull RoombaEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
        root.yRot = pNetHeadYaw * Mth.DEG_TO_RAD;

        float rotDeg = pEntity.tickCount % 90 * 4;

        brushRight.yRot = rotDeg * Mth.DEG_TO_RAD;
        brushLeft.yRot = -brushRight.yRot;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public static ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        root.addOrReplaceChild("body", GroupBuilder.create()
                .addMesh(new float[]{-8, -3, -3.3137f, -8, -3, 3.3137f, -3.3137f, -3, 8, -3.3137f, -3, -8, 3.3137f, -3, 8, 8, -3, 3.3137f, 8, -3, -3.3137f, 3.3137f, -3, -8, 3.3137f, 0, 8, 8, 0, 3.3137f, 8, 0, -3.3137f, 3.3137f, 0, -8, -3.3137f, 0, -8, -3.3137f, 0, 8, -8, 0, -3.3137f, -8, 0, 3.3137f, 2.3137f, 3, 5, 5.3284f, 3, 1.6569f, 5.3284f, 3, -1.6569f, 2.3137f, 3, -5, -2.3137f, 3, -5, -2.3137f, 3, 5, -5.3284f, 3, -1.6569f, -5.3284f, 3, 1.6569f, 5.3333f, -4, 2, 5.3333f, -4, -2, 2.6667f, -4, 2, 2.6667f, -4, -2, 2, -3.5f, 2, 2, -3, 2, 2, -3.5f, -2, 2, -3, -2, 6, -3.5f, 2, 6, -3, 2, 6, -3.5f, -2, 6, -3, -2, 5.3333f, -3.5f, -3, 5.3333f, -3, -3, 2.6667f, -3.5f, -3, 2.6667f, -3, -3, 5.3333f, -3.5f, 3, 5.3333f, -3, 3, 2.6667f, -3.5f, 3, 2.6667f, -3, 3, -2.6667f, -4, 2, -2.6667f, -4, -2, -5.3333f, -4, 2, -5.3333f, -4, -2, -6, -3.5f, 2, -6, -3, 2, -6, -3.5f, -2, -6, -3, -2, -2, -3.5f, 2, -2, -3, 2, -2, -3.5f, -2, -2, -3, -2, -2.6667f, -3.5f, -3, -2.6667f, -3, -3, -5.3333f, -3.5f, -3, -5.3333f, -3, -3, -2.6667f, -3.5f, 3, -2.6667f, -3, 3, -5.3333f, -3.5f, 3, -5.3333f, -3, 3}, new float[]{20, 10, 15, 22, 7, 18.3431f, 23, 7, 21.6569f, 21, 10, 25, 16, 15, 20, 17, 18, 16.6569f, 18, 18, 13.3431f, 19, 15, 10, 19, 17, 0, 20, 12, 0, 21, 12, 10, 16, 17, 10, 9, 32, 10, 8, 25, 10, 4, 25, 13, 5, 32, 13, 10, 30, 15, 9, 23, 15, 5, 23, 18, 6, 30, 18, 11, 22, 24, 10, 15, 24, 6, 15, 27, 7, 22, 27, 12, 7, 23, 11, 0, 23, 7, 0, 26, 3, 7, 26, 8, 29, 4, 13, 22, 4, 2, 22, 7, 4, 29, 7, 14, 7, 26, 12, 0, 26, 3, 0, 29, 0, 7, 29, 15, 31, 7, 14, 24, 7, 0, 24, 10, 1, 31, 10, 13, 29, 24, 15, 22, 24, 1, 22, 27, 2, 29, 27, 17, 5, 21.9142f, 16, 4.8429f, 17.4152f, 8, 0.7202f, 16, 9, 0.7202f, 22.6226f, 18, 20.1569f, 20, 17, 16.8431f, 20, 9, 15.1863f, 24, 10, 21.8137f, 24, 19, 23.4065f, 0.1669f, 18, 27.9051f, 0, 10, 28.6232f, 4, 11, 22, 4, 20, 23.8137f, 11, 19, 19.1863f, 11, 11, 18.1863f, 15, 12, 24.8137f, 15, 16, 22.8137f, 7, 21, 18.1863f, 7, 13, 17.1863f, 11, 8, 23.8137f, 11, 22, 22, 5.9142f, 20, 21.8429f, 1.4152f, 12, 17.7202f, 0, 14, 17.7202f, 6.6226f, 23, 22.1569f, 16, 22, 18.8431f, 16, 14, 17.1863f, 20, 15, 23.8137f, 20, 21, 27.2167f, 20.1669f, 23, 22.7181f, 20, 15, 22, 24, 13, 28.6232f, 24, 7, 7, 16, 6, 12, 11.3137f, 5, 12, 4.6863f, 4, 7, 0, 2, 15, 11, 1, 10, 15.6863f, 0, 10, 22.3137f, 3, 15, 27, 4, 7, 0, 2, 0, 0, 3, 0, 16, 7, 7, 16, 35, 22, 15, 33, 18, 15, 32, 18, 16, 34, 22, 16, 28, 29, 14, 29, 29, 13, 31, 25, 13, 30, 25, 14, 27, 10, 30, 25, 13, 30, 24, 13, 26, 26, 10, 26, 40, 12, 1, 41, 12, 0, 43, 9, 0, 42, 9, 1, 39, 15, 10, 37, 12, 10, 36, 12, 11, 38, 15, 11, 32, 29, 0, 24, 29, 1, 25, 33, 1, 34, 33, 0, 27, 29, 2, 26, 33, 2, 28, 33, 1, 30, 29, 1, 26, 29, 4, 24, 32, 4, 40, 32, 3, 42, 29, 3, 36, 29, 4, 25, 29, 5, 27, 32, 5, 38, 32, 4, 34, 10, 2, 36, 11, 2, 37, 11, 1, 35, 10, 1, 41, 11, 1, 40, 11, 2, 32, 12, 2, 33, 12, 1, 39, 11, 2, 38, 11, 3, 30, 12, 3, 31, 12, 2, 28, 12, 12, 42, 13, 12, 43, 13, 11, 29, 12, 11, 24, 30.6302f, 14, 32, 31, 13.2532f, 40, 29.7982f, 13.2532f, 29, 12, 11, 28, 5, 22.2532f, 26, 5.3698f, 23, 42, 6.2019f, 22.2532f, 29, 12, 11, 25, 29.3698f, 15, 34, 29, 14.2532f, 36, 30.2018f, 14.2532f, 29, 12, 11, 27, 8.6302f, 25, 38, 7.7981f, 24.2532f, 30, 9, 24.2532f, 29, 12, 11, 55, 29, 14, 53, 25, 14, 52, 25, 15, 54, 29, 15, 48, 27, 28, 49, 27, 27, 51, 23, 27, 50, 23, 28, 47, 16, 31, 45, 19, 31, 44, 19, 27, 46, 16, 27, 60, 31, 29, 61, 31, 28, 63, 28, 28, 62, 28, 29, 59, 3, 29, 57, 0, 29, 56, 0, 30, 58, 3, 30, 52, 28, 20, 44, 28, 21, 45, 32, 21, 54, 32, 20, 47, 28, 22, 46, 32, 22, 48, 32, 21, 50, 28, 21, 46, 29, 6, 44, 32, 6, 60, 32, 5, 62, 29, 5, 56, 6, 29, 45, 6, 30, 47, 9, 30, 58, 9, 29, 54, 4, 17, 56, 5, 17, 57, 5, 16, 55, 4, 16, 61, 7, 16, 60, 7, 17, 52, 8, 17, 53, 8, 16, 59, 16, 10, 58, 16, 11, 50, 17, 11, 51, 17, 10, 48, 17, 12, 62, 18, 12, 63, 18, 11, 49, 17, 11, 44, 11.6302f, 26, 52, 12, 25.2532f, 60, 10.7981f, 25.2532f, 49, 17, 11, 48, 29, 22.2532f, 46, 29.3698f, 23, 62, 30.2018f, 22.2532f, 49, 17, 11, 45, 29.3698f, 7, 54, 29, 6.2532f, 56, 30.2019f, 6.2532f, 49, 17, 11, 47, 30.6302f, 24, 58, 29.7982f, 23.2532f, 50, 31, 23.2532f, 49, 17, 11}), PartPose.offset(0, 4, 0));
        root.addOrReplaceChild("brush_right", GroupBuilder.create()
                .addMesh(new float[]{0.5f, 0, 3, 0.5f, 0, -3, -0.5f, 0, 3, -0.5f, 0, -3, -2.3481f, 0, 1.933f, 2.8481f, 0, -1.067f, -2.8481f, 0, 1.067f, 2.3481f, 0, -1.933f, -2.8481f, 0, -1.067f, 2.3481f, 0, 1.933f, -2.3481f, 0, -1.933f, 2.8481f, 0, 1.067f}, new float[]{2, 5, 22, 0, 6, 22, 1, 6, 16, 3, 5, 16, 6, 24, 18, 4, 24, 19, 5, 30, 19, 7, 30, 18, 10, 7, 16, 8, 6, 16, 9, 6, 22, 11, 7, 22}), PartPose.offset(4, 0.8f, -7));
        root.addOrReplaceChild("brush_left", GroupBuilder.create()
                .addMesh(new float[]{0.5f, 0, 3, 0.5f, 0, -3, -0.5f, 0, 3, -0.5f, 0, -3, -2.3481f, 0, 1.933f, 2.8481f, 0, -1.067f, -2.8481f, 0, 1.067f, 2.3481f, 0, -1.933f, -2.8481f, 0, -1.067f, 2.3481f, 0, 1.933f, -2.3481f, 0, -1.933f, 2.8481f, 0, 1.067f}, new float[]{2, 19, 33, 0, 20, 33, 1, 20, 27, 3, 19, 27, 6, 24, 19, 4, 24, 20, 5, 30, 20, 7, 30, 19, 10, 21, 27, 8, 20, 27, 9, 20, 33, 11, 21, 33}), PartPose.offset(-4, 0.8f, -7));

        return ModelDefinition.create(modelBuilder, 64, 64);
    }
}