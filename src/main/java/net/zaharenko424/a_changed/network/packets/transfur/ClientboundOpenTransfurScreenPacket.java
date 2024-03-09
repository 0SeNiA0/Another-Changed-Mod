package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ClientboundOpenTransfurScreenPacket() implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("open_transfur_screen");

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {}

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}