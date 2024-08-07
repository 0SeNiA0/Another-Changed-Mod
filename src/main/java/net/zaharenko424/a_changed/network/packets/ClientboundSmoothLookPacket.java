package net.zaharenko424.a_changed.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ClientboundSmoothLookPacket(float xRot, float yRot, float speed, int ticks) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("smooth_look");

    public ClientboundSmoothLookPacket(FriendlyByteBuf buf){
        this(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readVarInt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(xRot);
        buf.writeFloat(yRot);
        buf.writeFloat(speed);
        buf.writeVarInt(ticks);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}