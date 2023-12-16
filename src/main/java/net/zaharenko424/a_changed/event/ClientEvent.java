package net.zaharenko424.a_changed.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.block.doors.LibraryDoor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AChanged.MODID,value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onRenderBlockHighlight(RenderHighlightEvent.Block event){
        Level level= Minecraft.getInstance().level;
        BlockPos pos=event.getTarget().getBlockPos();
        Vec3 cameraPos=event.getCamera().getPosition();
        BlockState state=level.getBlockState(pos);
        if(!(state.getBlock() instanceof LibraryDoor)||!level.getWorldBorder().isWithinBounds(pos)) return;
        renderShape(
                event.getPoseStack(),
                event.getMultiBufferSource().getBuffer(RenderType.LINES),
                level.getBlockState(pos).getShape(level, pos, CollisionContext.of(event.getCamera().getEntity())),
                (double)pos.getX() - cameraPos.x,
                (double)pos.getY() - cameraPos.y,
                (double)pos.getZ() - cameraPos.z,
                0.0F,
                0.0F,
                0.0F,
                1F
        );
        event.setCanceled(true);
    }

    private static void renderShape(PoseStack p_109783_, VertexConsumer p_109784_, VoxelShape p_109785_,
            double p_109786_, double p_109787_, double p_109788_,
            float p_109789_, float p_109790_, float p_109791_, float p_109792_
    ) {
        PoseStack.Pose posestack$pose = p_109783_.last();
        p_109785_.forAllEdges(
                (p_234280_, p_234281_, p_234282_, p_234283_, p_234284_, p_234285_) -> {
                    float f = (float)(p_234283_ - p_234280_);
                    float f1 = (float)(p_234284_ - p_234281_);
                    float f2 = (float)(p_234285_ - p_234282_);
                    float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
                    f /= f3;
                    f1 /= f3;
                    f2 /= f3;
                    p_109784_.vertex(posestack$pose.pose(), (float)(p_234280_ + p_109786_), (float)(p_234281_ + p_109787_), (float)(p_234282_ + p_109788_))
                            .color(p_109789_, p_109790_, p_109791_, p_109792_)
                            .normal(posestack$pose.normal(), f, f1, f2)
                            .endVertex();
                    p_109784_.vertex(posestack$pose.pose(), (float)(p_234283_ + p_109786_), (float)(p_234284_ + p_109787_), (float)(p_234285_ + p_109788_))
                            .color(p_109789_, p_109790_, p_109791_, p_109792_)
                            .normal(posestack$pose.normal(), f, f1, f2)
                            .endVertex();
                }
        );
    }
}