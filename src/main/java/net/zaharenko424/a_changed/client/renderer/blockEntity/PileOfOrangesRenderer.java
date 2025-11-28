package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.PileOfOrangesEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.cmrs.client.ModelDefinitionCache;
import net.zaharenko424.cmrs.client.geom.Node;
import net.zaharenko424.cmrs.client.geom.builder.CubeUV;
import net.zaharenko424.cmrs.client.geom.builder.GroupBuilder;
import net.zaharenko424.cmrs.client.geom.builder.GroupDefinition;
import net.zaharenko424.cmrs.client.geom.builder.ModelDefinition;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class PileOfOrangesRenderer implements BlockEntityRenderer<PileOfOrangesEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.PILE_OF_ORANGES_ENTITY.getId(), "orange");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("block/orange");
    private final Node orange;

    public PileOfOrangesRenderer(){
        orange = ModelDefinitionCache.getInstance().bake(LAYER).getDirectChild("root");
    }

    public static @NotNull ModelDefinition bodyLayer() {
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        groupDefinition.addOrReplaceChild("root", GroupBuilder.create()
                .addBox(-2f, 0f, -2f, 4, 4, 4, new CubeUV().west(4, 4, 2, 2).north(2, 2, 0, 0).east(4, 4, 2, 2).up(2, 6, 0, 4).down(6, 0, 4, 2).south(2, 2, 0, 0)));

        return ModelDefinition.create(modelBuilder, 32, 32, 2);
    }

    @Override
    public void render(@NotNull PileOfOrangesEntity blockEntity, float partialTick, @NotNull PoseStack stack, @NotNull MultiBufferSource source, int packedLight, int packedOverlay) {
        PileOfOrangesEntity.Orange orange1;
        Vector3f translation = orange.translation(), rotation = orange.rotation();
        Vec3 pos;
        for(Pair<PileOfOrangesEntity.Orange, AABB> pair : blockEntity.getOranges()){
            orange1 = pair.getKey();
            pos = orange1.pos();

            orange.resetPose();
            translation.add((float) (pos.x * 16), (float) (pos.y * 16), (float) (pos.z * 16));
            rotation.y = orange1.rotRad();
            orange.render(stack, source.getBuffer(RenderType.entitySolid(TEXTURE)), packedLight, packedOverlay);
        }
    }
}