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

public record BidirectionalAbilityPacket(int abilityId, byte[] data) implements CustomPacketPayload {

    public static final Type<BidirectionalAbilityPacket> TYPE = new Type<>(AChanged.resourceLoc("bidirectional_ability_data"));

    public BidirectionalAbilityPacket(DeferredHolder<Ability, ? extends Ability> ability, Consumer<FriendlyByteBuf> dataWriter){
        this(AbilityRegistry.ABILITY_REGISTRY.getId(ability.get()), StreamCodecUtils.writeCustomData(dataWriter));
    }

    public BidirectionalAbilityPacket(DeferredHolder<Ability, ? extends Ability> ability, Consumer<FriendlyByteBuf> dataWriter, int expectedSize){
        this(AbilityRegistry.ABILITY_REGISTRY.getId(ability.get()), StreamCodecUtils.writeCustomData(dataWriter, expectedSize));
    }

    public void ability(Consumer<Ability> abilityConsumer){
        Ability ability = AbilityRegistry.ABILITY_REGISTRY.byId(abilityId);
        if(ability != null) abilityConsumer.accept(ability);
    }

    public FriendlyByteBuf buffer(){
        return new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
    }

    public static final StreamCodec<FriendlyByteBuf, BidirectionalAbilityPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.abilityId);
        buf.writeByteArray(packet.data);
    }, buf -> new BidirectionalAbilityPacket(buf.readVarInt(), buf.readByteArray()));

    @Override
    public @NotNull Type<BidirectionalAbilityPacket> type() {
        return TYPE;
    }
}