package net.zaharenko424.a_changed.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.LocalPlayerExtension;
import net.zaharenko424.a_changed.attachment.LatexCoveredData;
import net.zaharenko424.a_changed.client.screen.KeypadScreen;
import net.zaharenko424.a_changed.client.screen.NoteScreen;
import net.zaharenko424.a_changed.client.screen.TransfurScreen;
import net.zaharenko424.a_changed.network.packets.ClientboundLTCDataPacket;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenKeypadPacket;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenNotePacket;
import net.zaharenko424.a_changed.network.packets.ClientboundSmoothLookPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurToleranceSyncPacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ClientPacketHandler {

    public static final ClientPacketHandler INSTANCE = new ClientPacketHandler();

    private final Minecraft minecraft = Minecraft.getInstance();

    public void handleSmoothLookPacket(ClientboundSmoothLookPacket packet, IPayloadContext context){
        context.enqueueWork(()->
                ((LocalPlayerExtension)minecraft.player).achanged$lerpLookAt(packet.xRot(), packet.yRot(), packet.speed(), packet.ticks()));
    }

    public void handleTransfurToleranceSync(@NotNull ClientboundTransfurToleranceSyncPacket packet){
        TransfurManager.TRANSFUR_TOLERANCE = packet.transfurTolerance();
    }

    public void handleLTCDataSync(ClientboundLTCDataPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            LevelChunk chunk = minecraft.level.getChunk(packet.pos().x, packet.pos().z);
            LatexCoveredData.of(chunk).readPacket(packet.flags(), packet.buffer());
        });
    }

    @ApiStatus.Internal
    public void updateChunkSections(Set<SectionPos> sections){
        LevelRenderer levelRenderer = minecraft.levelRenderer;
        for(SectionPos pos : sections){
            levelRenderer.setSectionDirty(pos.x(), pos.y(), pos.z());
        }
    }

    public void handleOpenTransfurScreen(@NotNull IPayloadContext context){
        context.enqueueWork(() ->
                minecraft.setScreen(new TransfurScreen()));
    }

    public void handleOpenNotePacket(@NotNull ClientboundOpenNotePacket packet, @NotNull IPayloadContext context){
        context.enqueueWork(() ->
                minecraft.setScreen(new NoteScreen(packet.pos(), packet.text(), packet.finalized(), packet.guiId())));
    }

    public void handleOpenKeypadPacket(@NotNull ClientboundOpenKeypadPacket packet, @NotNull IPayloadContext context){
        context.enqueueWork(() ->
                minecraft.setScreen(new KeypadScreen(packet.isPasswordSet(), packet.length(), packet.pos())));
    }
}