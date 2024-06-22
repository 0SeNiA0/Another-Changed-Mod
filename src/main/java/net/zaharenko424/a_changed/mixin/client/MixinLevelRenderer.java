package net.zaharenko424.a_changed.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.event.ClientEvent;
import net.zaharenko424.a_changed.item.BuildersWand;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {

    /**
     * Renders bounds of selected builders wand area.
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/debug/DebugRenderer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;DDD)V", shift = At.Shift.BEFORE),
            method = "renderLevel")
    private void onRenderLevel(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci){
        Player player = Minecraft.getInstance().player;
        if(!player.getMainHandItem().is(ItemRegistry.BUILDERS_WAND)) return;
        ItemStack item = player.getMainHandItem();
        HitResult hitResult = Minecraft.getInstance().hitResult;


        if(item.getTagElement("data") == null) return;
        CompoundTag tag = item.getOrCreateTag();
        CompoundTag data = tag.getCompound("data");
        BuildersWand.Mode mode = BuildersWand.Mode.values()[tag.getByte("mode")];
        boolean destroy = mode == BuildersWand.Mode.DESTROY || mode == BuildersWand.Mode.REPLACE;

        BlockPos from = NBTUtils.getBlockPos(data);
        BlockPos to;
        if(destroy && hitResult.getType() == HitResult.Type.BLOCK){
            to = ((BlockHitResult) hitResult).getBlockPos();
        } else {
            Vec3 vec = player.getLookAngle().multiply(2, 0, 2).add(player.position());
            to = new BlockPos.MutableBlockPos(vec.x, vec.y, vec.z).immutable();
        }

        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();

        VoxelShape shape = Shapes.create(AABB.encapsulatingFullBlocks(from, to).move(-from.getX(), -from.getY(), -from.getZ()));
        Vec3 cameraPos = pCamera.getPosition();

        ClientEvent.renderShape(pPoseStack, source.getBuffer(RenderType.LINES), shape,
                (double)from.getX() - cameraPos.x,
                (double)from.getY() - cameraPos.y,
                (double)from.getZ() - cameraPos.z,
                destroy ? .5f : .1f,
                destroy ? .1f : .5f,
                .1f,
                1F
                );
    }
}