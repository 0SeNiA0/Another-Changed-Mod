package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundProcessingMachinePacket(BlockPos pos, int index, int data) implements CustomPacketPayload {

    public static final Type<ServerboundProcessingMachinePacket> TYPE = new Type<>(AChanged.resourceLoc("serverbound_processing_machine"));

    public ServerboundProcessingMachinePacket(@NotNull FriendlyByteBuf buf){
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public static final StreamCodec<FriendlyByteBuf, ServerboundProcessingMachinePacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeBlockPos(packet.pos);
        buf.writeInt(packet.index);
        buf.writeInt(packet.data);
    }, ServerboundProcessingMachinePacket::new);

    @Override
    public @NotNull Type<ServerboundProcessingMachinePacket> type() {
        return TYPE;
    }
}