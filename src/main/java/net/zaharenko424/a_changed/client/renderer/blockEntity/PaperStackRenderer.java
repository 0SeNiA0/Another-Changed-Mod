package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.AbstractStackEntity;
import net.zaharenko424.a_changed.entity.block.PaperStackEntity;
import net.zaharenko424.cmrs.client.geom.ModelPart;
import net.zaharenko424.cmrs.client.geom.builder.CubeUV;
import net.zaharenko424.cmrs.client.geom.builder.GroupBuilder;
import net.zaharenko424.cmrs.client.geom.builder.GroupDefinition;
import net.zaharenko424.cmrs.client.geom.builder.ModelDefinition;
import net.zaharenko424.cmrs.client.renderer.TransparencyType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PaperStackRenderer implements BlockEntityRenderer<PaperStackEntity> {

    private static final ResourceLocation PAPER_0 = AChanged.textureLoc("block/paper_stack/paper");
    private static final ResourceLocation PAPER_1 = AChanged.textureLoc("block/paper_stack/paper_written_0");
    private static final ResourceLocation PAPER_2 = AChanged.textureLoc("block/paper_stack/paper_written_1");
    private static final ResourceLocation PAPER_3 = AChanged.textureLoc("block/paper_stack/paper_written_2");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{PAPER_0, PAPER_1, PAPER_2, PAPER_3};
    private final ModelPart paper;

    public PaperStackRenderer(){
        paper = bodyLayer().bake().getDirectChild("paper");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        groupDefinition.addOrReplaceChild("paper", GroupBuilder.create()
                .addBox(-4f, 0f, -6f, 8, 0.5f, 12, new CubeUV().down(16, 0, 8, 12).north(8, 13.5f, 0, 13).east(12, 12.5f, 0, 12).up(8, 12, 0, 0).west(12, 14, 0, 13.5f).south(16, 13.5f, 8, 13)));

        return ModelDefinition.create(modelBuilder, 32, 32, 2);
    }

    @Override
    public void render(PaperStackEntity entity, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        NonNullList<AbstractStackEntity.Entry> list = entity.entries();
        if(list.isEmpty()) return;
        stack.translate(.5,0,.5);

        net.zaharenko424.cmrs.client.renderer.MultiBufferSource source = net.zaharenko424.cmrs.client.renderer.MultiBufferSource.getInstance();
        AbstractStackEntity.Entry entry;
        int id;
        for(int i = 0; i < list.size(); i++){
            paper.resetPose();
            paper.y = i * .5f;

            entry = list.get(i);
            paper.yRot = entry.rotation();

            id = entry.modelId();
            if(id > TEXTURES.length - 1) id = TEXTURES.length - 1;
            paper.render(stack, source.getBuffer(RenderType.entitySolid(TEXTURES[id]), TransparencyType.OPAQUE), packedLight, packedOverlay);
        }
    }
}