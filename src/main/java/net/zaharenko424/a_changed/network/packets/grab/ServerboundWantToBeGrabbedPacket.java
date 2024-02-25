package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundWantToBeGrabbedPacket(boolean wantsToBeGrabbed) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("want_to_be_grabbed");

    public ServerboundWantToBeGrabbedPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readBoolean());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeBoolean(wantsToBeGrabbed);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}