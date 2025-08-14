package net.zaharenko424.cmrs.network;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.cmrs.ModelProperties;
import net.zaharenko424.cmrs.network.packets.ServerboundModelPropertySync;
import net.zaharenko424.cmrs.registry.AttachmentRegistry;

public class ServerPacketHandler {

    public static void handleModelPropertySync(ServerboundModelPropertySync packet, IPayloadContext context){
        context.enqueueWork(() -> {
            Player player = context.player();

            ModelProperties properties = player.getData(AttachmentRegistry.MODEL_PROPERTIES);
            properties.set(packet.properties());
            properties.syncPlayers();
        });
    }
}
