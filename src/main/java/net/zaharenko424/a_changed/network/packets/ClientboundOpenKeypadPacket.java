package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record ClientboundOpenKeypadPacket(boolean isPasswordSet, int length, BlockPos pos) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("open_keypad");

    public ClientboundOpenKeypadPacket(FriendlyByteBuf buffer){
        this(buffer.readBoolean(), buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(isPasswordSet);
        buffer.writeInt(length);
        buffer.writeBlockPos(pos);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}