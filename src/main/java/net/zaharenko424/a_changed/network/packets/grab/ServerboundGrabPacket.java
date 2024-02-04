package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.IGrabHandler;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

public class ServerboundGrabPacket implements SimpleMessage {

    private final int targetId;

    public ServerboundGrabPacket(int targetId){
        this.targetId = targetId;
    }

    public ServerboundGrabPacket(@NotNull FriendlyByteBuf buf){
        targetId = buf.readInt();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buffer) {
        buffer.writeInt(targetId);
    }

    @Override
    public void handleMainThread(NetworkEvent.@NotNull Context context) {
        ServerPlayer sender = context.getSender();
        if(sender == null){
            AChanged.LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        IGrabHandler handler = sender.getCapability(GrabCapability.CAPABILITY).orElseThrow(GrabCapability.NO_CAPABILITY_EXC);
        if(targetId == -10){
            handler.drop();
            return;
        }
        Entity entity0 = sender.level().getEntity(targetId);
        if(!(entity0 instanceof LivingEntity entity) || entity.getTags().contains("a_changed:grabbed")//TODO Sync this to client to not spam packets?
                || sender.distanceTo(entity) > 3 ) return;
        if(entity0 instanceof Player player1 && (TransfurManager.isGrabbed(player1) || (!TransfurManager.getGrabMode(sender).givesDebuffToTarget && !TransfurManager.wantsToBeGrabbed(player1)))) return;
        handler.grab(entity);
    }
}