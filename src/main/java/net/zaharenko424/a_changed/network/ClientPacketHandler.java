package net.zaharenko424.a_changed.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.IGrabHandler;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.client.screen.KeypadScreen;
import net.zaharenko424.a_changed.client.screen.NoteScreen;
import net.zaharenko424.a_changed.client.screen.TransfurScreen;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenKeypadPacket;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenNotePacket;
import net.zaharenko424.a_changed.network.packets.grab.ClientboundGrabSyncPacket;
import net.zaharenko424.a_changed.network.packets.grab.ClientboundRemoteGrabSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundPlayerTransfurSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundRemotePlayerTransfurSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurToleranceSyncPacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class ClientPacketHandler {

    public static final ClientPacketHandler INSTANCE = new ClientPacketHandler();

    public void handleTransfurToleranceSync(@NotNull ClientboundTransfurToleranceSyncPacket packet){
        TransfurManager.TRANSFUR_TOLERANCE = packet.transfurTolerance();
    }

    public void handleGrabSyncPacket(ClientboundGrabSyncPacket packet){
        Player player = Minecraft.getInstance().player;
        if(player == null) return;
        IGrabHandler handler = GrabCapability.nonNullOf(player);
        int targetId = packet.targetId();
        int grabbedBy = packet.grabbedBy();
        Level level = Minecraft.getInstance().level;
        if(targetId == -1) {
            if(handler.getTarget() != null) handler.drop();
        } else handler.grab((LivingEntity) level.getEntity(targetId));
        if(grabbedBy == -1) {
            if(handler.getGrabbedBy() != null) handler.setGrabbedBy(null);
        } else handler.setGrabbedBy((Player) level.getEntity(grabbedBy));
        handler.setGrabMode(packet.mode());
        handler.setWantsToBeGrabbed(packet.wantsToBeGrabbed());
    }

    public void handleRemoteGrabSyncPacket(@NotNull ClientboundRemoteGrabSyncPacket packet){
        Player player = Minecraft.getInstance().level.getPlayerByUUID(packet.playerId());
        if(player == null) return;
        IGrabHandler handler = GrabCapability.nonNullOf(player);
        int targetId = packet.targetId();
        int grabbedBy = packet.grabbedBy();
        Level level = Minecraft.getInstance().level;
        if(targetId == -1) {
            if(handler.getTarget() != null) handler.drop();
        } else handler.grab((LivingEntity) level.getEntity(targetId));
        if(grabbedBy == -1) {
            if(handler.getGrabbedBy() != null) handler.setGrabbedBy(null);
        } else handler.setGrabbedBy((Player) level.getEntity(grabbedBy));
        handler.setGrabMode(packet.mode());
        handler.setWantsToBeGrabbed(packet.wantsToBeGrabbed());
    }



    public void handlePlayerTransfurSync(ClientboundPlayerTransfurSyncPacket packet){
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        ITransfurHandler handler = TransfurCapability.nonNullOf(player);
        if(!packet.isTransfurred()){
            if(handler.isTransfurred()){
                handler.unTransfur();
                player.refreshDimensions();
            } else handler.setTransfurProgress(packet.transfurProgress(), packet.transfurType());
            return;
        }
        handler.transfur(packet.transfurType());
        player.refreshDimensions();
    }

    public void handleRemotePlayerTransfurSync(@NotNull ClientboundRemotePlayerTransfurSyncPacket packet){
        UUID playerId = packet.playerId();
        Player player = Objects.requireNonNull(Minecraft.getInstance().level).getPlayerByUUID(playerId);
        if(player == null){
            AChanged.LOGGER.warn("No player found with uuid "+playerId+"!");
            return;
        }
        ITransfurHandler handler = TransfurCapability.nonNullOf(player);
        if(!packet.isTransfurred()){
            if(handler.isTransfurred()){
                handler.unTransfur();
                player.refreshDimensions();
            } else handler.setTransfurProgress(packet.transfurProgress(), packet.transfurType());
            return;
        }
        handler.transfur(packet.transfurType());
        player.refreshDimensions();
    }

    public void handleOpenTransfurScreen(){
        Minecraft.getInstance().setScreen(new TransfurScreen());
    }

    public void handleOpenNotePacket(@NotNull ClientboundOpenNotePacket packet){
        Minecraft.getInstance().setScreen(new NoteScreen(packet.pos(), packet.text(), packet.finalized(), packet.guiId()));
    }

    public void handleOpenKeypadPacket(@NotNull ClientboundOpenKeypadPacket packet){
        Minecraft.getInstance().setScreen(new KeypadScreen(packet.isPasswordSet(), packet.length(), packet.pos()));
    }
}