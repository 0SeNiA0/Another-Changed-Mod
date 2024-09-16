package net.zaharenko424.a_changed.network.packets.ability;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundActivateAbilityPacket(boolean oneShot, FriendlyByteBuf additionalData) implements CustomPacketPayload {

    public static final Type<ServerboundActivateAbilityPacket> TYPE = new Type<>(AChanged.resourceLoc("activate_ability"));

    public ServerboundActivateAbilityPacket(FriendlyByteBuf buf){
        this(buf.readBoolean(), new FriendlyByteBuf(Unpooled.wrappedBuffer(buf.readByteArray())));
    }

    public static final StreamCodec<FriendlyByteBuf, ServerboundActivateAbilityPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeBoolean(packet.oneShot);
        if(packet.additionalData != null) {
            //If backing array is not fully used, copy the used part to dummy and write that.
            if(packet.additionalData.readableBytes() == packet.additionalData.array().length) {
                buf.writeByteArray(packet.additionalData.array());
            } else {
                byte[] dummy = new byte[packet.additionalData.readableBytes()];
                System.arraycopy(packet.additionalData.array(), 0, dummy, 0, dummy.length);
                buf.writeByteArray(dummy);
            }
        } else buf.writeByteArray(new byte[0]);
    }, ServerboundActivateAbilityPacket::new);

    @Override
    public @NotNull Type<ServerboundActivateAbilityPacket> type() {
        return TYPE;
    }
}