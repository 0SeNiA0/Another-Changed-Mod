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
import net.zaharenko424.a_changed.entity.MilkPuddingEntity;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

public class MilkPuddingModel extends EntityModel<MilkPuddingEntity> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(EntityRegistry.MILK_PUDDING.getId(), "main");

    private final ModelPart root;

    public MilkPuddingModel(){
        root = ModelDefinitionCache.INSTANCE.bake(bodyLayer).getChild("root");
    }

    @Override
    public void setupAnim(MilkPuddingEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        root.resetPose();

        root.yRot = Mth.DEG_TO_RAD * pEntity.getYRot();
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack pPoseStack, @NotNull VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    public static ModelDefinition bodyLayer() {
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        groupDefinition.addOrReplaceChild("root", GroupBuilder.create()
                .addMesh(new float[]{-7, -3, -2.8995f, -7, -3, 2.8995f, -2.8995f, -3, 7, -2.8995f, -3, -7, 2.8995f, -3, 7, 7, -3, 2.8995f, 7, -3, -2.8995f, 2.8995f, -3, -7, 2.2782f, 0, 5.5f, 5.5f, 0, 2.2782f, 5.5f, 0, -2.2782f, 2.2782f, 0, -5.5f, -2.2782f, 0, -5.5f, -2.2782f, 0, 5.5f, -5.5f, 0, -2.2782f, -5.5f, 0, 2.2782f, 1.2282f, 2, 2.5f, 2.8284f, 2, 0.8284f, 2.8284f, 2, -0.8284f, 1.2282f, 2, -2.5f, -1.2282f, 2, -2.5f, -1.2282f, 2, 2.5f, -2.8284f, 2, -0.8284f, -2.8284f, 2, 0.8284f, -3.8137f, -4, 8.5f, -9, -4, 3.3137f, -8, -4, -3.8137f, -3.3137f, -4, -9, 3.3137f, -4, 8, 3.8137f, -4, -8, 8.5f, -4, -4.3137f, 9, -4, 3.3137f}, new float[]{20, 10, 15, 22, 7, 18.3431f, 23, 7, 21.6569f, 21, 10, 25, 16, 15, 20, 17, 18, 16.6569f, 18, 18, 13.3431f, 19, 15, 10, 19, 17, 0, 20, 12, 0, 21, 12, 10, 16, 17, 10, 9, 32, 10, 8, 25, 10, 4, 25, 13, 5, 32, 13, 10, 30, 15, 9, 23, 15, 5, 23, 18, 6, 30, 18, 11, 22, 24, 10, 15, 24, 6, 15, 27, 7, 22, 27, 12, 7, 23, 11, 0, 23, 7, 0, 26, 3, 7, 26, 8, 29, 4, 13, 22, 4, 2, 22, 7, 4, 29, 7, 14, 7, 26, 12, 0, 26, 3, 0, 29, 0, 7, 29, 15, 31, 7, 14, 24, 7, 0, 24, 10, 1, 31, 10, 13, 29, 24, 15, 22, 24, 1, 22, 27, 2, 29, 27, 17, 5, 21.9142f, 16, 4.8429f, 17.4152f, 8, 0.7202f, 16, 9, 0.7202f, 22.6226f, 18, 20.1569f, 20, 17, 16.8431f, 20, 9, 15.1863f, 24, 10, 21.8137f, 24, 19, 23.4065f, 0.1669f, 18, 27.9051f, 0, 10, 28.6232f, 4, 11, 22, 4, 20, 23.8137f, 11, 19, 19.1863f, 11, 11, 18.1863f, 15, 12, 24.8137f, 15, 16, 22.8137f, 7, 21, 18.1863f, 7, 13, 17.1863f, 11, 8, 23.8137f, 11, 22, 22, 5.9142f, 20, 21.8429f, 1.4152f, 12, 17.7202f, 0, 14, 17.7202f, 6.6226f, 23, 22.1569f, 16, 22, 18.8431f, 16, 14, 17.1863f, 20, 15, 23.8137f, 20, 21, 27.2167f, 20.1669f, 23, 22.7181f, 20, 15, 22, 24, 13, 28.6232f, 24, 24, 15, 11, 25, 10, 15.6863f, 26, 10, 22.3137f, 27, 15, 27, 28, 7, 0, 24, 0, 0, 27, 0, 16, 29, 7, 16, 25, 3, 7, 24, 9.6274f, 7, 2, 9.6274f, 6, 1, 3, 6, 26, 2, 10, 25, 8.6274f, 10, 1, 8.6274f, 9, 0, 2, 9, 27, 3, 13, 26, 9.6274f, 13, 0, 9.6274f, 12, 3, 3, 12, 24, 0, 1, 28, 6.6274f, 1, 4, 6.6274f, 0, 2, 0, 0, 29, 0, 9, 27, 6.6274f, 9, 3, 6.6274f, 8, 7, 0, 8, 30, 2, 7, 29, 8.6274f, 7, 7, 8.6274f, 6, 6, 2, 6, 31, 3, 5, 30, 9.6274f, 5, 6, 9.6274f, 4, 5, 3, 4, 28, 2, 2, 31, 8.6274f, 2, 5, 8.6274f, 1, 4, 2, 1, 29, 7, 16, 30, 12, 11.3137f, 31, 12, 4.6863f, 28, 7, 0}), PartPose.offset(0, 4, 0));

        return ModelDefinition.create(modelBuilder, 64, 64);
    }
}