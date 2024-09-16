package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundTryPasswordPacket(int[] attempt, BlockPos pos) implements CustomPacketPayload {

    public static final Type<ServerboundTryPasswordPacket> TYPE = new Type<>(AChanged.resourceLoc("try_password"));

    public ServerboundTryPasswordPacket(@NotNull FriendlyByteBuf buffer){
        this(buffer.readVarIntArray(8), buffer.readBlockPos());
    }

    public static final StreamCodec<FriendlyByteBuf, ServerboundTryPasswordPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarIntArray(packet.attempt);
        buf.writeBlockPos(packet.pos);
    }, ServerboundTryPasswordPacket::new);

    @Override
    public @NotNull Type<ServerboundTryPasswordPacket> type() {
        return TYPE;
    }
}