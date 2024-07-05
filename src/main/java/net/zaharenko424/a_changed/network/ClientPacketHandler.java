package net.zaharenko424.a_changed.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.IGrabHandler;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.client.cmrs.CustomModelManager;
import net.zaharenko424.a_changed.client.cmrs.model.URLLoadedModel;
import net.zaharenko424.a_changed.client.screen.KeypadScreen;
import net.zaharenko424.a_changed.client.screen.NoteScreen;
import net.zaharenko424.a_changed.client.screen.TransfurScreen;
import net.zaharenko424.a_changed.network.packets.ClientboundLTCDataPacket;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenKeypadPacket;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenNotePacket;
import net.zaharenko424.a_changed.network.packets.grab.ClientboundGrabSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurToleranceSyncPacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.Special;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ClientPacketHandler {

    public static final ClientPacketHandler INSTANCE = new ClientPacketHandler();

    private final Minecraft minecraft = Minecraft.getInstance();

    public void handleTransfurToleranceSync(@NotNull ClientboundTransfurToleranceSyncPacket packet){
        TransfurManager.TRANSFUR_TOLERANCE = packet.transfurTolerance();
    }

    public void handleGrabSyncPacket(@NotNull ClientboundGrabSyncPacket packet, @NotNull PlayPayloadContext context){
        context.workHandler().submitAsync(() -> {
            Entity entity = minecraft.level.getEntity(packet.holderId());
            if(!(entity instanceof LivingEntity holder)) {
                AChanged.LOGGER.warn("No suitable holder for GrabCapability found with id {}!", packet.holderId());
                return;
            }
            IGrabHandler handler = GrabCapability.nonNullOf(holder);

            int targetId = packet.targetId();
            int grabbedBy = packet.grabbedBy();
            Level level = minecraft.level;

            if(targetId == -1) {
                if(handler.getTarget() != null) handler.drop();
            } else {
                handler.grab((LivingEntity) level.getEntity(targetId));
            }

            if(grabbedBy == -1) {
                if(handler.getGrabbedBy() != null) handler.setGrabbedBy(null);
            } else {
                handler.setGrabbedBy((Player) level.getEntity(grabbedBy));
            }

            handler.setGrabMode(packet.mode());
            handler.setWantsToBeGrabbed(packet.wantsToBeGrabbed());
        });
    }

    public void handleRemotePlayerTransfurSync(@NotNull ClientboundTransfurSyncPacket packet, @NotNull PlayPayloadContext context){
        context.workHandler().submitAsync(() -> {
            Entity entity = minecraft.level.getEntity(packet.holderId());
            if(!(entity instanceof LivingEntity holder)) {
                AChanged.LOGGER.warn("No suitable holder for TransfurCapability found with id {}!", packet.holderId());
                return;
            }

            ITransfurHandler handler = TransfurCapability.nonNullOf(holder);

            if(packet.transfurTypeO() != null) removeTransfurModel((AbstractClientPlayer) holder, packet.transfurTypeO());
            if(packet.isTransfurred()){
                setTransfurModel((AbstractClientPlayer) holder, packet.transfurType());
            }

            handler.loadSyncedData(packet.transfurProgress(), packet.isTransfurred(), packet.transfurType());
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
        context.workHandler().submitAsync(() -> {
            LevelChunk chunk = minecraft.level.getChunk(packet.pos().x, packet.pos().z);
            LatexCoveredData.of(chunk).readPacket(packet.flags(), packet.rawData());
        });
    }

    public void updateChunkSections(Set<SectionPos> sections){
        LevelRenderer levelRenderer = minecraft.levelRenderer;
        for(SectionPos pos : sections){
            levelRenderer.setSectionDirty(pos.x(), pos.y(), pos.z());
        }
    }

    public void handleOpenTransfurScreen(@NotNull PlayPayloadContext context){
        context.workHandler().submitAsync(() ->
                minecraft.setScreen(new TransfurScreen()));
    }

    public void handleOpenNotePacket(@NotNull ClientboundOpenNotePacket packet, @NotNull PlayPayloadContext context){
        context.workHandler().submitAsync(() ->
                minecraft.setScreen(new NoteScreen(packet.pos(), packet.text(), packet.finalized(), packet.guiId())));
    }

    public void handleOpenKeypadPacket(@NotNull ClientboundOpenKeypadPacket packet, @NotNull PlayPayloadContext context){
        context.workHandler().submitAsync(() ->
                minecraft.setScreen(new KeypadScreen(packet.isPasswordSet(), packet.length(), packet.pos())));
    }
}