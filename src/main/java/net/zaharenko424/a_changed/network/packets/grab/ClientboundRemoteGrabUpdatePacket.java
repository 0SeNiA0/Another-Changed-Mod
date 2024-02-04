package net.zaharenko424.a_changed.network.packets.grab;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.GrabMode;
import net.zaharenko424.a_changed.capability.IGrabHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ClientboundRemoteGrabUpdatePacket implements SimpleMessage {

    private final UUID playerId;
    private final int targetId;
    private final int grabbedBy;
    private final GrabMode mode;
    private final boolean wantsToBeGrabbed;

    public ClientboundRemoteGrabUpdatePacket(UUID playerId, int targetId, int grabbedBy, GrabMode mode, boolean wantsToBeGrabbed){
        this.playerId = playerId;
        this.targetId = targetId;
        this.grabbedBy = grabbedBy;
        this.mode = mode;
        this.wantsToBeGrabbed = wantsToBeGrabbed;
    }

    public ClientboundRemoteGrabUpdatePacket(@NotNull FriendlyByteBuf buf){
        playerId = buf.readUUID();
        targetId = buf.readInt();
        grabbedBy = buf.readInt();
        mode = buf.readEnum(GrabMode.class);
        wantsToBeGrabbed = buf.readBoolean();
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buffer) {
        buffer.writeUUID(playerId);
        buffer.writeInt(targetId);
        buffer.writeInt(grabbedBy);
        buffer.writeEnum(mode);
        buffer.writeBoolean(wantsToBeGrabbed);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleMainThread(NetworkEvent.Context context) {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(playerId);
        if(player == null) return;
        IGrabHandler handler = player.getCapability(GrabCapability.CAPABILITY).orElseThrow(GrabCapability.NO_CAPABILITY_EXC);
        Level level = Minecraft.getInstance().level;
        if(targetId == -1) {
            if(handler.getTarget() != null) handler.drop();
        } else handler.grab((LivingEntity) level.getEntity(targetId));
        if(grabbedBy == -1) {
            if(handler.getGrabbedBy() != null) handler.setGrabbedBy(null);
        } else handler.setGrabbedBy((Player) level.getEntity(grabbedBy));
        handler.setGrabMode(mode);
        handler.setWantsToBeGrabbed(wantsToBeGrabbed);
    }
}