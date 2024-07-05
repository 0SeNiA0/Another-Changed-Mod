package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ClientboundGrabbedBySyncPacket(int holderId, int grabbedBy) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("grabbed_by_sync");

    public ClientboundGrabbedBySyncPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readVarInt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(holderId);
        buf.writeVarInt(grabbedBy);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
