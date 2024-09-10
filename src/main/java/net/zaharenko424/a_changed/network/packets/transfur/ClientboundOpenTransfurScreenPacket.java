package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ClientboundOpenTransfurScreenPacket() implements CustomPacketPayload {

    public static final Type<ClientboundOpenTransfurScreenPacket> TYPE = new Type<>(AChanged.resourceLoc("open_transfur_screen"));

    private static final ClientboundOpenTransfurScreenPacket INSTANCE = new ClientboundOpenTransfurScreenPacket();

    public static final StreamCodec<FriendlyByteBuf, ClientboundOpenTransfurScreenPacket> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public @NotNull Type<ClientboundOpenTransfurScreenPacket> type() {
        return TYPE;
    }
}