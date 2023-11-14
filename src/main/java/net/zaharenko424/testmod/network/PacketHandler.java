package net.zaharenko424.testmod.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerTransfurUpdatePacket;
import net.zaharenko424.testmod.network.packets.ClientboundRemotePlayerTransfurUpdatePacket;

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
    }
}