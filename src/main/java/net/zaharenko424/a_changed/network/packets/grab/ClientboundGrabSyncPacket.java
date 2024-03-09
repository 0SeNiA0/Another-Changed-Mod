package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.GrabMode;
import org.jetbrains.annotations.NotNull;

public record ClientboundGrabSyncPacket(int targetId, int grabbedBy, GrabMode mode, boolean wantsToBeGrabbed) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("grab_sync");

    public ClientboundGrabSyncPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readInt(), buf.readInt(), buf.readEnum(GrabMode.class), buf.readBoolean());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeInt(targetId);
        buffer.writeInt(grabbedBy);
        buffer.writeEnum(mode);
        buffer.writeBoolean(wantsToBeGrabbed);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}