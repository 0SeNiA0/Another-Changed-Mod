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
    public void renderToBuffer(@NotNull PoseStack pPoseStack, @NotNull VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    public static ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        root.addOrReplaceChild("body", GroupBuilder.create()
                .addMesh(new float[]{-8, -3, -3.3137f, -8, -3, 3.3137f, -3.3137f, -3, 8, -3.3137f, -3, -8, 3.3137f, -3, 8, 8, -3, 3.3137f, 8, -3, -3.3137f, 3.3137f, -3, -8, 3.3137f, 0, 8, 8, 0, 3.3137f, 8, 0, -3.3137f, 3.3137f, 0, -8, -3.3137f, 0, -8, -3.3137f, 0, 8, -8, 0, -3.3137f, -8, 0, 3.3137f, 2.3137f, 3, 5, 5.3284f, 3, 1.6569f, 5.3284f, 3, -1.6569f, 2.3137f, 3, -5, -2.3137f, 3, -5, -2.3137f, 3, 5, -5.3284f, 3, -1.6569f, -5.3284f, 3, 1.6569f, 5.3333f, -3, 2, 5.3333f, -3, -2, 2.6667f, -3, 2, 2.6667f, -3, -2, 5.3333f, -4, 2, 5.3333f, -4, -2, 2.6667f, -4, 2, 2.6667f, -4, -2, 2, -3.5f, 2, 2, -3, 2, 2, -3.5f, -2, 2, -3, -2, 6, -3.5f, 2, 6, -3, 2, 6, -3.5f, -2, 6, -3, -2, 5.3333f, -3.5f, -3, 5.3333f, -3, -3, 2.6667f, -3.5f, -3, 2.6667f, -3, -3, 5.3333f, -3.5f, 3, 5.3333f, -3, 3, 2.6667f, -3.5f, 3, 2.6667f, -3, 3, -2.6667f, -3, 2, -2.6667f, -3, -2, -5.3333f, -3, 2, -5.3333f, -3, -2, -2.6667f, -4, 2, -2.6667f, -4, -2, -5.3333f, -4, 2, -5.3333f, -4, -2, -6, -3.5f, 2, -6, -3, 2, -6, -3.5f, -2, -6, -3, -2, -2, -3.5f, 2, -2, -3, 2, -2, -3.5f, -2, -2, -3, -2, -2.6667f, -3.5f, -3, -2.6667f, -3, -3, -5.3333f, -3.5f, -3, -5.3333f, -3, -3, -2.6667f, -3.5f, 3, -2.6667f, -3, 3, -5.3333f, -3.5f, 3, -5.3333f, -3, 3}, new float[]{20, 10, 15, 22, 7, 18.3431f, 23, 7, 21.6569f, 21, 10, 25, 16, 15, 20, 17, 18, 16.6569f, 18, 18, 13.3431f, 19, 15, 10, 19, 17, 0, 20, 12, 0, 21, 12, 10, 16, 17, 10, 9, 32, 10, 8, 25, 10, 4, 25, 13, 5, 32, 13, 10, 30, 15, 9, 23, 15, 5, 23, 18, 6, 30, 18, 11, 22, 24, 10, 15, 24, 6, 15, 27, 7, 22, 27, 12, 7, 23, 11, 0, 23, 7, 0, 26, 3, 7, 26, 8, 29, 4, 13, 22, 4, 2, 22, 7, 4, 29, 7, 14, 7, 26, 12, 0, 26, 3, 0, 29, 0, 7, 29, 15, 31, 7, 14, 24, 7, 0, 24, 10, 1, 31, 10, 13, 29, 24, 15, 22, 24, 1, 22, 27, 2, 29, 27, 17, 5, 21.9142f, 16, 4.8429f, 17.4152f, 8, 0.7202f, 16, 9, 0.7202f, 22.6226f, 18, 20.1569f, 20, 17, 16.8431f, 20, 9, 15.1863f, 24, 10, 21.8137f, 24, 19, 23.4065f, 0.1669f, 18, 27.9051f, 0, 10, 28.6232f, 4, 11, 22, 4, 20, 23.8137f, 11, 19, 19.1863f, 11, 11, 18.1863f, 15, 12, 24.8137f, 15, 16, 22.8137f, 7, 21, 18.1863f, 7, 13, 17.1863f, 11, 8, 23.8137f, 11, 22, 22, 5.9142f, 20, 21.8429f, 1.4152f, 12, 17.7202f, 0, 14, 17.7202f, 6.6226f, 23, 22.1569f, 16, 22, 18.8431f, 16, 14, 17.1863f, 20, 15, 23.8137f, 20, 21, 27.2167f, 20.1669f, 23, 22.7181f, 20, 15, 22, 24, 13, 28.6232f, 24, 7, 7, 16, 6, 12, 11.3137f, 5, 12, 4.6863f, 4, 7, 0, 2, 15, 11, 1, 10, 15.6863f, 0, 10, 22.3137f, 3, 15, 27, 4, 7, 0, 2, 0, 0, 3, 0, 16, 7, 7, 16, 39, 22, 15, 37, 18, 15, 36, 18, 16, 38, 22, 16, 32, 29, 14, 33, 29, 13, 35, 25, 13, 34, 25, 14, 24, 10, 29, 25, 10, 25, 27, 7, 25, 26, 7, 29, 31, 10, 30, 29, 13, 30, 28, 13, 26, 30, 10, 26, 44, 12, 1, 45, 12, 0, 47, 9, 0, 46, 9, 1, 43, 15, 10, 41, 12, 10, 40, 12, 11, 42, 15, 11, 25, 21, 27, 24, 21, 31, 37, 22, 31, 39, 22, 27, 33, 22, 31, 26, 23, 31, 27, 23, 27, 35, 22, 27, 45, 27, 29, 24, 27, 28, 26, 24, 28, 47, 24, 29, 27, 28, 28, 25, 31, 28, 41, 31, 27, 43, 28, 27, 36, 29, 0, 28, 29, 1, 29, 33, 1, 38, 33, 0, 31, 29, 2, 30, 33, 2, 32, 33, 1, 34, 29, 1, 30, 29, 4, 28, 32, 4, 44, 32, 3, 46, 29, 3, 40, 29, 4, 29, 29, 5, 31, 32, 5, 42, 32, 4, 38, 10, 2, 40, 11, 2, 41, 11, 1, 39, 10, 1, 45, 11, 1, 44, 11, 2, 36, 12, 2, 37, 12, 1, 43, 11, 2, 42, 11, 3, 34, 12, 3, 35, 12, 2, 32, 12, 12, 46, 13, 12, 47, 13, 11, 33, 12, 11, 28, 30.6302f, 14, 36, 31, 13.2532f, 44, 29.7982f, 13.2532f, 33, 12, 11, 32, 5, 22.2532f, 30, 5.3698f, 23, 46, 6.2019f, 22.2532f, 33, 12, 11, 29, 29.3698f, 15, 38, 29, 14.2532f, 40, 30.2018f, 14.2532f, 33, 12, 11, 31, 8.6302f, 25, 42, 7.7981f, 24.2532f, 34, 9, 24.2532f, 33, 12, 11, 24, 21.1667f, 20, 37, 21.8333f, 20, 45, 21.1667f, 21, 33, 12, 11, 47, 16.8333f, 20, 26, 16.8333f, 19, 33, 16.1667f, 19, 33, 12, 11, 25, 21.1667f, 22, 41, 21.1667f, 21, 39, 21.8333f, 22, 33, 12, 11, 27, 15.8334f, 21, 43, 15.8334f, 20, 35, 15.1666f, 21, 33, 12, 11, 63, 29, 14, 61, 25, 14, 60, 25, 15, 62, 29, 15, 56, 27, 28, 57, 27, 27, 59, 23, 27, 58, 23, 28, 48, 16, 31, 49, 16, 27, 51, 13, 27, 50, 13, 31, 55, 16, 31, 53, 19, 31, 52, 19, 27, 54, 16, 27, 68, 31, 29, 69, 31, 28, 71, 28, 28, 70, 28, 29, 67, 3, 29, 65, 0, 29, 64, 0, 30, 66, 3, 30, 49, 27, 27, 48, 27, 31, 61, 28, 31, 63, 28, 27, 57, 23, 32, 50, 24, 32, 51, 24, 28, 59, 23, 28, 69, 32, 3, 48, 32, 2, 50, 29, 2, 71, 29, 3, 51, 3, 30, 49, 6, 30, 65, 6, 29, 67, 3, 29, 60, 28, 20, 52, 28, 21, 53, 32, 21, 62, 32, 20, 55, 28, 22, 54, 32, 22, 56, 32, 21, 58, 28, 21, 54, 29, 6, 52, 32, 6, 68, 32, 5, 70, 29, 5, 64, 6, 29, 53, 6, 30, 55, 9, 30, 66, 9, 29, 62, 4, 17, 64, 5, 17, 65, 5, 16, 63, 4, 16, 69, 7, 16, 68, 7, 17, 60, 8, 17, 61, 8, 16, 67, 16, 10, 66, 16, 11, 58, 17, 11, 59, 17, 10, 56, 17, 12, 70, 18, 12, 71, 18, 11, 57, 17, 11, 52, 11.6302f, 26, 60, 12, 25.2532f, 68, 10.7981f, 25.2532f, 57, 17, 11, 56, 29, 22.2532f, 54, 29.3698f, 23, 70, 30.2018f, 22.2532f, 57, 17, 11, 53, 29.3698f, 7, 62, 29, 6.2532f, 64, 30.2019f, 6.2532f, 57, 17, 11, 55, 30.6302f, 24, 66, 29.7982f, 23.2532f, 58, 31, 23.2532f, 57, 17, 11, 48, 21.1667f, 0, 61, 21.8333f, 0, 69, 21.1667f, 1, 57, 17, 11, 50, 22.8333f, 15, 57, 22.1667f, 15, 71, 22.8333f, 16, 57, 17, 11, 49, 15.1666f, 22, 65, 15.1666f, 21, 63, 15.8334f, 22, 57, 17, 11, 51, 7.8334f, 24, 67, 7.8334f, 23, 59, 7.1666f, 24, 57, 17, 11}), PartPose.offset(0, 4, 0));
        root.addOrReplaceChild("brush_right", GroupBuilder.create()
                .addMesh(new float[]{0.5f, 0, 3, 0.5f, 0, -3, -0.5f, 0, 3, -0.5f, 0, -3, -2.3481f, 0, 1.933f, 2.8481f, 0, -1.067f, -2.8481f, 0, 1.067f, 2.3481f, 0, -1.933f, -2.8481f, 0, -1.067f, 2.3481f, 0, 1.933f, -2.3481f, 0, -1.933f, 2.8481f, 0, 1.067f}, new float[]{2, 5, 22, 0, 6, 22, 1, 6, 16, 3, 5, 16, 6, 24, 18, 4, 24, 19, 5, 30, 19, 7, 30, 18, 10, 7, 16, 8, 6, 16, 9, 6, 22, 11, 7, 22}), PartPose.offset(4, 0.8f, -7));
        root.addOrReplaceChild("brush_left", GroupBuilder.create()
                .addMesh(new float[]{0.5f, 0, 3, 0.5f, 0, -3, -0.5f, 0, 3, -0.5f, 0, -3, -2.3481f, 0, 1.933f, 2.8481f, 0, -1.067f, -2.8481f, 0, 1.067f, 2.3481f, 0, -1.933f, -2.8481f, 0, -1.067f, 2.3481f, 0, 1.933f, -2.3481f, 0, -1.933f, 2.8481f, 0, 1.067f}, new float[]{2, 19, 33, 0, 20, 33, 1, 20, 27, 3, 19, 27, 6, 24, 19, 4, 24, 20, 5, 30, 20, 7, 30, 19, 10, 21, 27, 8, 20, 27, 9, 20, 33, 11, 21, 33}), PartPose.offset(-4, 0.8f, -7));

        return ModelDefinition.create(modelBuilder, 64, 64);
    }
}