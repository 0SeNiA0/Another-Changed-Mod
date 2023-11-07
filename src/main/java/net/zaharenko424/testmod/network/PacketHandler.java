package net.zaharenko424.testmod.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerTransfurUpdatePacket;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerTransfurredPacket;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerUnTransfurredPacket;

public class PacketHandler {

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
        INSTANCE.simpleMessageBuilder(ClientboundPlayerTransfurredPacket.class,i++)
                .decoder(ClientboundPlayerTransfurredPacket::new).add();
        INSTANCE.simpleMessageBuilder(ClientboundPlayerUnTransfurredPacket.class,i++)
                .decoder(ClientboundPlayerUnTransfurredPacket::new).add();
    }
}