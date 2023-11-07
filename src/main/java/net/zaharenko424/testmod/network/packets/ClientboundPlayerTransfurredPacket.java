package net.zaharenko424.testmod.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.testmod.TransfurHolder;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.testmod.TransfurManager.*;

public class ClientboundPlayerTransfurredPacket implements SimpleMessage {
    private final String transfurType;

    public ClientboundPlayerTransfurredPacket(String transfurType){
        this.transfurType=transfurType;
    }

    public ClientboundPlayerTransfurredPacket(@NotNull FriendlyByteBuf buffer){
        CompoundTag tag=buffer.readNbt();
        try {
            transfurType = tag.getString(TRANSFUR_TYPE_KEY);
        } catch (Exception ex){
            throw new RuntimeException("Empty or corrupted packet received!");
        }
    }

    @Override
    public void encode(@NotNull FriendlyByteBuf buffer) {
        CompoundTag tag=new CompoundTag();
        tag.putString(TRANSFUR_TYPE_KEY,transfurType);
        buffer.writeNbt(tag);
    }

    @Override
    public void handleMainThread(NetworkEvent.Context context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            TransfurHolder holder=(TransfurHolder) Minecraft.getInstance().player;
            if(holder!=null) holder.mod$transfur(transfurType);
        });
    }
}
