package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.GrabMode;
import org.jetbrains.annotations.NotNull;

public record ClientboundGrabDataSyncPacket(int holderId, int targetId, int grabbedBy, GrabMode mode, boolean wantsToBeGrabbed) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("grab_data_sync");

    public ClientboundGrabDataSyncPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readEnum(GrabMode.class), buf.readBoolean());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeVarInt(holderId);
        buffer.writeVarInt(targetId);
        buffer.writeVarInt(grabbedBy);
        buffer.writeEnum(mode);
        buffer.writeBoolean(wantsToBeGrabbed);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}