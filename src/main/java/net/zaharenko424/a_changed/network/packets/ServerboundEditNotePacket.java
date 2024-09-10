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
public record ServerboundEditNotePacket(List<String> text, BlockPos pos, boolean finalize_) implements CustomPacketPayload {

    public static final Type<ServerboundEditNotePacket> TYPE = new Type<>(AChanged.resourceLoc("edit_note"));

    public ServerboundEditNotePacket(FriendlyByteBuf buffer){
        this(buffer.readList(FriendlyByteBuf::readUtf), buffer.readBlockPos(), buffer.readBoolean());
    }

    public static final StreamCodec<FriendlyByteBuf, ServerboundEditNotePacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeCollection(packet.text, FriendlyByteBuf::writeUtf);
        buf.writeBlockPos(packet.pos);
        buf.writeBoolean(packet.finalize_);
    }, ServerboundEditNotePacket::new);

    @Override
    public @NotNull Type<ServerboundEditNotePacket> type() {
        return TYPE;
    }
}