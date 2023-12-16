package net.zaharenko424.a_changed.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.network.packets.*;

public final class PacketHandler {
    private PacketHandler(){}
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(AChanged.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init(){
        int i=0;
        INSTANCE.simpleMessageBuilder(ClientboundPlayerTransfurUpdatePacket.class,i++)
                .decoder(ClientboundPlayerTransfurUpdatePacket::new).add();
        INSTANCE.simpleMessageBuilder(ClientboundRemotePlayerTransfurUpdatePacket.class,i++)
                .decoder(ClientboundRemotePlayerTransfurUpdatePacket::new).add();

        INSTANCE.simpleMessageBuilder(ClientboundOpenNotePacket.class,i++)
                .decoder(ClientboundOpenNotePacket::new).add();
        INSTANCE.simpleMessageBuilder(ServerboundEditNotePacket.class,i++)
                .decoder(ServerboundEditNotePacket::new).add();

        INSTANCE.simpleMessageBuilder(ClientboundOpenKeypadPacket.class,i++)
                .decoder(ClientboundOpenKeypadPacket::new).add();
        INSTANCE.simpleMessageBuilder(ServerboundTryPasswordPacket.class,i++)
                .decoder(ServerboundTryPasswordPacket::new).add();
    }
}