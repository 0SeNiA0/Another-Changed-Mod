package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.GrabMode;
import org.jetbrains.annotations.NotNull;

public record ServerboundGrabModePacket(GrabMode mode) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("grab_mode");

    public ServerboundGrabModePacket(@NotNull FriendlyByteBuf buf){
        this(buf.readEnum(GrabMode.class));
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeEnum(mode);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}