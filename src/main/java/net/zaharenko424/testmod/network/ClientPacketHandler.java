package net.zaharenko424.testmod.network;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.NetworkEvent;
import net.zaharenko424.testmod.entity.TransfurHolder;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {

    public static void handlePlayerTransfurUpdate(int transfurProgress,String transfurType, NetworkEvent.Context context){
        System.out.println("packet is being handled!");
        TransfurHolder holder=(TransfurHolder) Minecraft.getInstance().player;
        if(holder!=null) holder.mod$setTransfurProgress(transfurProgress,transfurType);
    }
}