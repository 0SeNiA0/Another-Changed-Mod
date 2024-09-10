package net.zaharenko424.a_changed.network.packets.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundDeactivateAbilityPacket() implements CustomPacketPayload {

    public static final Type<ServerboundDeactivateAbilityPacket> TYPE = new Type<>(AChanged.resourceLoc("deactivate_ability"));

    private static final ServerboundDeactivateAbilityPacket INSTANCE = new ServerboundDeactivateAbilityPacket();

    public static final StreamCodec<FriendlyByteBuf, ServerboundDeactivateAbilityPacket> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public @NotNull Type<ServerboundDeactivateAbilityPacket> type() {
        return TYPE;
    }
}