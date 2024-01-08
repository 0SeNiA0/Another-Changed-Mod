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
import net.minecraft.world.phys.AABB;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.ModelCache;
import net.zaharenko424.a_changed.client.model.geom.*;
import net.zaharenko424.a_changed.entity.block.LaserEmitterEntity;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LaserEmitterRenderer implements BlockEntityRenderer<LaserEmitterEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BlockEntityRegistry.LASER_EMITTER_ENTITY.getId(), "beam");
    private static final ResourceLocation TEXTURE = AChanged.textureLoc("misc/beam");
    private final ModelPart beam;

    public LaserEmitterRenderer(){
        beam = ModelCache.INSTANCE.bake(LAYER).getChild("beam");
    }

    public static @NotNull ModelDefinition bodyLayer(){
        MeshDefinition meshDefinition = new MeshDefinition();
        GroupDefinition groupDefinition = meshDefinition.getRoot();
        GroupDefinition beam = groupDefinition.addOrReplaceChild("beam", GroupBuilder.create(), PartPose.offset(0f, 8f, 0f));
        beam.addOrReplaceChild("cube_i0", GroupBuilder.create()
                .addBox(-1f, -1f, -8f, 2f, 2f, 16f, new Vector3f(), ImmutableMap.of(Direction.SOUTH, new UVData(6f, 8f, 4f, 6f), Direction.WEST, new UVData(16f, 4f, 0f, 2f), Direction.NORTH, new UVData(6f, 6f, 4f, 4f), Direction.EAST, new UVData(16f, 2f, 0f, 0f), Direction.UP, new UVData(2f, 20f, 0f, 4f), Direction.DOWN, new UVData(4f, 4f, 2f, 20f))), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0.7854f));
        return ModelDefinition.create(meshDefinition, 32, 32);
    }

    @Override
    public boolean shouldRenderOffScreen(LaserEmitterEntity p_112306_) {
        return p_112306_.isActive();
    }

    @Override
    public void render(LaserEmitterEntity emitter, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if(!emitter.isActive()) return;
        poseStack.pushPose();
        poseStack.translate(.5,0,.5);
        beam.resetPose();
        setupBeam(emitter.getDirection(), emitter.getLaserLength());
        beam.render(poseStack, buffer.getBuffer(RenderType.beaconBeam(TEXTURE, false)), light, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    protected void setupBeam(Direction direction, int length){
        beam.zScale = length;
        float offset = (.5f + (float) length /2) * 16;
        switch (direction){
            case NORTH -> beam.z -= offset;
            case SOUTH -> beam.z += offset;
            case WEST -> {
                beam.x -= offset;
                beam.yRot = Mth.PI/2;
            }
            case EAST -> {
                beam.x += offset;
                beam.yRot = -Mth.PI/2;
            }
            case UP -> {
                beam.y += offset;
                beam.xRot = -Mth.PI/2;
            }
            case DOWN -> {
                beam.y -= offset;
                beam.xRot = Mth.PI/2;
            }
        }
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(LaserEmitterEntity emitter) {
        return emitter.getLaserAABB();
    }
}