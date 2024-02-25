package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundTryPasswordPacket(int[] attempt, BlockPos pos) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("try_password");

    public ServerboundTryPasswordPacket(@NotNull FriendlyByteBuf buffer){
        this(buffer.readVarIntArray(8), buffer.readBlockPos());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeVarIntArray(attempt);
        buffer.writeBlockPos(pos);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}