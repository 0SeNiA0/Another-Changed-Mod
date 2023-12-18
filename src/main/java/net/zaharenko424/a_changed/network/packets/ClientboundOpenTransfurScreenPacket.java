package net.zaharenko424.a_changed.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.client.screen.TransfurScreen;

public class ClientboundOpenTransfurScreenPacket implements SimpleMessage {

    public ClientboundOpenTransfurScreenPacket(){}

    public ClientboundOpenTransfurScreenPacket(FriendlyByteBuf buffer){}

    @Override
    public void encode(FriendlyByteBuf buffer) {}

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleMainThread(NetworkEvent.Context context) {
        LocalPlayer player= Minecraft.getInstance().player;
        if(player==null) return;
        Minecraft.getInstance().setScreen(new TransfurScreen());
    }
}