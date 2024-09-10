package net.zaharenko424.a_changed.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ClientboundSmoothLookPacket(float xRot, float yRot, float speed, int ticks) implements CustomPacketPayload {

    public static final Type<ClientboundSmoothLookPacket> TYPE = new Type<>(AChanged.resourceLoc("smooth_look"));

    public ClientboundSmoothLookPacket(FriendlyByteBuf buf){
        this(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readVarInt());
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundSmoothLookPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeFloat(packet.xRot);
        buf.writeFloat(packet.yRot);
        buf.writeFloat(packet.speed);
        buf.writeVarInt(packet.ticks);
    }, ClientboundSmoothLookPacket::new);

    @Override
    public @NotNull Type<ClientboundSmoothLookPacket> type() {
        return TYPE;
    }
}