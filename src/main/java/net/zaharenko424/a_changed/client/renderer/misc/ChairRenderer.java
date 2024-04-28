package net.zaharenko424.a_changed.client.renderer.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
import net.zaharenko424.a_changed.client.cmrs.geom.*;
import net.zaharenko424.a_changed.entity.RotatingChairEntity;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

public class ChairRenderer extends EntityRenderer<RotatingChairEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(EntityRegistry.CHAIR_ENTITY.getId(), "chair");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("block/chair");
    private final ModelPart chair;

    public ChairRenderer(EntityRendererProvider.Context context) {
        super(context);
        chair = ModelDefinitionCache.INSTANCE.bake(LAYER).getChild("root");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        groupDefinition.addOrReplaceChild("root", GroupBuilder.create()
                .addBox(-5f, 1f, 5f, 1, 2, 1, new CubeUV().south(27, 18, 26, 16).north(27, 14, 26, 12).west(27, 26, 26, 24).down(28, 14, 27, 15).up(28, 14, 27, 13).east(27, 16, 26, 14))
                .addBox(4f, 1f, 5f, 1, 2, 1, new CubeUV().south(11, 28, 10, 26).north(9, 28, 8, 26).west(12, 28, 11, 26).down(28, 12, 27, 13).up(27, 27, 26, 26).east(10, 28, 9, 26))
                .addBox(-6f, -1f, -6f, 12, 2, 12, new CubeUV().south(36, 10, 24, 8).north(36, 6, 24, 4).west(36, 12, 24, 10).down(12, 12, 0, 24).up(12, 12, 0, 0).east(36, 8, 24, 6))
                .addBox(-6f, 3f, 4f, 12, 7, 2, new CubeUV().south(24, 14, 12, 7).north(24, 7, 12, 0).west(20, 31, 18, 24).down(36, 2, 24, 4).up(36, 2, 24, 0).east(18, 31, 16, 24)));

        return ModelDefinition.create(modelBuilder, 64, 64);
    }

    @Override
    public void render(@NotNull RotatingChairEntity entity, float pEntityYaw, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        chair.yRot = Mth.rotLerp(pPartialTick, -entity.yRotO + 180, -entity.getYRot() + 180) * Mth.DEG_TO_RAD;
        chair.render(pPoseStack, pBuffer.getBuffer(RenderType.entitySolid(TEXTURE)),pPackedLight, OverlayTexture.NO_OVERLAY);
    }

    @Override
    protected void renderNameTag(@NotNull RotatingChairEntity pEntity, @NotNull Component pDisplayName, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {}

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull RotatingChairEntity pEntity) {
        return TEXTURE;
    }
}