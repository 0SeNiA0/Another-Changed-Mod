package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record ClientboundTransfurToleranceSyncPacket(float transfurTolerance) implements CustomPacketPayload {

    public static final Type<ClientboundTransfurToleranceSyncPacket> TYPE = new Type<>(AChanged.resourceLoc("transfur_tolerance_sync"));

    public ClientboundTransfurToleranceSyncPacket(){
        this(TransfurManager.TRANSFUR_TOLERANCE);
    }

    public ClientboundTransfurToleranceSyncPacket(FriendlyByteBuf buffer){
        this(buffer.readFloat());
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundTransfurToleranceSyncPacket> CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeFloat(packet.transfurTolerance),
            ClientboundTransfurToleranceSyncPacket::new);

    @Override
    public @NotNull Type<ClientboundTransfurToleranceSyncPacket> type() {
        return TYPE;
    }
}