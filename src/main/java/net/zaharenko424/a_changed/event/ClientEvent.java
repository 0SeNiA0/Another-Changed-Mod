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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.block.blocks.BrokenFlask;
import net.zaharenko424.a_changed.block.blocks.CryoChamber;
import net.zaharenko424.a_changed.block.blocks.Flask;
import net.zaharenko424.a_changed.block.blocks.TestTubes;
import net.zaharenko424.a_changed.block.doors.BigLabDoor;
import net.zaharenko424.a_changed.block.doors.BigLibraryDoor;
import net.zaharenko424.a_changed.block.doors.LabDoor;
import net.zaharenko424.a_changed.block.doors.LibraryDoor;
import net.zaharenko424.a_changed.block.machines.DNAExtractor;
import net.zaharenko424.a_changed.block.machines.LatexEncoder;
import net.zaharenko424.a_changed.capability.GrabMode;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.model.ModelCache;
import net.zaharenko424.a_changed.client.screen.GrabModeSelectionScreen;
import net.zaharenko424.a_changed.client.screen.WantToBeGrabbedScreen;
import net.zaharenko424.a_changed.network.PacketHandler;
import net.zaharenko424.a_changed.network.packets.grab.ServerboundGrabPacket;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AChanged.MODID, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event){
        event.addListener(ModelCache.INSTANCE);
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event){
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if(Keybindings.GRAB_KEY.consumeClick() && TransfurManager.isTransfurred(player)) {
            grabLogic(minecraft, player);
            return;
        }

        if(Keybindings.GRAB_MODE_KEY.consumeClick() && minecraft.screen == null){
            if(TransfurManager.isTransfurred(player)) {
                if(!TransfurManager.isOrganic(player)) minecraft.setScreen(new GrabModeSelectionScreen());
            } else minecraft.setScreen(new WantToBeGrabbedScreen());
        }
    }

    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.GatherComponents event){
        ItemStack item = event.getItemStack();
        if(item.is(Items.PAPER)){
            event.getTooltipElements().add(Either.left(Component.translatable("tooltip.a_changed.notes").withStyle(ChatFormatting.GRAY)));
            return;
        }

        if(item.is(ItemTags.BOOKSHELF_BOOKS)){
            event.getTooltipElements().add(Either.left(Component.translatable("tooltip.a_changed.books").withStyle(ChatFormatting.GRAY)));
        }
    }

    private static void grabLogic(Minecraft minecraft, Player player){
        if(TransfurManager.isHoldingEntity(player)){
            if(player.isCrouching()){
                PacketHandler.INSTANCE.sendToServer(new ServerboundGrabPacket(-10));
                return;
            }
            return;
        }

        if(TransfurManager.isGrabbed(player)) {
            player.displayClientMessage(Component.translatable("message.a_changed.grabbed"),true);
            return;
        }

        if(!(minecraft.crosshairPickEntity instanceof LivingEntity entity)
                || !entity.getType().is(AChanged.TRANSFURRABLE_TAG) || player.distanceTo(entity) > 2.5) return;

        if(player.hasEffect(MobEffectRegistry.GRAB_COOLDOWN.get())){
            player.displayClientMessage(Component.translatable("message.a_changed.grab_cooldown",
                    String.valueOf((float) player.getEffect(MobEffectRegistry.GRAB_COOLDOWN.get()).getDuration() / 20)), true);
            return;
        }

        GrabMode mode = TransfurManager.getGrabMode(player);
        if(!mode.checkTarget(entity)) {
            player.displayClientMessage(Component.translatable("message.a_changed.only_friendly_grab_players"), true);
            return;
        }
        if(entity instanceof Player player1){
            if(TransfurManager.isBeingTransfurred(player1) || TransfurManager.isTransfurred(player1)) return;
            if(TransfurManager.isGrabbed(player1)){
                player.displayClientMessage(Component.translatable("message.a_changed.player_already_grabbed"), true);
                return;
            }
            if(!mode.givesDebuffToTarget && !TransfurManager.wantsToBeGrabbed(player1)){
                player.displayClientMessage(Component.translatable("message.a_changed.player_doesnt_want_to_be_grabbed"), true);
                return;
            }
        }
        PacketHandler.INSTANCE.sendToServer(new ServerboundGrabPacket(entity.getId()));
    }

    private static final List<Class<? extends Block>> blocksNoOutline = List.of(BrokenFlask.class, CryoChamber.class,
            Flask.class, LatexEncoder.class, TestTubes.class);
    private static final List<Class<? extends Block>> blocksSolidOutline = List.of(BigLabDoor.class, BigLibraryDoor.class,
            DNAExtractor.class, LabDoor.class, LibraryDoor.class);

    @SubscribeEvent
    public static void onRenderBlockHighlight(RenderHighlightEvent.Block event){
        Level level = Minecraft.getInstance().level;
        BlockPos pos = event.getTarget().getBlockPos();
        Vec3 cameraPos = event.getCamera().getPosition();
        Class<? extends Block> clazz = level.getBlockState(pos).getBlock().getClass();

        if(containsClass(clazz, blocksNoOutline)){
            event.setCanceled(true);
            return;
        }

        if(!level.getWorldBorder().isWithinBounds(pos) || !containsClass(clazz, blocksSolidOutline)) return;

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

    private static boolean containsClass(Class<? extends Block> clazz, List<Class<? extends Block>> list){
        for (Class<? extends Block> block : list){
            if(block.isAssignableFrom(clazz)) return true;
        }
        return false;
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