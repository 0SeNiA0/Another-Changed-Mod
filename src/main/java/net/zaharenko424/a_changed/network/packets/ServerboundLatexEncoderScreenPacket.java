package net.zaharenko424.a_changed.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machines.LatexEncoderEntity;
import org.jetbrains.annotations.NotNull;

public class ServerboundLatexEncoderScreenPacket implements SimpleMessage {

    private final BlockPos pos;
    private final int index;
    private final int data;

    public ServerboundLatexEncoderScreenPacket(@NotNull BlockPos pos, int index, int data){
        this.pos = pos;
        this.index = index;
        this.data = data;
    }

    public ServerboundLatexEncoderScreenPacket(@NotNull FriendlyByteBuf buf){
        pos = buf.readBlockPos();
        index = buf.readInt();
        data = buf.readInt();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(index);
        buffer.writeInt(data);
    }

    @Override
    public void handleMainThread(NetworkEvent.@NotNull Context context) {
        ServerPlayer sender = context.getSender();
        if(sender == null) {
            AChanged.LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        if(sender.distanceToSqr(pos.getCenter()) > 16) return;
        if(!(sender.level().getBlockEntity(pos) instanceof LatexEncoderEntity encoder)) return;
        encoder.setData(index, data);
    }
}