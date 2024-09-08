package net.zaharenko424.a_changed.network.packets.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundDeactivateAbilityPacket() implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("deactivate_ability");

    public ServerboundDeactivateAbilityPacket(FriendlyByteBuf buf){
        this();
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {}

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}