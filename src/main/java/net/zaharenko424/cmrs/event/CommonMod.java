package net.zaharenko424.cmrs.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.network.ClientPacketHandler;
import net.zaharenko424.cmrs.network.ServerPacketHandler;
import net.zaharenko424.cmrs.network.packets.ClientboundRemovePlayerModelPacket;
import net.zaharenko424.cmrs.network.packets.ClientboundSetBuiltInPlayerModelPacket;
import net.zaharenko424.cmrs.network.packets.ServerboundModelPropertySync;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber()
public class CommonMod {

    @SubscribeEvent
    public static void onRegisterPayload(@NotNull RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar(CMRS.MODID);

        //Lambda SHOULDN'T be replaced with method reference on handleClient! -> server will crash

        registrar.playToClient(ClientboundSetBuiltInPlayerModelPacket.TYPE, ClientboundSetBuiltInPlayerModelPacket.CODEC,
                ((payload, context) -> ClientPacketHandler.INSTANCE.handleSetBuiltInPlayerModelPacket(payload, context)));

        registrar.playToClient(ClientboundRemovePlayerModelPacket.TYPE, ClientboundRemovePlayerModelPacket.CODEC,
                (payload, context) -> ClientPacketHandler.INSTANCE.handleRemovePlayerModelPacket(payload, context));


        PayloadRegistrar optional = registrar.optional();

        optional.playToServer(ServerboundModelPropertySync.TYPE, ServerboundModelPropertySync.CODEC,
                ServerPacketHandler::handleModelPropertySync);
    }
}
