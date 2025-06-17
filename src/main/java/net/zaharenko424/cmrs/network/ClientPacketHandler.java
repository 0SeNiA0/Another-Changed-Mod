package net.zaharenko424.cmrs.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.cmrs.client.CustomModelManager;
import net.zaharenko424.cmrs.network.packets.ClientboundRemovePlayerModelPacket;
import net.zaharenko424.cmrs.network.packets.ClientboundSetBuiltInPlayerModelPacket;
import org.jetbrains.annotations.NotNull;

public class ClientPacketHandler {

    public static final ClientPacketHandler INSTANCE = new ClientPacketHandler();

    private final Minecraft minecraft = Minecraft.getInstance();

    public void handleSetBuiltInPlayerModelPacket(ClientboundSetBuiltInPlayerModelPacket packet, @NotNull IPayloadContext context){
        context.enqueueWork(() -> {
            Entity entity = minecraft.level.getEntity(packet.entityId());
            if(!(entity instanceof AbstractClientPlayer player)) return;

            if(!CustomModelManager.getInstance().isBuiltIn(packet.modelId())) return;
            CustomModelManager.getInstance().setPlayerModel(player, packet.modelId(), packet.priority(), packet.removeOnDeath(), packet.reason());
        });
    }

    public void handleRemovePlayerModelPacket(ClientboundRemovePlayerModelPacket packet, @NotNull IPayloadContext context){
        context.enqueueWork(() -> {
            Entity entity = minecraft.level.getEntity(packet.entityId());
            if(!(entity instanceof AbstractClientPlayer player)) return;

            if(packet.usePriority()) {
                CustomModelManager.getInstance().removePlayerModel(player, packet.modelId(), packet.priority());
            } else CustomModelManager.getInstance().removePlayerModel(player, packet.modelId());
        });
    }
}
