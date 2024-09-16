package net.zaharenko424.a_changed.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.entity.block.KeypadEntity;
import net.zaharenko424.a_changed.entity.block.NoteEntity;
import net.zaharenko424.a_changed.entity.block.machines.LatexEncoderEntity;
import net.zaharenko424.a_changed.network.packets.ServerboundEditNotePacket;
import net.zaharenko424.a_changed.network.packets.ServerboundLatexEncoderScreenPacket;
import net.zaharenko424.a_changed.network.packets.ServerboundTryPasswordPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundAbilityPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundActivateAbilityPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundDeactivateAbilityPacket;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundSelectAbilityPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ServerboundTransfurChoicePacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

import static net.zaharenko424.a_changed.AChanged.LOGGER;

public class ServerPacketHandler {

    public static final ServerPacketHandler INSTANCE = new ServerPacketHandler();
    private static final String PLAYER_NOT_FOUND = "Received a packet from player which is not on the server!";

    private boolean playerEmpty(boolean empty){
        if(empty) LOGGER.warn(PLAYER_NOT_FOUND);
        return empty;
    }

    public void handleActivateAbilityPacket(ServerboundActivateAbilityPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            TransfurHandler handler = TransfurHandler.nonNullOf(player);

            if(!handler.isTransfurred()) return;
            Ability selected = handler.getSelectedAbility();
            if(selected == null) return;

            selected.activate(player, packet.oneShot(), packet.additionalData());
        });
    }

    public void handleDeactivateAbilityPacket(ServerboundDeactivateAbilityPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            TransfurHandler handler = TransfurHandler.nonNullOf(player);

            if(!handler.isTransfurred()) return;
            Ability selected = handler.getSelectedAbility();
            if(selected == null) return;

            selected.deactivate(player);
        });
    }

    public void handleSelectAbilityPacket(ServerboundSelectAbilityPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            TransfurHandler handler = TransfurHandler.nonNullOf(player);

            if(!handler.isTransfurred()) return;
            handler.selectAbility(packet.ability());
        });
    }

    public void handleAbilityPacket(ServerboundAbilityPacket packet, IPayloadContext context){
        context.enqueueWork(() ->
                packet.ability().handleData(context.player(), packet.buffer(), context));
    }

    public void handleTransfurChoicePacket(ServerboundTransfurChoicePacket packet, @NotNull IPayloadContext context){
        ServerPlayer player = (ServerPlayer) context.player();
        TransfurHandler handler = TransfurHandler.nonNullOf(player);
        if(!handler.isBeingTransfurred()) return;

        TransfurType transfurType = handler.getTransfurType();
        if(packet.becomeTransfur()) handler.transfur(transfurType, TransfurContext.TRANSFUR_TF);
        else handler.transfur(transfurType, TransfurContext.TRANSFUR_DEATH);
    }

    public void handleLatexEncoderScreenPacket(@NotNull ServerboundLatexEncoderScreenPacket packet, IPayloadContext context){
        blockEntityInteract(context, packet.pos(), LatexEncoderEntity.class, (player, encoder) ->
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
        if(sender.distanceToSqr(pos.getCenter()) > 64) {
            LOGGER.warn("Player {} tried to interact with {} from more than 8 blocks away!", sender, clazz);
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