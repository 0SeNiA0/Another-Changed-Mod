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
import net.minecraft.world.phys.AABB;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.ModelCache;
import net.zaharenko424.a_changed.client.model.geom.*;
import net.zaharenko424.a_changed.entity.block.CryoChamberEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class CryoChamberRenderer implements BlockEntityRenderer<CryoChamberEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.CRYO_CHAMBER_ENTITY.getId(), "fluid");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("misc/cryo_chamber");
    private final ModelPart fluid;

    public CryoChamberRenderer(){
        fluid = ModelCache.INSTANCE.bake(LAYER).getChild("fluid");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        MeshDefinition meshDefinition = new MeshDefinition();
        GroupDefinition groupDefinition = meshDefinition.getRoot();

        groupDefinition.addOrReplaceChild("fluid", GroupBuilder.create()
                .addBox(-17.82f, 0f, -1.98f, 19.8f, 31.68f, 19.8f, new Vector3f(), ImmutableMap.of(Direction.UP, new UVData(4.25f, 0.25f, 4.75f, 0.75f), Direction.DOWN, new UVData(5.25f, 0.25f, 5.75f, 0.75f)))
                .addBox(-19.8f, 0f, 0.99f, 0.99f, 31.68f, 13.86f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(6.75f, 0.75f, 6.25f, 0.25f), Direction.UP, new UVData(10.25f, 0.25f, 10.75f, 0.75f), Direction.SOUTH, new UVData(8.75f, 0.75f, 8.25f, 0.25f), Direction.WEST, new UVData(8.5f, 6.75f, 5.5f, 6.25f), Direction.DOWN, new UVData(11.25f, 0.25f, 11.75f, 0.75f)))
                .addBox(-18.81f, 0f, -1.98f, 0.99f, 31.68f, 19.8f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(12.75f, 0.75f, 12.25f, 0.25f), Direction.UP, new UVData(0.25f, 1.25f, 0.75f, 1.75f), Direction.SOUTH, new UVData(14.75f, 0.75f, 14.25f, 0.25f), Direction.WEST, new UVData(8.45f, 6.75f, 5.55f, 6.25f), Direction.DOWN, new UVData(1.25f, 1.25f, 1.75f, 1.75f)))
                .addBox(-20.79f, 0f, 2.97f, 0.99f, 31.68f, 9.9f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(2.75f, 1.75f, 2.25f, 1.25f), Direction.UP, new UVData(5.25f, 1.25f, 5.75f, 1.75f), Direction.SOUTH, new UVData(4.75f, 1.75f, 4.25f, 1.25f), Direction.WEST, new UVData(8.05f, 1.75f, 5.05f, 1.25f), Direction.DOWN, new UVData(7.25f, 1.25f, 7.75f, 1.75f)))
                .addBox(-14.85f, 0f, 18.81f, 13.86f, 31.68f, 0.99f, new Vector3f(), ImmutableMap.of(Direction.UP, new UVData(12.25f, 1.25f, 12.75f, 1.75f), Direction.SOUTH, new UVData(8.5f, 6.75f, 5.5f, 6.25f), Direction.WEST, new UVData(11.75f, 1.75f, 11.25f, 1.25f), Direction.DOWN, new UVData(13.25f, 1.25f, 13.75f, 1.75f), Direction.EAST, new UVData(9.75f, 1.75f, 9.25f, 1.25f)))
                .addBox(-12.87f, 0f, 19.8f, 9.9f, 31.68f, 0.99f, new Vector3f(), ImmutableMap.of(Direction.UP, new UVData(2.25f, 2.25f, 2.75f, 2.75f), Direction.SOUTH, new UVData(7.75f, 1.75f, 5.25f, 1.25f), Direction.WEST, new UVData(1.75f, 2.75f, 1.25f, 2.25f), Direction.DOWN, new UVData(3.25f, 2.25f, 3.75f, 2.75f), Direction.EAST, new UVData(15.75f, 1.75f, 15.25f, 1.25f)))
                .addBox(-9.9f, 0f, 20.79f, 3.96f, 31.68f, 0.99f, new Vector3f(), ImmutableMap.of(Direction.UP, new UVData(8.25f, 2.25f, 8.75f, 2.75f), Direction.SOUTH, new UVData(6.75f, 2.75f, 6.25f, 2.25f), Direction.WEST, new UVData(7.75f, 2.75f, 7.25f, 2.25f), Direction.DOWN, new UVData(9.25f, 2.25f, 9.75f, 2.75f), Direction.EAST, new UVData(5.75f, 2.75f, 5.25f, 2.25f)))
                .addBox(-17.82f, 0f, 17.82f, 19.8f, 31.68f, 0.99f, new Vector3f(), ImmutableMap.of(Direction.UP, new UVData(14.25f, 2.25f, 14.75f, 2.75f), Direction.SOUTH, new UVData(8.45f, 6.75f, 5.55f, 6.25f), Direction.WEST, new UVData(13.75f, 2.75f, 13.25f, 2.25f), Direction.DOWN, new UVData(15.25f, 2.25f, 15.75f, 2.75f), Direction.EAST, new UVData(11.75f, 2.75f, 11.25f, 2.25f)))
                .addBox(2.97f, 0f, 0.99f, 0.99f, 31.68f, 13.86f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(0.75f, 3.75f, 0.25f, 3.25f), Direction.UP, new UVData(4.25f, 3.25f, 4.75f, 3.75f), Direction.SOUTH, new UVData(2.75f, 3.75f, 2.25f, 3.25f), Direction.DOWN, new UVData(5.25f, 3.25f, 5.75f, 3.75f), Direction.EAST, new UVData(8.5f, 6.75f, 5.5f, 6.25f)))
                .addBox(3.96f, 0f, 2.97f, 0.99f, 31.68f, 9.9f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(6.75f, 3.75f, 6.25f, 3.25f), Direction.UP, new UVData(10.25f, 3.25f, 10.75f, 3.75f), Direction.SOUTH, new UVData(8.75f, 3.75f, 8.25f, 3.25f), Direction.DOWN, new UVData(11.25f, 3.25f, 11.75f, 3.75f), Direction.EAST, new UVData(7.75f, 1.75f, 5.25f, 1.25f)))
                .addBox(4.95f, 0f, 5.94f, 0.99f, 31.68f, 3.96f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(12.75f, 3.75f, 12.25f, 3.25f), Direction.UP, new UVData(0.25f, 4.25f, 0.75f, 4.75f), Direction.SOUTH, new UVData(14.75f, 3.75f, 14.25f, 3.25f), Direction.DOWN, new UVData(1.25f, 4.25f, 1.75f, 4.75f), Direction.EAST, new UVData(13.75f, 3.75f, 13.25f, 3.25f)))
                .addBox(1.98f, 0f, -1.98f, 0.99f, 31.68f, 19.8f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(2.75f, 4.75f, 2.25f, 4.25f), Direction.UP, new UVData(6.25f, 4.25f, 6.75f, 4.75f), Direction.SOUTH, new UVData(4.75f, 4.75f, 4.25f, 4.25f), Direction.DOWN, new UVData(7.25f, 4.25f, 7.75f, 4.75f), Direction.EAST, new UVData(8.45f, 6.75f, 5.55f, 6.25f)))
                .addBox(-9.9f, 0f, -5.94f, 3.96f, 31.68f, 0.99f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(8.75f, 4.75f, 8.25f, 4.25f), Direction.UP, new UVData(12.25f, 4.25f, 12.75f, 4.75f), Direction.WEST, new UVData(11.75f, 4.75f, 11.25f, 4.25f), Direction.DOWN, new UVData(13.25f, 4.25f, 13.75f, 4.75f), Direction.EAST, new UVData(9.75f, 4.75f, 9.25f, 4.25f)))
                .addBox(-12.87f, 0f, -4.95f, 9.9f, 31.68f, 0.99f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(7.75f, 1.75f, 5.25f, 1.25f), Direction.UP, new UVData(2.25f, 5.25f, 2.75f, 5.75f), Direction.WEST, new UVData(1.75f, 5.75f, 1.25f, 5.25f), Direction.DOWN, new UVData(3.25f, 5.25f, 3.75f, 5.75f), Direction.EAST, new UVData(15.75f, 4.75f, 15.25f, 4.25f)))
                .addBox(-14.85f, 0f, -3.96f, 13.86f, 31.68f, 0.99f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(8.5f, 6.75f, 5.5f, 6.25f), Direction.UP, new UVData(8.25f, 5.25f, 8.75f, 5.75f), Direction.WEST, new UVData(7.75f, 5.75f, 7.25f, 5.25f), Direction.DOWN, new UVData(9.25f, 5.25f, 9.75f, 5.75f), Direction.EAST, new UVData(5.75f, 5.75f, 5.25f, 5.25f)))
                .addBox(-17.82f, 0f, -2.97f, 19.8f, 31.68f, 0.99f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(8.45f, 6.75f, 5.55f, 6.25f), Direction.UP, new UVData(14.25f, 5.25f, 14.75f, 5.75f), Direction.WEST, new UVData(13.75f, 5.75f, 13.25f, 5.25f), Direction.DOWN, new UVData(15.25f, 5.25f, 15.75f, 5.75f), Direction.EAST, new UVData(11.75f, 5.75f, 11.25f, 5.25f)))
                .addBox(-21.78f, 0f, 5.94f, 0.99f, 31.68f, 3.96f, new Vector3f(), ImmutableMap.of(Direction.NORTH, new UVData(0.75f, 6.75f, 0.25f, 6.25f), Direction.UP, new UVData(4.25f, 6.25f, 4.75f, 6.75f), Direction.SOUTH, new UVData(2.75f, 6.75f, 2.25f, 6.25f), Direction.WEST, new UVData(3.75f, 6.75f, 3.25f, 6.25f), Direction.DOWN, new UVData(5.25f, 6.25f, 5.75f, 6.75f))), PartPose.offset(0f, 0f, 0f));

        return ModelDefinition.create(meshDefinition, 16, 16);
    }

    @Override
    public void render(@NotNull CryoChamberEntity chamber, float pPartialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int pPackedLight, int pPackedOverlay) {
        if(chamber.isOpen() || chamber.getFluidAmount() == 0) return;
        fluid.resetPose();
        float scale = chamber.getFluidAmount() * .03125f;
        fluid.yScale = scale;
        fluid.y -= scale / 2;
        setupFluid(chamber.getDirection());
        poseStack.pushPose();
        poseStack.translate(.5,.125,.5);
        fluid.render(poseStack, buffer.getBuffer(RenderType.entityTranslucent(TEXTURE)), pPackedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private void setupFluid(@NotNull Direction direction){
        switch(direction){
            case WEST -> fluid.x += 16;
            case SOUTH -> {
                fluid.x += 16;
                fluid.z -= 16;
            }
            case EAST -> fluid.z -=16;
        }
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull CryoChamberEntity chamber) {
        return chamber.getInsideAABB();
    }
}