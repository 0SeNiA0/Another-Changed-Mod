package net.zaharenko424.a_changed.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.entity.block.KeypadEntity;
import net.zaharenko424.a_changed.entity.block.NoteEntity;
import net.zaharenko424.a_changed.entity.block.machine.ProcessingMachine;
import net.zaharenko424.a_changed.network.packets.ServerboundEditNotePacket;
import net.zaharenko424.a_changed.network.packets.ServerboundProcessingMachinePacket;
import net.zaharenko424.a_changed.network.packets.ServerboundTryPasswordPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ServerboundTransfurChoicePacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

import static net.zaharenko424.a_changed.AChanged.LOGGER;

public class ServerPacketHandler {

    public static final ServerPacketHandler INSTANCE = new ServerPacketHandler();

    public void handleTransfurChoicePacket(ServerboundTransfurChoicePacket packet, @NotNull IPayloadContext context){
        ServerPlayer player = (ServerPlayer) context.player();
        TransfurHandler handler = TransfurHandler.nonNullOf(player);
        if(!handler.isBeingTransfurred()) return;

        TransfurType<?> transfurType = handler.getTransfurType();
        if(packet.becomeTransfur()) handler.transfur(transfurType, TransfurContext.TRANSFUR);
        else handler.transfur(transfurType, TransfurContext.TRANSFUR_DEATH);
    }

    public void handleProcessingMachinePacket(@NotNull ServerboundProcessingMachinePacket packet, IPayloadContext context){
        blockEntityInteract(context, packet.pos(), ProcessingMachine.class, (player, encoder) ->
                encoder.setData(packet.index(), packet.data()));
    }

    public void handleEditNotePacket(@NotNull ServerboundEditNotePacket packet, @NotNull IPayloadContext context){
        blockEntityInteract(context, packet.pos(), NoteEntity.class, (player, noteEntity) ->
                noteEntity.setText(packet.text(), packet.finalize_()));
    }

    public void handleTryPasswordPacket(@NotNull ServerboundTryPasswordPacket packet, @NotNull IPayloadContext context){
        blockEntityInteract(context, packet.pos(), KeypadEntity.class, (player, keypad) -> {
            if(keypad.isCodeSet()){
                keypad.tryCode(packet.attempt());
            } else keypad.setCode(packet.attempt());
        });
    }

    private <E extends BlockEntity> void blockEntityInteract(@NotNull IPayloadContext context, BlockPos pos, Class<E> clazz, BiConsumer<ServerPlayer, E> task){
        ServerPlayer sender = (ServerPlayer) context.player();

        float interactionRange = (float) sender.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        if(sender.distanceToSqr(pos.getCenter()) > interactionRange * interactionRange) {
            LOGGER.warn("Player {} tried to interact with {} from too far away!", sender, clazz);
            return;
        }
        context.enqueueWork(()->{
            BlockEntity entity = sender.level().getBlockEntity(pos);
            if(entity == null || !clazz.isAssignableFrom(entity.getClass())){
                LOGGER.warn("Block position does not contain {}! ({})", clazz, pos);
                return;
            }
            task.accept(sender, (E) entity);
        });
    }
}