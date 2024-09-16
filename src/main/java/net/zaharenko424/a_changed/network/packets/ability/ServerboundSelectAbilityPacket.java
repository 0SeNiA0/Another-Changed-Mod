package net.zaharenko424.a_changed.network.packets.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundSelectAbilityPacket(ResourceLocation ability) implements CustomPacketPayload {

    public static final Type<ServerboundSelectAbilityPacket> TYPE = new Type<>(AChanged.resourceLoc("select_ability"));

    public ServerboundSelectAbilityPacket(FriendlyByteBuf buf){
        this(buf.readResourceLocation());
    }

    public static final StreamCodec<FriendlyByteBuf, ServerboundSelectAbilityPacket> CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeResourceLocation(packet.ability),
            ServerboundSelectAbilityPacket::new);

    @Override
    public @NotNull Type<ServerboundSelectAbilityPacket> type() {
        return TYPE;
    }
}