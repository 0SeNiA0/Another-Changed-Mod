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

public record ServerboundAbilityPacket(ResourceLocation abilityId, FriendlyByteBuf buffer) implements CustomPacketPayload {

    public static final Type<ServerboundAbilityPacket> TYPE = new Type<>(AChanged.resourceLoc("serverbound_ability_sync"));

    public ServerboundAbilityPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readResourceLocation(), new FriendlyByteBuf(Unpooled.wrappedBuffer(buf.readByteArray())));
    }

    public Ability ability(){
        return Objects.requireNonNull(AbilityRegistry.ABILITY_REGISTRY.get(abilityId), "Client-Server ability desync! Compare mod lists.");
    }

    public static final StreamCodec<FriendlyByteBuf, ServerboundAbilityPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeResourceLocation(packet.abilityId);
        //If backing array is not fully used, copy the used part to dummy and write that.
        if(packet.buffer.readableBytes() == packet.buffer.array().length) {
            buf.writeByteArray(packet.buffer.array());
        } else {
            byte[] dummy = new byte[packet.buffer.readableBytes()];
            System.arraycopy(packet.buffer.array(), 0, dummy, 0, dummy.length);
            buf.writeByteArray(dummy);
        }
    }, ServerboundAbilityPacket::new);

    @Override
    public @NotNull Type<ServerboundAbilityPacket> type() {
        return TYPE;
    }
}