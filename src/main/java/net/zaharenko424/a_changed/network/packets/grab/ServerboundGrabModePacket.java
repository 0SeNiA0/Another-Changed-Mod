package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.GrabMode;
import net.zaharenko424.a_changed.capability.IGrabHandler;
import org.jetbrains.annotations.NotNull;

public class ServerboundGrabModePacket implements SimpleMessage {

    private final GrabMode mode;

    public ServerboundGrabModePacket(GrabMode mode){
        this.mode = mode;
    }

    public ServerboundGrabModePacket(@NotNull FriendlyByteBuf buf){
        mode = buf.readEnum(GrabMode.class);
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buffer) {
        buffer.writeEnum(mode);
    }

    @Override
    public void handleMainThread(NetworkEvent.@NotNull Context context) {
        ServerPlayer sender = context.getSender();
        if(sender == null){
            AChanged.LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        IGrabHandler handler = sender.getCapability(GrabCapability.CAPABILITY).orElseThrow(GrabCapability.NO_CAPABILITY_EXC);
        if(handler.grabMode() == mode) return;
        handler.setGrabMode(mode);
    }
}