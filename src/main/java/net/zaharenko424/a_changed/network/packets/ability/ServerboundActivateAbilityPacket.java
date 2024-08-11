package net.zaharenko424.a_changed.network.packets.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundActivateAbilityPacket(boolean oneShot, FriendlyByteBuf additionalData) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("activate_ability");

    public ServerboundActivateAbilityPacket(FriendlyByteBuf buf){
        this(buf.readBoolean(), new FriendlyByteBuf(buf.readBytes(buf.readableBytes())));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(oneShot);
        if(additionalData != null) buf.writeBytes(additionalData);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}