package net.zaharenko424.a_changed.ability.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.cmrs.util.StreamCodecUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public record ClientboundAbilityPacket(int holderId, int abilityId, byte[] data) implements CustomPacketPayload {

    public static final Type<ClientboundAbilityPacket> TYPE =  new Type<>(AChanged.resourceLoc("clientbound_ability_data"));

    public ClientboundAbilityPacket(int holderId, DeferredHolder<Ability, ? extends Ability> ability, Consumer<FriendlyByteBuf> dataWriter){
        this(holderId, AbilityRegistry.ABILITY_REGISTRY.getId(ability.get()), StreamCodecUtils.writeCustomData(dataWriter));
    }

    public ClientboundAbilityPacket(int holderId, DeferredHolder<Ability, ? extends Ability> ability, Consumer<FriendlyByteBuf> dataWriter, int expectedSize){
        this(holderId, AbilityRegistry.ABILITY_REGISTRY.getId(ability.get()), StreamCodecUtils.writeCustomData(dataWriter, expectedSize));
    }

    public void ability(Consumer<Ability> abilityConsumer){
        AbilityRegistry.ABILITY_REGISTRY.getHolder(abilityId).ifPresent(holder -> abilityConsumer.accept(holder.value()));
    }

    public FriendlyByteBuf buffer(){
        return new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundAbilityPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.holderId);
        buf.writeVarInt(packet.abilityId);
        buf.writeByteArray(packet.data);
    }, buf -> new ClientboundAbilityPacket(buf.readVarInt(), buf.readVarInt(), buf.readByteArray()));

    @Override
    public @NotNull Type<ClientboundAbilityPacket> type() {
        return TYPE;
    }
}