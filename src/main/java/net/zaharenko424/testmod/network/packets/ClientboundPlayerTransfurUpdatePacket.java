package net.zaharenko424.testmod.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.testmod.TransfurHolder;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.testmod.TransfurManager.TRANSFUR_PROGRESS_KEY;
import static net.zaharenko424.testmod.TransfurManager.TRANSFUR_TYPE_KEY;

public class ClientboundPlayerTransfurUpdatePacket implements SimpleMessage {
    private final int transfurProgress;
    private final String transfurType;

    public ClientboundPlayerTransfurUpdatePacket(int transfurProgress, String transfurType){
        this.transfurProgress=transfurProgress;
        this.transfurType=transfurType;
    }

    public ClientboundPlayerTransfurUpdatePacket(@NotNull FriendlyByteBuf buffer){
        CompoundTag tag=buffer.readNbt();
        try {
            transfurProgress = tag.getInt(TRANSFUR_PROGRESS_KEY);
            transfurType = tag.getString(TRANSFUR_TYPE_KEY);
            System.out.println("packet received!");
        } catch (Exception ex){
            throw new RuntimeException("Empty or corrupted packet received!");
        }
    }

    public void encode(@NotNull FriendlyByteBuf buffer){
        CompoundTag tag=new CompoundTag();
        tag.putInt(TRANSFUR_PROGRESS_KEY,transfurProgress);
        tag.putString(TRANSFUR_TYPE_KEY,transfurType);
        buffer.writeNbt(tag);
    }

    @Override
    public void handleMainThread(NetworkEvent.Context context) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            System.out.println("packet is being handled!");
            TransfurHolder holder=(TransfurHolder)Minecraft.getInstance().player;
            if(holder!=null) holder.mod$setTransfurProgress(transfurProgress,transfurType);
        });
    }
}
