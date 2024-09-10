package net.zaharenko424.a_changed.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.ChunkPos;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ClientboundLTCDataPacket(ChunkPos pos, byte flags, byte[] rawData) implements CustomPacketPayload {

    public static final Type<ClientboundLTCDataPacket> TYPE = new Type<>(AChanged.resourceLoc("ltc_data_sync"));

    public ClientboundLTCDataPacket(FriendlyByteBuf buf){
        this(new ChunkPos(buf.readVarInt(), buf.readVarInt()), buf.readByte(), buf.readByteArray());
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundLTCDataPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.pos.x);
        buf.writeVarInt(packet.pos.z);
        buf.writeByte(packet.flags);
        buf.writeByteArray(packet.rawData);
    }, ClientboundLTCDataPacket::new);

    @Override
    public @NotNull Type<ClientboundLTCDataPacket> type() {
        return TYPE;
    }
}