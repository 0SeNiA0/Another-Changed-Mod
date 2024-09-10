package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record ClientboundOpenKeypadPacket(boolean isPasswordSet, int length, BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClientboundOpenKeypadPacket> TYPE = new Type<>(AChanged.resourceLoc("open_keypad"));

    public ClientboundOpenKeypadPacket(FriendlyByteBuf buffer){
        this(buffer.readBoolean(), buffer.readInt(), buffer.readBlockPos());
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundOpenKeypadPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeBoolean(packet.isPasswordSet);
        buf.writeInt(packet.length);
        buf.writeBlockPos(packet.pos);
    }, ClientboundOpenKeypadPacket::new);

    @Override
    public @NotNull Type<ClientboundOpenKeypadPacket> type() {
        return TYPE;
    }
}