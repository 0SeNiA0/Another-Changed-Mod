package net.zaharenko424.a_changed.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.network.packets.*;
import net.zaharenko424.a_changed.network.packets.grab.*;
import net.zaharenko424.a_changed.network.packets.transfur.*;

public final class PacketHandler {
    private PacketHandler(){}
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(AChanged.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int i = 0;
    public static void init(){
        if(i > 0) throw new IllegalStateException("PacketHandler already initialized!");
        //TF tolerance update
        INSTANCE.simpleMessageBuilder(ClientboundTransfurToleranceUpdatePacket.class, i++)
                .decoder(ClientboundTransfurToleranceUpdatePacket::new).add();
        //Grab
        INSTANCE.simpleMessageBuilder(ClientboundGrabUpdatePacket.class, i++)
                .decoder(ClientboundGrabUpdatePacket::new).add();
        INSTANCE.simpleMessageBuilder(ClientboundRemoteGrabUpdatePacket.class, i++)
                .decoder(ClientboundRemoteGrabUpdatePacket::new).add();
        INSTANCE.simpleMessageBuilder(ServerboundGrabPacket.class, i++)
                .decoder(ServerboundGrabPacket::new).add();
        //Grab modes
        INSTANCE.simpleMessageBuilder(ServerboundGrabModePacket.class, i++)
                .decoder(ServerboundGrabModePacket::new).add();
        INSTANCE.simpleMessageBuilder(ServerboundWantToBeGrabbedPacket.class, i++)
                .decoder(ServerboundWantToBeGrabbedPacket::new).add();
        //TF
        INSTANCE.simpleMessageBuilder(ClientboundPlayerTransfurUpdatePacket.class, i++)
                .decoder(ClientboundPlayerTransfurUpdatePacket::new).add();
        INSTANCE.simpleMessageBuilder(ClientboundRemotePlayerTransfurUpdatePacket.class, i++)
                .decoder(ClientboundRemotePlayerTransfurUpdatePacket::new).add();
        //TF screen
        INSTANCE.simpleMessageBuilder(ClientboundOpenTransfurScreenPacket.class, i++)
                .decoder(ClientboundOpenTransfurScreenPacket::new).add();
        INSTANCE.simpleMessageBuilder(ServerboundTransfurChoicePacket.class, i++)
                .decoder(ServerboundTransfurChoicePacket::new).add();
        //Latex encoder screen
        INSTANCE.simpleMessageBuilder(ServerboundLatexEncoderScreenPacket.class, i++)
                .decoder(ServerboundLatexEncoderScreenPacket::new).add();
        //Note
        INSTANCE.simpleMessageBuilder(ClientboundOpenNotePacket.class, i++)
                .decoder(ClientboundOpenNotePacket::new).add();
        INSTANCE.simpleMessageBuilder(ServerboundEditNotePacket.class, i++)
                .decoder(ServerboundEditNotePacket::new).add();
        //Keypad
        INSTANCE.simpleMessageBuilder(ClientboundOpenKeypadPacket.class, i++)
                .decoder(ClientboundOpenKeypadPacket::new).add();
        INSTANCE.simpleMessageBuilder(ServerboundTryPasswordPacket.class, i++)
                .decoder(ServerboundTryPasswordPacket::new).add();
    }
}