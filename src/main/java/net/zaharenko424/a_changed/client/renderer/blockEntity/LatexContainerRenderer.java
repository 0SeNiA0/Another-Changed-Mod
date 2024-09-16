package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.entity.block.LatexContainerEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LatexContainerRenderer implements BlockEntityRenderer<LatexContainerEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.LATEX_CONTAINER_ENTITY.getId(), "latex");
    static final ResourceLocation DARK = BlockRegistry.DARK_LATEX_BLOCK.getId().withPrefix("textures/block/").withSuffix(".png");
    static final ResourceLocation WHITE = BlockRegistry.WHITE_LATEX_BLOCK.getId().withPrefix("textures/block/").withSuffix(".png");
    private final ModelPart latex;

    public LatexContainerRenderer(BlockEntityRendererProvider.Context p_173521_){
        latex = p_173521_.bakeLayer(LAYER).getChild("latex");
    }

    public static @NotNull LayerDefinition bodyLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("latex", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void render(LatexContainerEntity entity, float p_112308_, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if(entity.isEmpty()) return;
        latex.resetPose();
        poseStack.pushPose();
        poseStack.translate(.5,.125,.5);
        latex.yScale = entity.getLatexAmount() * .75f;
        latex.render(poseStack, buffer.getBuffer(RenderType.entitySolid(entity.getLatexType() == Latex.DARK ? DARK : WHITE)), packedLight, packedOverlay);
        poseStack.popPose();
    }
}