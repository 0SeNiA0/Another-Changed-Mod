package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.ModelCache;
import net.zaharenko424.a_changed.client.model.geom.*;
import net.zaharenko424.a_changed.entity.block.machines.DNAExtractorEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class DNAExtractorRenderer implements BlockEntityRenderer<DNAExtractorEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.DNA_EXTRACTOR_ENTITY.getId(), "main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("block/dna_extractor");
    private final ModelPart root;
    private final ModelPart[] tubes = new ModelPart[4];

    public DNAExtractorRenderer(){
        root = ModelCache.INSTANCE.bake(LAYER).getChild("root");
        tubes[0] = root.getChild("tube0");
        tubes[1] = root.getChild("tube1");
        tubes[2] = root.getChild("tube2");
        tubes[3] = root.getChild("tube3");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        MeshDefinition meshDefinition = new MeshDefinition();
        GroupDefinition groupDefinition = meshDefinition.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create()
                .addBox(-1f, 11f, -1f, 2f, 3f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(32f, 50f, 30f, 52f), Direction.SOUTH, new UVData(52f, 29f, 50f, 26f), Direction.WEST, new UVData(30f, 53f, 28f, 50f), Direction.NORTH, new UVData(26f, 53f, 24f, 50f), Direction.UP, new UVData(52f, 31f, 50f, 29f), Direction.EAST, new UVData(28f, 53f, 26f, 50f)))
                .addBox(-2f, 10f, -2f, 4f, 1f, 4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(52f, 0f, 48f, 4f), Direction.SOUTH, new UVData(54f, 50f, 50f, 49f), Direction.WEST, new UVData(54f, 51f, 50f, 50f), Direction.NORTH, new UVData(54f, 48f, 50f, 47f), Direction.UP, new UVData(4f, 52f, 0f, 48f), Direction.EAST, new UVData(54f, 49f, 50f, 48f))), PartPose.offset(0f, 0f, 0f));
        root.addOrReplaceChild("tube0", GroupBuilder.create()
                .addBox(-0.7f, 11.5f, 4.8f, 1.4f, 4f, 1.4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(50f, 33f, 51.4f, 32.9f), Direction.SOUTH, new UVData(51.4f, 35.5f, 50f, 33f), Direction.WEST, new UVData(51.4f, 35.5f, 50f, 33f), Direction.NORTH, new UVData(51.4f, 35.5f, 50f, 33f), Direction.UP, new UVData(50f, 33f, 51.4f, 32.9f), Direction.EAST, new UVData(51.4f, 35.5f, 50f, 33f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, 0.7854f, 0f));
        root.addOrReplaceChild("tube1", GroupBuilder.create()
                .addBox(-0.7f, 11.5f, 16.8f, 1.4f, 4f, 1.4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(50.5f, 33f, 51.9f, 33.5f), Direction.SOUTH, new UVData(51.9f, 35.5f, 50.5f, 33f), Direction.WEST, new UVData(51.9f, 35.5f, 50.5f, 33f), Direction.NORTH, new UVData(51.9f, 35.5f, 50.5f, 33f), Direction.UP, new UVData(50.5f, 33f, 51.9f, 33.5f), Direction.EAST, new UVData(51.9f, 35.5f, 50.5f, 33f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, 0.7854f, 0f));
        root.addOrReplaceChild("tube2", GroupBuilder.create()
                .addBox(5.3f, 11.5f, 10.8f, 1.4f, 4f, 1.4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(50.5f, 33f, 51.9f, 33.5f), Direction.SOUTH, new UVData(51.9f, 35.5f, 50.5f, 33f), Direction.WEST, new UVData(51.9f, 35.5f, 50.5f, 33f), Direction.NORTH, new UVData(51.9f, 35.5f, 50.5f, 33f), Direction.UP, new UVData(50.5f, 33f, 51.9f, 33.5f), Direction.EAST, new UVData(51.9f, 35.5f, 50.5f, 33f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, 0.7854f, 0f));
        root.addOrReplaceChild("tube3", GroupBuilder.create()
                .addBox(-6.7f, 11.5f, 10.8f, 1.4f, 4f, 1.4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(50.5f, 33f, 51.9f, 33.5f), Direction.SOUTH, new UVData(51.9f, 35.5f, 50.5f, 33f), Direction.WEST, new UVData(51.9f, 35.5f, 50.5f, 33f), Direction.NORTH, new UVData(51.9f, 35.5f, 50.5f, 33f), Direction.UP, new UVData(50.5f, 33f, 51.9f, 33.5f), Direction.EAST, new UVData(51.9f, 35.5f, 50.5f, 33f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, 0.7854f, 0f));
        root.addOrReplaceChild("cube", GroupBuilder.create()
                .addBox(6.53f, 14f, -0.8f, 9.95f, 1.5f, 1.6f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(58f, 8f, 48f, 9.5f), Direction.SOUTH, new UVData(58f, 5.5f, 48f, 4f), Direction.WEST, new UVData(1.5f, 53.5f, 0f, 52f), Direction.NORTH, new UVData(14f, 49.5f, 4f, 48f), Direction.UP, new UVData(58f, 7.5f, 48f, 6f), Direction.EAST, new UVData(51.5f, 52.5f, 50f, 51f)))
                .addBox(4.5f, 11f, -1f, 2f, 6f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(52f, 33f, 50f, 35f), Direction.SOUTH, new UVData(50f, 42f, 48f, 36f), Direction.WEST, new UVData(50f, 48f, 48f, 42f), Direction.NORTH, new UVData(50f, 30f, 48f, 24f), Direction.UP, new UVData(52f, 33f, 50f, 31f), Direction.EAST, new UVData(50f, 36f, 48f, 30f)))
                .addBox(16.5f, 11f, -1f, 2f, 6f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(52f, 37f, 50f, 39f), Direction.SOUTH, new UVData(8f, 56f, 6f, 50f), Direction.WEST, new UVData(10f, 56f, 8f, 50f), Direction.NORTH, new UVData(50f, 54f, 48f, 48f), Direction.UP, new UVData(52f, 37f, 50f, 35f), Direction.EAST, new UVData(6f, 56f, 4f, 50f)))
                .addBox(10.7f, 14f, -4.99f, 1.6f, 1.51f, 9.95f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(49.5f, 14f, 48f, 24f), Direction.SOUTH, new UVData(3.5f, 53.5f, 2f, 52f), Direction.WEST, new UVData(58f, 13.5f, 48f, 12f), Direction.NORTH, new UVData(53.5f, 1.5f, 52f, 0f), Direction.UP, new UVData(15.5f, 58f, 14f, 48f), Direction.EAST, new UVData(58f, 11.5f, 48f, 10f)))
                .addBox(10.5f, 11f, -7f, 2f, 6f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(52f, 41f, 50f, 43f), Direction.SOUTH, new UVData(52f, 20f, 50f, 14f), Direction.WEST, new UVData(18f, 56f, 16f, 50f), Direction.NORTH, new UVData(12f, 56f, 10f, 50f), Direction.UP, new UVData(52f, 41f, 50f, 39f), Direction.EAST, new UVData(14f, 56f, 12f, 50f)))
                .addBox(10.5f, 11f, 5f, 2f, 6f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(52f, 45f, 50f, 47f), Direction.SOUTH, new UVData(52f, 26f, 50f, 20f), Direction.WEST, new UVData(24f, 56f, 22f, 50f), Direction.NORTH, new UVData(20f, 56f, 18f, 50f), Direction.UP, new UVData(52f, 45f, 50f, 43f), Direction.EAST, new UVData(22f, 56f, 20f, 50f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, -0.7854f, 0f));

        return ModelDefinition.create(meshDefinition, 128, 128, 2);
    }

    @Override
    public void render(@NotNull DNAExtractorEntity extractor, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource pBuffer, int packedLight, int pPackedOverlay) {
        ItemStackHandler inv = extractor.getInventory();
        prepareTubes();
        boolean empty = true;
        for(int i = 0; i < 4; i++){
            if(inv.getStackInSlot(i).isEmpty()) continue;
            empty = false;
            tubes[i].visible = true;
        }
        if(empty) return;

        if(extractor.hasAnyProgress()){
            int rot = extractor.getRot();
            root.yRot = Mth.rotLerp(partialTick, rot - 20, rot) * Mth.DEG_TO_RAD;
        }

        poseStack.pushPose();
        poseStack.translate(.5,0,.5);
        root.render(poseStack, pBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private void prepareTubes(){
        root.getAllParts().forEach(ModelPart::resetPose);
        for(ModelPart part : tubes){
            part.visible = false;
        }
    }
}