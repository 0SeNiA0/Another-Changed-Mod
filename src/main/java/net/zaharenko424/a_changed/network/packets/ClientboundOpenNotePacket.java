package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
@ParametersAreNonnullByDefault
public record ClientboundOpenNotePacket(List<String> text, BlockPos pos, boolean finalized, int guiId) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("open_note");

    public ClientboundOpenNotePacket(FriendlyByteBuf buffer){
        this(buffer.readList(FriendlyByteBuf::readUtf), buffer.readBlockPos(), buffer.readBoolean(), buffer.readByte());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeCollection(text, FriendlyByteBuf::writeUtf);
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(finalized);
        buffer.writeByte(guiId);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}