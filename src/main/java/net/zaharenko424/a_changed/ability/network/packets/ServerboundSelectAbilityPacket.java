package net.zaharenko424.a_changed.ability.network.packets;

import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;

public record ServerboundSelectAbilityPacket(int abilityId) implements CustomPacketPayload {

    public static final Type<ServerboundSelectAbilityPacket> TYPE = new Type<>(AChanged.resourceLoc("select_ability"));

    public ServerboundSelectAbilityPacket(Ability ability){
        this(AbilityRegistry.ABILITY_REGISTRY.getId(ability));
    }

    public Ability ability(){
        return AbilityRegistry.ABILITY_REGISTRY.getHolder(abilityId).map(Holder::value).orElse(null);
    }

    public static final StreamCodec<FriendlyByteBuf, ServerboundSelectAbilityPacket> CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeVarInt(packet.abilityId),
            buf -> new ServerboundSelectAbilityPacket(buf.readVarInt()));

    @Override
    public @NotNull Type<ServerboundSelectAbilityPacket> type() {
        return TYPE;
    }
}