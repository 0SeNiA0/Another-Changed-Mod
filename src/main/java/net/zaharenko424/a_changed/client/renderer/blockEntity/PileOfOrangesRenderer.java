package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
import net.zaharenko424.a_changed.client.cmrs.geom.*;
import net.zaharenko424.a_changed.entity.block.PileOfOrangesEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class PileOfOrangesRenderer implements BlockEntityRenderer<PileOfOrangesEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.PILE_OF_ORANGES_ENTITY.getId(), "orange");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("block/orange");
    private final ModelPart orange;

    public PileOfOrangesRenderer(){
        orange = ModelDefinitionCache.INSTANCE.bake(LAYER).getChild("root");
    }

    public static @NotNull ModelDefinition bodyLayer() {
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        groupDefinition.addOrReplaceChild("root", GroupBuilder.create()
                .addBox(-2f, 0f, -2f, 4, 4, 4, new CubeUV().west(4, 4, 2, 2).north(2, 2, 0, 0).east(4, 4, 2, 2).up(2, 6, 0, 4).down(6, 0, 4, 2).south(2, 2, 0, 0)));

        return ModelDefinition.create(modelBuilder, 32, 32, 2);
    }

    @Override
    public void render(@NotNull PileOfOrangesEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        PileOfOrangesEntity.Orange orange1;
        for(Pair<PileOfOrangesEntity.Orange, AABB> pair : pBlockEntity.getOranges()){
            orange.resetPose();
            orange1 = pair.getKey();
            orange.offsetPos(orange1.pos().toVector3f().mul(16));
            orange.yRot = orange1.rotRad();
            orange.render(pPoseStack, pBuffer.getBuffer(RenderType.entitySolid(TEXTURE)), pPackedLight, pPackedOverlay);
        }
    }
}