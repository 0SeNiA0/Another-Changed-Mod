package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundLatexEncoderScreenPacket(BlockPos pos, int index, int data) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("latex_encoder_screen");

    public ServerboundLatexEncoderScreenPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(index);
        buffer.writeInt(data);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    /*@Override
    public void handleMainThread(NetworkEvent.@NotNull Context context) {
        ServerPlayer sender = context.getSender();
        if(sender == null) {
            AChanged.LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        if(sender.distanceToSqr(pos.getCenter()) > 64) return;
        if(!(sender.level().getBlockEntity(pos) instanceof LatexEncoderEntity encoder)) return;
        encoder.setData(index, data);
    }*/
}