package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
import net.zaharenko424.a_changed.client.cmrs.geom.*;
import net.zaharenko424.a_changed.entity.block.machines.DNAExtractorEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorRenderer implements BlockEntityRenderer<DNAExtractorEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.DNA_EXTRACTOR_ENTITY.getId(), "main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("misc/dna_extractor_entity");
    private final ModelPart root;
    private final ModelPart[] tubes = new ModelPart[4];

    public DNAExtractorRenderer(){
        root = ModelDefinitionCache.INSTANCE.bake(LAYER).getChild("root");
        tubes[0] = root.getChild("tube0");
        tubes[1] = root.getChild("tube1");
        tubes[2] = root.getChild("tube2");
        tubes[3] = root.getChild("tube3");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create()
                .addBox(-1f, 11f, -1f, 2, 3, 2, new CubeUV().west(12, 21, 10, 18).north(20, 9, 18, 6).east(10, 21, 8, 18).up(24, 6, 22, 4).down(24, 6, 22, 8).south(20, 12, 18, 9))
                .addBox(-2f, 10f, -2f, 4, 1, 4, new CubeUV().west(26, 12, 22, 11).north(26, 9, 22, 8).east(26, 10, 22, 9).up(4, 4, 0, 0).down(4, 4, 0, 8).south(26, 11, 22, 10)));
        root.addOrReplaceChild("tube0", GroupBuilder.create()
                .addBox(-0.7f, 11.5f, 4.8f, 1.4f, 4, 1.4f, new CubeUV().west(19.5f, 20, 18, 16).north(19.5f, 16, 18, 12).east(15.5f, 22, 14, 18).up(25.5f, 5.5f, 24, 4).down(7.5f, 24, 6, 25.5f).south(17.5f, 22, 16, 18)), PartPose.offsetAndRotation(-8, 0, -8, 0, 0.7854f, 0));
        root.addOrReplaceChild("tube1", GroupBuilder.create()
                .addBox(-0.7f, 11.5f, 16.8f, 1.4f, 4, 1.4f, new CubeUV().west(13.5f, 24, 12, 20).north(21.5f, 4, 20, 0).east(21.5f, 8, 20, 4).up(25.5f, 7.5f, 24, 6).down(13.5f, 24, 12, 25.5f).south(21.5f, 12, 20, 8)), PartPose.offsetAndRotation(-8, 0, -8, 0, 0.7854f, 0));
        root.addOrReplaceChild("tube2", GroupBuilder.create()
                .addBox(5.3f, 11.5f, 10.8f, 1.4f, 4, 1.4f, new CubeUV().west(21.5f, 24, 20, 20).north(21.5f, 16, 20, 12).east(21.5f, 20, 20, 16).up(25.5f, 13.5f, 24, 12).down(15.5f, 24, 14, 25.5f).south(19.5f, 24, 18, 20)), PartPose.offsetAndRotation(-8, 0, -8, 0, 0.7854f, 0));
        root.addOrReplaceChild("tube3", GroupBuilder.create()
                .addBox(-6.7f, 11.5f, 10.8f, 1.4f, 4, 1.4f, new CubeUV().west(23.5f, 4, 22, 0).north(9.5f, 25, 8, 21).east(11.5f, 25, 10, 21).up(25.5f, 15.5f, 24, 14).down(17.5f, 24, 16, 25.5f).south(1.5f, 26, 0, 22)), PartPose.offsetAndRotation(-8, 0, -8, 0, 0.7854f, 0));
        root.addOrReplaceChild("cube", GroupBuilder.create()
                .addBox(6.53f, 14f, -0.8f, 9.95f, 1.5f, 1.6f, new CubeUV().west(3.5f, 25.5f, 2, 24).north(14, 1.5f, 4, 0).east(25.5f, 1.5f, 24, 0).up(14, 5.5f, 4, 4).down(14, 6, 4, 7.5f).south(14, 3.5f, 4, 2))
                .addBox(4.5f, 11f, -1f, 2, 6, 2, new CubeUV().west(10, 18, 8, 12).north(4, 18, 2, 12).east(6, 18, 4, 12).up(24, 14, 22, 12).down(16, 22, 14, 24).south(8, 18, 6, 12))
                .addBox(16.5f, 11f, -1f, 2, 6, 2, new CubeUV().west(14, 20, 12, 14).north(14, 14, 12, 8).east(16, 6, 14, 0).up(24, 16, 22, 14).down(18, 22, 16, 24).south(16, 12, 14, 6))
                .addBox(10.7f, 14f, -4.99f, 1.6f, 1.51f, 9.95f, new CubeUV().west(10, 11.5f, 0, 10).north(25.5f, 3.5f, 24, 2).east(10, 9.5f, 0, 8).up(11.5f, 18, 10, 8).down(1.5f, 12, 0, 22).south(5.5f, 25.5f, 4, 24))
                .addBox(10.5f, 11f, -7f, 2, 6, 2, new CubeUV().west(18, 18, 16, 12).north(16, 18, 14, 12).east(18, 6, 16, 0).up(24, 18, 22, 16).down(24, 18, 22, 20).south(18, 12, 16, 6))
                .addBox(10.5f, 11f, 5f, 2, 6, 2, new CubeUV().west(8, 24, 6, 18).north(20, 6, 18, 0).east(4, 24, 2, 18).up(24, 22, 22, 20).down(24, 22, 22, 24).south(6, 24, 4, 18)), PartPose.offsetAndRotation(-8, 0, -8, 0, -0.7854f, 0));

        return ModelDefinition.create(modelBuilder, 64, 64, 2);
    }

    @Override
    public void render(@NotNull DNAExtractorEntity extractor, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource pBuffer, int packedLight, int pPackedOverlay) {
        prepareTubes();
        ItemStackHandler inv = extractor.getInventory();
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