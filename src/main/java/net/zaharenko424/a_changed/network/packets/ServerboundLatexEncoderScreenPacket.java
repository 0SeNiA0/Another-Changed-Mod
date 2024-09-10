package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundLatexEncoderScreenPacket(BlockPos pos, int index, int data) implements CustomPacketPayload {

    public static final Type<ServerboundLatexEncoderScreenPacket> TYPE = new Type<>(AChanged.resourceLoc("latex_encoder_screen"));

    public ServerboundLatexEncoderScreenPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public static final StreamCodec<FriendlyByteBuf, ServerboundLatexEncoderScreenPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeBlockPos(packet.pos);
        buf.writeInt(packet.index);
        buf.writeInt(packet.data);
    }, ServerboundLatexEncoderScreenPacket::new);

    @Override
    public @NotNull Type<ServerboundLatexEncoderScreenPacket> type() {
        return TYPE;
    }
}