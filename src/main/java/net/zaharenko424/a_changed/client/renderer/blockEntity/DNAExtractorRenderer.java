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
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("misc/dna_extractor_entity");
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
                .addBox(-1f, 11f, -1f, 2f, 3f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(24f, 6f, 22f, 8f), Direction.SOUTH, new UVData(20f, 12f, 18f, 9f), Direction.WEST, new UVData(12f, 21f, 10f, 18f), Direction.NORTH, new UVData(20f, 9f, 18f, 6f), Direction.UP, new UVData(24f, 6f, 22f, 4f), Direction.EAST, new UVData(10f, 21f, 8f, 18f)))
                .addBox(-2f, 10f, -2f, 4f, 1f, 4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(4f, 4f, 0f, 8f), Direction.SOUTH, new UVData(26f, 11f, 22f, 10f), Direction.WEST, new UVData(26f, 12f, 22f, 11f), Direction.NORTH, new UVData(26f, 9f, 22f, 8f), Direction.UP, new UVData(4f, 4f, 0f, 0f), Direction.EAST, new UVData(26f, 10f, 22f, 9f))), PartPose.offset(0f, 0f, 0f));
        root.addOrReplaceChild("tube0", GroupBuilder.create()
                .addBox(-0.7f, 11.5f, 4.8f, 1.4f, 4f, 1.4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(7.5f, 24f, 6f, 25.5f), Direction.SOUTH, new UVData(17.5f, 22f, 16f, 18f), Direction.WEST, new UVData(19.5f, 20f, 18f, 16f), Direction.NORTH, new UVData(19.5f, 16f, 18f, 12f), Direction.UP, new UVData(25.5f, 5.5f, 24f, 4f), Direction.EAST, new UVData(15.5f, 22f, 14f, 18f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, 0.7854f, 0f));
        root.addOrReplaceChild("tube1", GroupBuilder.create()
                .addBox(-0.7f, 11.5f, 16.8f, 1.4f, 4f, 1.4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(13.5f, 24f, 12f, 25.5f), Direction.SOUTH, new UVData(21.5f, 12f, 20f, 8f), Direction.WEST, new UVData(13.5f, 24f, 12f, 20f), Direction.NORTH, new UVData(21.5f, 4f, 20f, 0f), Direction.UP, new UVData(25.5f, 7.5f, 24f, 6f), Direction.EAST, new UVData(21.5f, 8f, 20f, 4f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, 0.7854f, 0f));
        root.addOrReplaceChild("tube2", GroupBuilder.create()
                .addBox(5.3f, 11.5f, 10.8f, 1.4f, 4f, 1.4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(15.5f, 24f, 14f, 25.5f), Direction.SOUTH, new UVData(19.5f, 24f, 18f, 20f), Direction.WEST, new UVData(21.5f, 24f, 20f, 20f), Direction.NORTH, new UVData(21.5f, 16f, 20f, 12f), Direction.UP, new UVData(25.5f, 13.5f, 24f, 12f), Direction.EAST, new UVData(21.5f, 20f, 20f, 16f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, 0.7854f, 0f));
        root.addOrReplaceChild("tube3", GroupBuilder.create()
                .addBox(-6.7f, 11.5f, 10.8f, 1.4f, 4f, 1.4f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(17.5f, 24f, 16f, 25.5f), Direction.SOUTH, new UVData(1.5f, 26f, 0f, 22f), Direction.WEST, new UVData(23.5f, 4f, 22f, 0f), Direction.NORTH, new UVData(9.5f, 25f, 8f, 21f), Direction.UP, new UVData(25.5f, 15.5f, 24f, 14f), Direction.EAST, new UVData(11.5f, 25f, 10f, 21f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, 0.7854f, 0f));
        root.addOrReplaceChild("cube", GroupBuilder.create()
                .addBox(6.53f, 14f, -0.8f, 9.95f, 1.5f, 1.6f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(14f, 6f, 4f, 7.5f), Direction.SOUTH, new UVData(14f, 3.5f, 4f, 2f), Direction.WEST, new UVData(3.5f, 25.5f, 2f, 24f), Direction.NORTH, new UVData(14f, 1.5f, 4f, 0f), Direction.UP, new UVData(14f, 5.5f, 4f, 4f), Direction.EAST, new UVData(25.5f, 1.5f, 24f, 0f)))
                .addBox(4.5f, 11f, -1f, 2f, 6f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(16f, 22f, 14f, 24f), Direction.SOUTH, new UVData(8f, 18f, 6f, 12f), Direction.WEST, new UVData(10f, 18f, 8f, 12f), Direction.NORTH, new UVData(4f, 18f, 2f, 12f), Direction.UP, new UVData(24f, 14f, 22f, 12f), Direction.EAST, new UVData(6f, 18f, 4f, 12f)))
                .addBox(16.5f, 11f, -1f, 2f, 6f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(18f, 22f, 16f, 24f), Direction.SOUTH, new UVData(16f, 12f, 14f, 6f), Direction.WEST, new UVData(14f, 20f, 12f, 14f), Direction.NORTH, new UVData(14f, 14f, 12f, 8f), Direction.UP, new UVData(24f, 16f, 22f, 14f), Direction.EAST, new UVData(16f, 6f, 14f, 0f)))
                .addBox(10.7f, 14f, -4.99f, 1.6f, 1.51f, 9.95f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(1.5f, 12f, 0f, 22f), Direction.SOUTH, new UVData(5.5f, 25.5f, 4f, 24f), Direction.WEST, new UVData(10f, 11.5f, 0f, 10f), Direction.NORTH, new UVData(25.5f, 3.5f, 24f, 2f), Direction.UP, new UVData(11.5f, 18f, 10f, 8f), Direction.EAST, new UVData(10f, 9.5f, 0f, 8f)))
                .addBox(10.5f, 11f, -7f, 2f, 6f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(24f, 18f, 22f, 20f), Direction.SOUTH, new UVData(18f, 12f, 16f, 6f), Direction.WEST, new UVData(18f, 18f, 16f, 12f), Direction.NORTH, new UVData(16f, 18f, 14f, 12f), Direction.UP, new UVData(24f, 18f, 22f, 16f), Direction.EAST, new UVData(18f, 6f, 16f, 0f)))
                .addBox(10.5f, 11f, 5f, 2f, 6f, 2f, new Vector3f(), ImmutableMap.of(Direction.DOWN, new UVData(24f, 22f, 22f, 24f), Direction.SOUTH, new UVData(6f, 24f, 4f, 18f), Direction.WEST, new UVData(8f, 24f, 6f, 18f), Direction.NORTH, new UVData(20f, 6f, 18f, 0f), Direction.UP, new UVData(24f, 22f, 22f, 20f), Direction.EAST, new UVData(4f, 24f, 2f, 18f))), PartPose.offsetAndRotation(-8f, 0f, -8f, 0f, -0.7854f, 0f));

        return ModelDefinition.create(meshDefinition, 64, 64, 2);
    }

    @Override
    public void render(@NotNull DNAExtractorEntity extractor, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource pBuffer, int packedLight, int pPackedOverlay) {
        ItemStackHandler inv = extractor.getInventory();
        prepareTubes();
        for(int i = 0; i < 4; i++){
            if(inv.getStackInSlot(i).isEmpty()) continue;
            tubes[i].visible = true;
        }

        root.yRot = Mth.rotLerp(partialTick, extractor.getRotO(), extractor.getRot()) * Mth.DEG_TO_RAD;

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