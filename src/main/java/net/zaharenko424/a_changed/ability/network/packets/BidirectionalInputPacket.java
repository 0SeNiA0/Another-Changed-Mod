package net.zaharenko424.a_changed.ability.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.cmrs.util.StreamCodecUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public record BidirectionalInputPacket(byte[] data) implements CustomPacketPayload {

    public static final Type<BidirectionalInputPacket> TYPE = new Type<>(AChanged.resourceLoc("bidirectional_input_data"));

    public BidirectionalInputPacket(Consumer<FriendlyByteBuf> dataWriter){
        this(StreamCodecUtils.writeCustomData(dataWriter));
    }

    public BidirectionalInputPacket(Consumer<FriendlyByteBuf> dataWriter, int expectedSize){
        this(StreamCodecUtils.writeCustomData(dataWriter, expectedSize));
    }

    public FriendlyByteBuf buffer(){
        return new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
    }

    public static final StreamCodec<FriendlyByteBuf, BidirectionalInputPacket> CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeByteArray(packet.data),
            buf -> new BidirectionalInputPacket(buf.readByteArray()));

    @Override
    public @NotNull Type<BidirectionalInputPacket> type() {
        return TYPE;
    }
}