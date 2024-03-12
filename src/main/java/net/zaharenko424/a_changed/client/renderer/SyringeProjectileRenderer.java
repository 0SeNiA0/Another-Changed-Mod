package net.zaharenko424.a_changed.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.ModelPartCache;
import net.zaharenko424.a_changed.client.cmrs.geom.*;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

public class SyringeProjectileRenderer extends EntityRenderer<SyringeProjectile> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(EntityRegistry.SYRINGE_PROJECTILE.getId(), "main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/syringe_projectile");
    private final ModelPart root;

    public SyringeProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        root = ModelPartCache.INSTANCE.bake(LAYER).getChild("root");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        groupDefinition.addOrReplaceChild("root", GroupBuilder.create()
                .addBox(-0.9f, 7f, -0.9f, 1.8f, 5.5f, 1.8f, new CubeUV().west(2f, 12f, 0f, 6f).north(2f, 6f, 0f, 0f).east(4f, 6f, 2f, 0f).down(8f, 0f, 6f, 2f).south(6f, 6f, 4f, 0f))
                .addBox(-1.1f, 12.5f, -1.1f, 2.2f, 0.5f, 2.2f, new CubeUV().west(12f, 10f, 10f, 9f).north(12f, 7f, 10f, 6f).east(12f, 8f, 10f, 7f).down(10f, 6f, 8f, 8f).up(10f, 6f, 8f, 4f).south(12f, 9f, 10f, 8f))
                .addBox(-0.6f, 12f, -0.6f, 1.2f, 0.5f, 1.2f, new CubeUV().west(5f, 12f, 4f, 11f).north(12f, 3f, 11f, 2f).east(4f, 12f, 3f, 11f).down(7f, 11f, 6f, 12f).up(6f, 12f, 5f, 11f).south(12f, 4f, 11f, 3f))
                .addBox(-0.6f, 7.5f, -0.6f, 1.2f, 4f, 1.2f, new CubeUV().west(3f, 14f, 2f, 10f).north(9f, 12f, 8f, 8f).east(10f, 12f, 9f, 8f).down(11f, 11f, 10f, 12f).up(8f, 12f, 7f, 11f).south(11f, 4f, 10f, 0f))
                .addBox(-0.3f, 3f, -0.3f, 0.6f, 4f, 0.6f, new CubeUV().west(5f, 10f, 4f, 6f).north(3f, 10f, 2f, 6f).east(7f, 6f, 6f, 2f).down(11f, 10f, 10f, 11f).up(8f, 11f, 7f, 10f).south(4f, 10f, 3f, 6f))
                .addBox(-0.9f, 16f, -0.9f, 1.8f, 0.5f, 1.8f, new CubeUV().west(12f, 6f, 10f, 5f).north(5f, 11f, 3f, 10f).east(12f, 5f, 10f, 4f).down(10f, 2f, 8f, 4f).up(10f, 2f, 8f, 0f).south(7f, 11f, 5f, 10f))
                .addBox(-0.2f, 12f, -0.2f, 0.4f, 4f, 0.4f, new CubeUV().west(8f, 10f, 7f, 6f).north(6f, 10f, 5f, 6f).east(7f, 10f, 6f, 6f).down(12f, 1f, 11f, 2f).up(12f, 1f, 11f, 0f).south(8f, 6f, 7f, 2f)), PartPose.offsetAndRotation(9f, 0f, 0f, 0f, 0f, 1.5708f));

        return ModelDefinition.create(modelBuilder, 16, 16);
    }

    @Override
    public void render(@NotNull SyringeProjectile pEntity, float pEntityYaw, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot())));

        float f9 = (float)pEntity.shakeTime - pPartialTick;
        if (f9 > 0.0F) {
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(-Mth.sin(f9 * 3.0F) * f9));
        }

        root.render(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE)), pPackedLight, OverlayTexture.NO_OVERLAY);

        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SyringeProjectile pEntity) {
        return TEXTURE;
    }
}