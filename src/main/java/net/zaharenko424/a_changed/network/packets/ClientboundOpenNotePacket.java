package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
@ParametersAreNonnullByDefault
public record ClientboundOpenNotePacket(List<String> text, BlockPos pos, boolean finalized, int guiId) implements CustomPacketPayload {

    public static final Type<ClientboundOpenNotePacket> TYPE = new Type<>(AChanged.resourceLoc("open_note"));

    public ClientboundOpenNotePacket(FriendlyByteBuf buffer){
        this(buffer.readList(FriendlyByteBuf::readUtf), buffer.readBlockPos(), buffer.readBoolean(), buffer.readByte());
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundOpenNotePacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeCollection(packet.text, FriendlyByteBuf::writeUtf);
        buf.writeBlockPos(packet.pos);
        buf.writeBoolean(packet.finalized);
        buf.writeByte(packet.guiId);
    }, ClientboundOpenNotePacket::new);

    @Override
    public @NotNull Type<ClientboundOpenNotePacket> type() {
        return TYPE;
    }
}