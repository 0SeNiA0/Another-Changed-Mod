package net.zaharenko424.a_changed.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ClientboundLTCDataPacket(ChunkPos pos, byte flags, byte[] rawData) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("ltc_data_sync");

    public ClientboundLTCDataPacket(FriendlyByteBuf buf){
        this(new ChunkPos(buf.readVarInt(), buf.readVarInt()), buf.readByte(), buf.readByteArray());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeVarInt(pos.x);
        buf.writeVarInt(pos.z);
        buf.writeByteArray(rawData);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}