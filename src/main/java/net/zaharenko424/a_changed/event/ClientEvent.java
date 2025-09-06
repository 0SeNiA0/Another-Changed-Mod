package net.zaharenko424.a_changed.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.Tags;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.block.CryoChamber;
import net.zaharenko424.a_changed.block.FloorCircle;
import net.zaharenko424.a_changed.block.PileOfOranges;
import net.zaharenko424.a_changed.block.door.BigLabDoor;
import net.zaharenko424.a_changed.block.door.BigLibraryDoor;
import net.zaharenko424.a_changed.block.door.LabDoor;
import net.zaharenko424.a_changed.block.door.LibraryDoor;
import net.zaharenko424.a_changed.block.machine.DNAExtractor;
import net.zaharenko424.a_changed.block.machine.LatexEncoder;
import net.zaharenko424.a_changed.block.smalldecor.BrokenFlask;
import net.zaharenko424.a_changed.block.smalldecor.Flask;
import net.zaharenko424.a_changed.block.smalldecor.MetalCan;
import net.zaharenko424.a_changed.block.smalldecor.TestTubes;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.Utils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = AChanged.MODID, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.GatherComponents event){
        ItemStack item = event.getItemStack();
        if(item.is(Tags.Items.DYES_LIME) || item.is(Tags.Items.DYES_RED)){
            event.getTooltipElements().add(Either.left(Component.translatable("tooltip.a_changed.floor_circle").withStyle(ChatFormatting.GRAY)));
            return;
        }

        if(item.is(Tags.Items.DYES_BLACK)){
            event.getTooltipElements().add(Either.left(Component.translatable("tooltip.a_changed.paper_stack.write").withStyle(ChatFormatting.GRAY)));
            return;
        }

        if(item.is(Tags.Items.DYES_WHITE)){
            event.getTooltipElements().add(Either.left(Component.translatable("tooltip.a_changed.paper_stack.erase").withStyle(ChatFormatting.GRAY)));
            return;
        }

        if(item.is(Items.PAPER)){
            event.getTooltipElements().add(Either.left(Component.translatable("tooltip.a_changed.notes").withStyle(ChatFormatting.GRAY)));
            return;
        }

        if(item.is(ItemTags.BOOKSHELF_BOOKS)){
            event.getTooltipElements().add(Either.left(Component.translatable("tooltip.a_changed.books").withStyle(ChatFormatting.GRAY)));
            return;
        }

        if(item.is(ItemRegistry.COPPER_WIRE_ITEM.get())){
            event.getTooltipElements().add(Either.left(Component.translatable("tooltip.a_changed.wires", 256).withStyle(ChatFormatting.GRAY)));
        }
    }

    private static final List<Class<?>> blocksNoOutline = List.of(BrokenFlask.class, CryoChamber.class,
            Flask.class, FloorCircle.class, LatexEncoder.class, MetalCan.class, PileOfOranges.class, TestTubes.class);
    private static final List<Class<?>> blocksSolidOutline = List.of(BigLabDoor.class, BigLibraryDoor.class,
            DNAExtractor.class, LabDoor.class, LibraryDoor.class);

    @SubscribeEvent
    public static void onRenderBlockHighlight(RenderHighlightEvent.Block event){
        Level level = Minecraft.getInstance().level;
        BlockPos pos = event.getTarget().getBlockPos();
        Vec3 cameraPos = event.getCamera().getPosition();
        Class<? extends Block> clazz = level.getBlockState(pos).getBlock().getClass();

        if(Utils.containsClass(clazz, blocksNoOutline)){
            event.setCanceled(true);
            return;
        }

        if(!level.getWorldBorder().isWithinBounds(pos) || !Utils.containsClass(clazz, blocksSolidOutline)) return;

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

    public static void renderShape(PoseStack p_109783_, VertexConsumer consumer, VoxelShape p_109785_,
                                   double p_109786_, double p_109787_, double p_109788_,
                                   float r, float g, float b, float a) {
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
                    consumer.addVertex(posestack$pose.pose(), (float)(p_234280_ + p_109786_), (float)(p_234281_ + p_109787_), (float)(p_234282_ + p_109788_))
                            .setColor(r, g, b, a)
                            .setNormal(posestack$pose, f, f1, f2);
                    consumer.addVertex(posestack$pose.pose(), (float)(p_234283_ + p_109786_), (float)(p_234284_ + p_109787_), (float)(p_234285_ + p_109788_))
                            .setColor(r, g, b, a)
                            .setNormal(posestack$pose, f, f1, f2);
                }
        );
    }
}