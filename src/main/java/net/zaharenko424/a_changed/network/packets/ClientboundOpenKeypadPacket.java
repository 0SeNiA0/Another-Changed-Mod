package net.zaharenko424.a_changed.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.client.screen.KeypadScreen;
import org.jetbrains.annotations.NotNull;

public class ClientboundOpenKeypadPacket implements SimpleMessage {

    private final boolean isPasswordSet;
    private final int length;
    private final BlockPos pos;

    public ClientboundOpenKeypadPacket(boolean isPasswordSet, int length,@NotNull BlockPos pos){
        this.isPasswordSet=isPasswordSet;
        this.length=length;
        this.pos=pos;
    }

    public ClientboundOpenKeypadPacket(@NotNull FriendlyByteBuf buffer){
        isPasswordSet=buffer.readBoolean();
        length=buffer.readInt();
        pos=buffer.readBlockPos();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buffer) {
        buffer.writeBoolean(isPasswordSet);
        buffer.writeInt(length);
        buffer.writeBlockPos(pos);
    }

    @Override
    public void handleMainThread(NetworkEvent.@NotNull Context context) {
        if(Minecraft.getInstance().player==null) return;
        Minecraft.getInstance().setScreen(new KeypadScreen(isPasswordSet,length,pos));
    }
}