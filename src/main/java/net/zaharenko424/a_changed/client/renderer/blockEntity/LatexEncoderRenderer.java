package net.zaharenko424.a_changed.client.renderer.blockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.ModelCache;
import net.zaharenko424.a_changed.client.model.geom.*;
import net.zaharenko424.a_changed.entity.block.machines.LatexEncoderEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexEncoderRenderer implements BlockEntityRenderer<LatexEncoderEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.LATEX_ENCODER_ENTITY.getId(), "main");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("misc/latex_encoder_entity");
    private final ModelPart root;
    private final ModelPart latexBase;
    private final ModelPart dnaRoot;
    private final ModelPart[] dna = new ModelPart[3];

    public LatexEncoderRenderer(){
        root = ModelCache.INSTANCE.bake(LAYER).getChild("root");
        latexBase = root.getChild("latexBase");
        dnaRoot = root.getChild("dna");
        dna[0] = dnaRoot.getChild("dna0");
        dna[1] = dnaRoot.getChild("dna1");
        dna[2] = dnaRoot.getChild("dna2");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        MeshDefinition meshDefinition = new MeshDefinition();
        GroupDefinition groupDefinition = meshDefinition.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        root.addOrReplaceChild("latexBase", GroupBuilder.create()
                .addBox(3.5f, 13f, 3.5f, 1f, 4f, 1f, new UV().south(1f, 4f, 0f, 0f).north(1f, 4f, 0f, 0f).west(1f, 4f, 0f, 0f).up(0f, 0f, 1f, 1f).down(0f, 0f, 1f, 1f).east(1f, 4f, 0f, 0f)));
        GroupDefinition dna = root.addOrReplaceChild("dna", GroupBuilder.create());
        dna.addOrReplaceChild("dna0", GroupBuilder.create()
                .addBox(-1.4f, 13f, 3.1f, 1.8f, 3f, 1.8f, new UV().south(2f, 6f, 0f, 3f).north(2f, 3f, 0f, 0f).west(4f, 6f, 2f, 3f).up(10f, 2f, 8f, 0f).down(10f, 2f, 8f, 4f).east(4f, 3f, 2f, 0f))
                .addBox(-0.9f, 13f, 3.6f, 0.8f, 2.5f, 0.8f, new UV().south(5f, 11.5f, 4f, 9f).north(3f, 11.5f, 2f, 9f).west(6f, 11.5f, 5f, 9f).up(1f, 12f, 0f, 11f).down(12f, 0f, 11f, 1f).east(4f, 11.5f, 3f, 9f)));
        dna.addOrReplaceChild("dna1", GroupBuilder.create()
                .addBox(-3.4f, 13f, 3.1f, 1.8f, 3f, 1.8f, new UV().south(2f, 9f, 0f, 6f).north(6f, 3f, 4f, 0f).west(8f, 3f, 6f, 0f).up(10f, 6f, 8f, 4f).down(10f, 6f, 8f, 8f).east(6f, 6f, 4f, 3f))
                .addBox(-2.9f, 13f, 3.6f, 0.8f, 2.5f, 0.8f, new UV().south(11f, 2.5f, 10f, 0f).north(7f, 11.5f, 6f, 9f).west(11f, 5.5f, 10f, 3f).up(2f, 12f, 1f, 11f).down(12f, 1f, 11f, 2f).east(8f, 11.5f, 7f, 9f)));
        dna.addOrReplaceChild("dna2", GroupBuilder.create()
                .addBox(-5.4f, 13f, 3.1f, 1.8f, 3f, 1.8f, new UV().south(6f, 9f, 4f, 6f).north(4f, 9f, 2f, 6f).west(8f, 9f, 6f, 6f).up(10f, 10f, 8f, 8f).down(2f, 9f, 0f, 11f).east(8f, 6f, 6f, 3f))
                .addBox(-4.9f, 13f, 3.6f, 0.8f, 2.5f, 0.8f, new UV().south(10f, 12.5f, 9f, 10f).north(11f, 8.5f, 10f, 6f).west(11f, 11.5f, 10f, 9f).up(12f, 3f, 11f, 2f).down(12f, 3f, 11f, 4f).east(9f, 12.5f, 8f, 10f)));

        return ModelDefinition.create(meshDefinition, 32, 32, 2);
    }

    @Override
    public void render(@NotNull LatexEncoderEntity encoder, float pPartialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int pPackedLight, int pPackedOverlay) {
        root.resetPose();
        Direction direction = encoder.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        root.yRot = (direction.getAxis() == Direction.Axis.X ? direction.toYRot() : direction.getOpposite().toYRot()) * Mth.DEG_TO_RAD;
        poseStack.pushPose();
        poseStack.translate(.5,0,.5);
        ItemStackHandler inv = encoder.getInventory();

        ItemStack item = inv.getStackInSlot(1);
        if(!item.isEmpty() && (item.is(ItemRegistry.DARK_LATEX_BASE.get()) || item.is(ItemRegistry.WHITE_LATEX_BASE.get()))) {
            latexBase.visible = true;
            dnaRoot.visible = false;
            root.render(poseStack, buffer.getBuffer(
                    RenderType.entitySolid(item.is(ItemRegistry.DARK_LATEX_BASE.get()) ? LatexContainerRenderer.DARK : LatexContainerRenderer.WHITE)),
                    pPackedLight, OverlayTexture.NO_OVERLAY);
        }

        boolean render = false;
        boolean b;
        for(int i = 2; i < 5; i++){
            b = !inv.getStackInSlot(i).isEmpty();
            dna[i - 2].visible = b;
            if(b) render = true;
        }

        if(!render) {
            poseStack.popPose();
            return;
        }

        latexBase.visible = false;
        dnaRoot.visible = true;
        root.render(poseStack, buffer.getBuffer(RenderType.entityTranslucent(TEXTURE)), pPackedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}