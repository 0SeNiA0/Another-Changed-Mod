package net.zaharenko424.a_changed.network.packets.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ClientboundRemoveAttachmentPacket(int holderId, ResourceLocation attachmentId) implements CustomPacketPayload {

    public static final Type<ClientboundRemoveAttachmentPacket> TYPE = new Type<>(AChanged.resourceLoc("clientbound_remove_attachment"));

    public ClientboundRemoveAttachmentPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readResourceLocation());
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundRemoveAttachmentPacket> CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeVarInt(packet.holderId);
                buf.writeResourceLocation(packet.attachmentId);
            }, ClientboundRemoveAttachmentPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}