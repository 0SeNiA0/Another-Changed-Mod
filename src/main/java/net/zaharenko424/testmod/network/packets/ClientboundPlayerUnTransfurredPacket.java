package net.zaharenko424.testmod.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.testmod.TransfurHolder;
import org.jetbrains.annotations.NotNull;

public class ClientboundPlayerUnTransfurredPacket implements SimpleMessage {

    public ClientboundPlayerUnTransfurredPacket(){}

    public ClientboundPlayerUnTransfurredPacket(@NotNull FriendlyByteBuf buffer){
    }

    public void encode(@NotNull FriendlyByteBuf buffer) {
    }

    @Override
    public void handleMainThread(NetworkEvent.Context context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            TransfurHolder holder=(TransfurHolder) Minecraft.getInstance().player;
            if(holder!=null) holder.mod$unTransfur();
        });
    }
}
