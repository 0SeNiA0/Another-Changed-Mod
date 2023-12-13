package net.zaharenko424.testmod.client.renderer.blockEntity;

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
import net.zaharenko424.testmod.block.blockEntity.LatexContainerEntity;
import net.zaharenko424.testmod.registry.BlockEntityRegistry;
import net.zaharenko424.testmod.registry.BlockRegistry;
import net.zaharenko424.testmod.util.Latex;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LatexContainerRenderer implements BlockEntityRenderer<LatexContainerEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.LATEX_CONTAINER_ENTITY.getId(), "latex");
    private static final ResourceLocation DARK = BlockRegistry.DARK_LATEX_BLOCK.getId().withPrefix("textures/block/").withSuffix(".png");
    private static final ResourceLocation WHITE = BlockRegistry.WHITE_LATEX_BLOCK.getId().withPrefix("textures/block/").withSuffix(".png");
    private final ModelPart latex;

    public LatexContainerRenderer(BlockEntityRendererProvider.Context p_173521_){
        latex=p_173521_.bakeLayer(LAYER).getChild("latex");
    }

    public static @NotNull LayerDefinition bodyLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("latex", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void render(LatexContainerEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        if(p_112307_.isEmpty()) return;
        latex.resetPose();
        p_112309_.pushPose();
        p_112309_.translate(.5,.125,.5);
        latex.yScale=p_112307_.getLatexAmount()*.75f;
        latex.render(p_112309_, p_112310_.getBuffer(RenderType.entitySolid(p_112307_.getLatexType()== Latex.DARK?DARK:WHITE)),p_112311_,p_112312_);
        p_112309_.popPose();
    }
}