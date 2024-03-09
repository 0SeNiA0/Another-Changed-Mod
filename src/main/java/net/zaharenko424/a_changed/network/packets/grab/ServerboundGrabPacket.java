package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundGrabPacket(int targetId) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("grab_action");

    public ServerboundGrabPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readInt());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeInt(targetId);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}