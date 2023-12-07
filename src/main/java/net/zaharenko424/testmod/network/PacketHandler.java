package net.zaharenko424.testmod.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.network.packets.ClientboundOpenNotePacket;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerTransfurUpdatePacket;
import net.zaharenko424.testmod.network.packets.ClientboundRemotePlayerTransfurUpdatePacket;
import net.zaharenko424.testmod.network.packets.ServerboundEditNotePacket;

public final class PacketHandler {
    private PacketHandler(){}
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TestMod.MODID, "main"),
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
    }
}