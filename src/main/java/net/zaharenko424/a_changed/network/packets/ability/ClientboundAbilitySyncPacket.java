package net.zaharenko424.a_changed.network.packets.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ClientboundAbilitySyncPacket(int holderId, ResourceLocation abilityId, FriendlyByteBuf buffer) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("clientbound_ability_sync");

    public ClientboundAbilitySyncPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readResourceLocation(),
                new FriendlyByteBuf(buf.readBytes(buf.readableBytes())));
    }

    public Ability ability(){
        return Objects.requireNonNull(AbilityRegistry.ABILITY_REGISTRY.get(abilityId), "Client-Server ability desync! Compare mod lists.");
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeVarInt(holderId);
        buf.writeResourceLocation(abilityId);
        buf.writeBytes(buffer);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}