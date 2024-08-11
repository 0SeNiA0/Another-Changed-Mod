package net.zaharenko424.a_changed.network.packets.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ServerboundAbilityPacket(ResourceLocation abilityId, FriendlyByteBuf buffer) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("serverbound_ability_sync");

    public ServerboundAbilityPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readResourceLocation(), new FriendlyByteBuf(buf.readBytes(buf.readableBytes())));
    }

    public Ability ability(){
        return Objects.requireNonNull(AbilityRegistry.ABILITY_REGISTRY.get(abilityId), "Client-Server ability desync! Compare mod lists.");
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeResourceLocation(abilityId);
        buf.writeBytes(buffer);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}