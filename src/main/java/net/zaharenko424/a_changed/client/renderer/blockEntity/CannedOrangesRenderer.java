package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupBuilder;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.entity.block.CannedOrangesEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;

public class CannedOrangesRenderer implements BlockEntityRenderer<CannedOrangesEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.CANNED_ORANGES_ENTITY.getId(), "oranges");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("misc/oranges");
    private final ModelPart oranges;

    public CannedOrangesRenderer(){
        oranges = ModelDefinitionCache.INSTANCE.bake(LAYER).getChild("circle");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        groupDefinition.addOrReplaceChild("circle", GroupBuilder.create()
                .addMesh(new float[]{0f, 0.5f, -0f, 0.7456f, 0.5f, 1.8f, 1.8f, 0.5f, 0.7456f, 1.8f, 0.5f, -0.7456f, 0.7456f, 0.5f, -1.8f, -0.7456f, 0.5f, -1.8f, -1.8f, 0.5f, -0.7456f, -1.8f, 0.5f, 0.7456f, -0.7456f, 0.5f, 1.8f}, new float[]{0, 2, 1.8f, 7, 0.2f, 2.5456f, 8, 1.2544f, 3.6f, 1, 2.7456f, 3.6f, 1, 2.7456f, 3.6f, 2, 3.8f, 2.5456f, 3, 3.8f, 1.0544f, 0, 2, 1.8f, 0, 2, 1.8f, 5, 1.2544f, 0, 6, 0.2f, 1.0544f, 7, 0.2f, 2.5456f, 3, 3.8f, 1.0544f, 4, 2.7456f, 0, 5, 1.2544f, 0, 0, 2, 1.8f}), PartPose.offset(8, 0, 8));

        return ModelDefinition.create(modelBuilder, 16, 16);
    }

    @Override
    public void render(@NotNull CannedOrangesEntity can, float pPartialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int pPackedLight, int pPackedOverlay) {
        if(!can.hasFoodLeft()) return;
        oranges.resetPose();
        oranges.y = can.getFoodLeft();
        oranges.render(poseStack, buffer.getBuffer(RenderType.entitySolid(TEXTURE)), pPackedLight, pPackedOverlay);
    }
}