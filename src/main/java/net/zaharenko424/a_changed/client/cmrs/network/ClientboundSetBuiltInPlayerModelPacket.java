package net.zaharenko424.a_changed.client.cmrs.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.client.cmrs.CMRS;
import org.jetbrains.annotations.NotNull;

public record ClientboundSetBuiltInPlayerModelPacket(int entityId, ResourceLocation modelId, int priority, boolean removeOnDeath, ModelSetReason reason) implements CustomPacketPayload {

    public static final Type<ClientboundSetBuiltInPlayerModelPacket> TYPE = new Type<>(CMRS.resourceLoc("clientbound_set_builtin_player_model"));

    public ClientboundSetBuiltInPlayerModelPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readResourceLocation(), buf.readVarInt(), buf.readBoolean(), buf.readEnum(ModelSetReason.class));
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundSetBuiltInPlayerModelPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.entityId);
        buf.writeResourceLocation(packet.modelId);
        buf.writeVarInt(packet.priority);
        buf.writeBoolean(packet.removeOnDeath);
        buf.writeEnum(packet.reason);
    }, ClientboundSetBuiltInPlayerModelPacket::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}