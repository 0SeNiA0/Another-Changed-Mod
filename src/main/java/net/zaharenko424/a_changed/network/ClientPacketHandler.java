package net.zaharenko424.a_changed.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.LocalPlayerExtension;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.client.cmrs.CustomModelManager;
import net.zaharenko424.a_changed.client.cmrs.model.URLLoadedModel;
import net.zaharenko424.a_changed.client.screen.KeypadScreen;
import net.zaharenko424.a_changed.client.screen.NoteScreen;
import net.zaharenko424.a_changed.client.screen.TransfurScreen;
import net.zaharenko424.a_changed.network.packets.ClientboundLTCDataPacket;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenKeypadPacket;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenNotePacket;
import net.zaharenko424.a_changed.network.packets.ClientboundSmoothLookPacket;
import net.zaharenko424.a_changed.network.packets.ability.ClientboundAbilitySyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurToleranceSyncPacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.Special;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ClientPacketHandler {

    public static final ClientPacketHandler INSTANCE = new ClientPacketHandler();

    private final Minecraft minecraft = Minecraft.getInstance();

    public void handleSmoothLookPacket(ClientboundSmoothLookPacket packet, PlayPayloadContext context){
        context.workHandler().execute(()->
                ((LocalPlayerExtension)minecraft.player).mod$lerpLookAt(packet.xRot(), packet.yRot(), packet.speed(), packet.ticks()));
    }

    public void handleTransfurToleranceSync(@NotNull ClientboundTransfurToleranceSyncPacket packet){
        TransfurManager.TRANSFUR_TOLERANCE = packet.transfurTolerance();
    }

    public void handleAbilitySyncPacket(@NotNull ClientboundAbilitySyncPacket packet, @NotNull PlayPayloadContext context){
        context.workHandler().execute(()-> {
            Entity entity = minecraft.level.getEntity(packet.holderId());
            if(!(entity instanceof LivingEntity living)) return;
            packet.ability().handleData(living, packet.buffer(), context);
        });
    }

    public void handleTransfurSyncPacket(@NotNull ClientboundTransfurSyncPacket packet, @NotNull PlayPayloadContext context){
        context.workHandler().execute(() -> {
            Entity entity = minecraft.level.getEntity(packet.holderId());
            if(!(entity instanceof LivingEntity holder)) {
                AChanged.LOGGER.warn("No suitable holder for TransfurCapability found with id {}!", packet.holderId());
                return;
            }

            TransfurHandler handler = TransfurHandler.nonNullOf(holder);

            if(packet.transfurTypeO() != null) removeTransfurModel((AbstractClientPlayer) holder, packet.transfurTypeO());
            if(packet.isTransfurred()){
                setTransfurModel((AbstractClientPlayer) holder, packet.transfurType());
            }

            handler.loadSyncedData(packet.ability(), packet.transfurProgress(), packet.isTransfurred(), packet.transfurType());
            holder.refreshDimensions();
        });
    }

    private void removeTransfurModel(AbstractClientPlayer player, TransfurType transfurType){
        if(transfurType instanceof Special) CustomModelManager.getInstance().removePlayerModel(player, player.getStringUUID());
        else CustomModelManager.getInstance().removePlayerModel(player, transfurType.id);
    }

    private void setTransfurModel(AbstractClientPlayer player, TransfurType transfurType){
        if(transfurType instanceof Special) CustomModelManager.getInstance().setPlayerModel(player, player.getStringUUID(), URLLoadedModel::new, 1);
        else CustomModelManager.getInstance().setPlayerModel(player, transfurType.id, transfurType::getModel, 1);
    }

    public void handleLTCDataSync(ClientboundLTCDataPacket packet, PlayPayloadContext context){
        context.workHandler().execute(() -> {
            LevelChunk chunk = minecraft.level.getChunk(packet.pos().x, packet.pos().z);
            LatexCoveredData.of(chunk).readPacket(packet.flags(), packet.rawData());
        });
    }

    @ApiStatus.Internal
    public void updateChunkSections(Set<SectionPos> sections){
        LevelRenderer levelRenderer = minecraft.levelRenderer;
        for(SectionPos pos : sections){
            levelRenderer.setSectionDirty(pos.x(), pos.y(), pos.z());
        }
    }

    public void handleOpenTransfurScreen(@NotNull PlayPayloadContext context){
        context.workHandler().execute(() ->
                minecraft.setScreen(new TransfurScreen()));
    }

    public void handleOpenNotePacket(@NotNull ClientboundOpenNotePacket packet, @NotNull PlayPayloadContext context){
        context.workHandler().execute(() ->
                minecraft.setScreen(new NoteScreen(packet.pos(), packet.text(), packet.finalized(), packet.guiId())));
    }

    public void handleOpenKeypadPacket(@NotNull ClientboundOpenKeypadPacket packet, @NotNull PlayPayloadContext context){
        context.workHandler().execute(() ->
                minecraft.setScreen(new KeypadScreen(packet.isPasswordSet(), packet.length(), packet.pos())));
    }
}