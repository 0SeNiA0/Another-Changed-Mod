package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.IGrabHandler;
import org.jetbrains.annotations.NotNull;

public class ServerboundWantToBeGrabbedPacket implements SimpleMessage {

    private final boolean wantsToBeGrabbed;

    public ServerboundWantToBeGrabbedPacket(boolean wantsToBeGrabbed){
        this.wantsToBeGrabbed = wantsToBeGrabbed;
    }

    public ServerboundWantToBeGrabbedPacket(@NotNull FriendlyByteBuf buf){
        wantsToBeGrabbed = buf.readBoolean();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buffer) {
        buffer.writeBoolean(wantsToBeGrabbed);
    }

    @Override
    public void handleMainThread(NetworkEvent.@NotNull Context context) {
        ServerPlayer sender = context.getSender();
        if(sender == null){
            AChanged.LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        IGrabHandler handler = sender.getCapability(GrabCapability.CAPABILITY).orElseThrow(GrabCapability.NO_CAPABILITY_EXC);
        if(handler.wantsToBeGrabbed() != wantsToBeGrabbed) handler.setWantsToBeGrabbed(wantsToBeGrabbed);
    }
}