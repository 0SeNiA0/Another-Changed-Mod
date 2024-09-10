package net.zaharenko424.a_changed.network.packets.ability;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ClientboundAbilitySyncPacket(int holderId, ResourceLocation abilityId, FriendlyByteBuf buffer) implements CustomPacketPayload {

    public static final Type<ClientboundAbilitySyncPacket> TYPE =  new Type<>(AChanged.resourceLoc("clientbound_ability_sync"));

    public ClientboundAbilitySyncPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readResourceLocation(),
                new FriendlyByteBuf(Unpooled.wrappedBuffer(buf.readByteArray())));
    }

    public Ability ability(){
        return Objects.requireNonNull(AbilityRegistry.ABILITY_REGISTRY.get(abilityId), "Client-Server ability desync! Compare mod lists.");
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundAbilitySyncPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.holderId);
        buf.writeResourceLocation(packet.abilityId);
        //If backing array is not fully used, copy the used part to dummy and write that.
        if(packet.buffer.readableBytes() == packet.buffer.array().length) {
            buf.writeByteArray(packet.buffer.array());
        } else {
            byte[] dummy = new byte[packet.buffer.readableBytes()];
            System.arraycopy(packet.buffer.array(), 0, dummy, 0, dummy.length);
            buf.writeByteArray(dummy);
        }
    }, ClientboundAbilitySyncPacket::new);

    @Override
    public @NotNull Type<ClientboundAbilitySyncPacket> type() {
        return TYPE;
    }
}