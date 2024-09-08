package net.zaharenko424.a_changed.network.packets.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundSelectAbilityPacket(ResourceLocation ability) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("select_ability");

    public ServerboundSelectAbilityPacket(FriendlyByteBuf buf){
        this(buf.readResourceLocation());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(ability);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}