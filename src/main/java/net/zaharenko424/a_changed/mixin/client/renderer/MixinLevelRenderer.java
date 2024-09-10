package net.zaharenko424.a_changed.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.event.ClientEvent;
import net.zaharenko424.a_changed.item.BuildersWand;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {

    @Shadow
    @Nullable
    private ClientLevel level;

    /**
     * Replace block break sound if block is latex covered.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/block/SoundType;"),
            method = "levelEvent")
    private SoundType onBlockBreakEvent(SoundType original, @Local BlockState state, @Local(argsOnly = true) BlockPos pos){
        if(LatexCoveredData.of(level.getChunkAt(pos)).getCoveredWith(pos) == CoveredWith.NOTHING) return original;
        return BlockRegistry.DARK_LATEX_BLOCK.get().getSoundType(state, level, pos, null);
    }

    /**
     * Renders bounds of selected builders wand area.
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/debug/DebugRenderer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;DDD)V", shift = At.Shift.BEFORE),
            method = "renderLevel")
    private void onRenderLevel(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci, @Local PoseStack poseStack){
        Player player = Minecraft.getInstance().player;
        if(!player.getMainHandItem().is(ItemRegistry.BUILDERS_WAND)) return;
        ItemStack item = player.getMainHandItem();
        HitResult hitResult = Minecraft.getInstance().hitResult;

        BuildersWand.Data data = item.getOrDefault(ComponentRegistry.BUILDERS_WAND_DATA, BuildersWand.Data.DEF);
        if(data.from() == null) return;

        BuildersWand.Mode mode = data.mode();
        boolean destroyReplace = mode == BuildersWand.Mode.DESTROY || mode == BuildersWand.Mode.REPLACE;

        BlockPos from = data.from();
        BlockPos to;
        if(destroyReplace && hitResult.getType() == HitResult.Type.BLOCK){
            to = ((BlockHitResult) hitResult).getBlockPos();
        } else {
            Vec3 vec = player.getLookAngle().multiply(2, 0, 2).add(player.position());
            to = new BlockPos.MutableBlockPos(vec.x, vec.y, vec.z).immutable();
        }

        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();

        VoxelShape shape = Shapes.create(AABB.encapsulatingFullBlocks(from, to).move(-from.getX(), -from.getY(), -from.getZ()));
        Vec3 cameraPos = camera.getPosition();

        ClientEvent.renderShape(poseStack, source.getBuffer(RenderType.LINES), shape,
                (double)from.getX() - cameraPos.x,
                (double)from.getY() - cameraPos.y,
                (double)from.getZ() - cameraPos.z,
                destroyReplace ? .5f : .1f,
                destroyReplace ? .1f : .5f,
                .1f,
                1F
                );
    }
}