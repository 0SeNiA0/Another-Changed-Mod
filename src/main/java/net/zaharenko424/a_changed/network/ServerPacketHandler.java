package net.zaharenko424.a_changed.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.IGrabHandler;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.entity.block.KeypadEntity;
import net.zaharenko424.a_changed.entity.block.NoteEntity;
import net.zaharenko424.a_changed.entity.block.machines.LatexEncoderEntity;
import net.zaharenko424.a_changed.network.packets.ServerboundEditNotePacket;
import net.zaharenko424.a_changed.network.packets.ServerboundLatexEncoderScreenPacket;
import net.zaharenko424.a_changed.network.packets.ServerboundTryPasswordPacket;
import net.zaharenko424.a_changed.network.packets.grab.ServerboundGrabModePacket;
import net.zaharenko424.a_changed.network.packets.grab.ServerboundGrabPacket;
import net.zaharenko424.a_changed.network.packets.grab.ServerboundWantToBeGrabbedPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ServerboundTransfurChoicePacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

import static net.zaharenko424.a_changed.AChanged.LOGGER;

public class ServerPacketHandler {

    public static final ServerPacketHandler INSTANCE = new ServerPacketHandler();

    public void handleGrabPacket(ServerboundGrabPacket packet, @NotNull PlayPayloadContext context){
        if(context.player().isEmpty()) {
            LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        ServerPlayer player = (ServerPlayer) context.player().get();
        IGrabHandler handler = GrabCapability.nonNullOf(player);
        int targetId = packet.targetId();
        if(targetId == -10){
            handler.drop();
            return;
        }
        Entity entity0 = player.level().getEntity(targetId);
        if(!(entity0 instanceof LivingEntity entity) || entity.getTags().contains("a_changed:grabbed")//TODO Sync this to client to not spam packets?
                || player.distanceTo(entity) > 3 ) return;
        if(entity0 instanceof Player player1 && (TransfurManager.isGrabbed(player1) || (!TransfurManager.getGrabMode(player).givesDebuffToTarget && !TransfurManager.wantsToBeGrabbed(player1)))) return;
        handler.grab(entity);
    }

    public void handleGrabModePacket(ServerboundGrabModePacket packet, @NotNull PlayPayloadContext context){
        if(context.player().isEmpty()) {
            LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        ServerPlayer player = (ServerPlayer) context.player().get();
        IGrabHandler handler = GrabCapability.nonNullOf(player);
        if(handler.grabMode() == packet.mode()) return;
        handler.setGrabMode(packet.mode());
    }

    public void handleWantToBeGrabbedPacket(ServerboundWantToBeGrabbedPacket packet, @NotNull PlayPayloadContext context){
        if(context.player().isEmpty()) {
            LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        ServerPlayer player = (ServerPlayer) context.player().get();
        IGrabHandler handler = GrabCapability.nonNullOf(player);
        if(handler.wantsToBeGrabbed() != packet.wantsToBeGrabbed()) handler.setWantsToBeGrabbed(packet.wantsToBeGrabbed());
    }

    public void handleTransfurChoicePacket(ServerboundTransfurChoicePacket packet, @NotNull PlayPayloadContext context){
        if(context.player().isEmpty()) {
            LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        ServerPlayer player = (ServerPlayer) context.player().get();
        ITransfurHandler handler = TransfurCapability.nonNullOf(player);
        if(!handler.isBeingTransfurred()) return;

        TransfurType transfurType = handler.getTransfurType();
        if(packet.becomeTransfur()) handler.transfur(transfurType, TransfurContext.TRANSFUR_TF);
        else handler.transfur(transfurType, TransfurContext.TRANSFUR_DEATH);
    }

    public void handleLatexEncoderScreenPacket(@NotNull ServerboundLatexEncoderScreenPacket packet, PlayPayloadContext context){
        blockEntityInteract(context, packet.pos(), LatexEncoderEntity.class, (player, encoder) ->
                encoder.setData(packet.index(), packet.data()));
    }

    public void handleEditNotePacket(@NotNull ServerboundEditNotePacket packet, @NotNull PlayPayloadContext context){
        blockEntityInteract(context, packet.pos(), NoteEntity.class, (player, noteEntity) ->
                noteEntity.setText(packet.text(), packet.finalize_()));
    }

    public void handleTryPasswordPacket(@NotNull ServerboundTryPasswordPacket packet, @NotNull PlayPayloadContext context){
        blockEntityInteract(context, packet.pos(), KeypadEntity.class, (player, keypad) -> {
            if(keypad.isCodeSet()){
                keypad.tryCode(packet.attempt());
            } else keypad.setCode(packet.attempt());
        });
    }

    private <E extends BlockEntity> void blockEntityInteract(@NotNull PlayPayloadContext context, BlockPos pos, Class<E> clazz, BiConsumer<ServerPlayer, E> task){
        if(context.player().isEmpty()) {
            LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        ServerPlayer sender = (ServerPlayer) context.player().get();
        if(sender.distanceToSqr(pos.getCenter()) > 64) {
            LOGGER.warn("Player {} tried to interact with {} from more than 8 blocks away!", sender, clazz);
            return;
        }
        context.workHandler().submitAsync(()->{
            BlockEntity entity = sender.level().getBlockEntity(pos);
            if(entity == null || !clazz.isAssignableFrom(entity.getClass())){
                LOGGER.warn("Block position does not contain {}! ({})", clazz, pos);
                return;
            }
            task.accept(sender, (E) entity);
        });
    }
}