package net.zaharenko424.testmod.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.testmod.capability.ITransfurHandler;
import net.zaharenko424.testmod.capability.TransfurCapability;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

import static net.zaharenko424.testmod.TestMod.LOGGER;
import static net.zaharenko424.testmod.TransfurManager.*;

public class ClientboundRemotePlayerTransfurUpdatePacket implements SimpleMessage {
    private static final String UUID_KEY = "uuid";
    //TODO check if works
    private final UUID playerId;
    private final int transfurProgress;
    private final ResourceLocation transfurType;
    private final boolean isTransfurred;

    public ClientboundRemotePlayerTransfurUpdatePacket(@NotNull ITransfurHandler handler,@NotNull UUID uuid){
        transfurProgress=handler.getTransfurProgress();
        transfurType=handler.getTransfurType();
        isTransfurred=handler.isTransfurred();
        playerId=uuid;
    }

    public ClientboundRemotePlayerTransfurUpdatePacket(@NotNull FriendlyByteBuf buffer){
        CompoundTag tag=buffer.readNbt();
        if(tag==null) throw new RuntimeException("Empty or corrupted packet received!");
        playerId=tag.getUUID(UUID_KEY);
        transfurProgress = tag.getInt(TRANSFUR_PROGRESS_KEY);
        if(tag.contains(TRANSFUR_TYPE_KEY)) transfurType = new ResourceLocation(tag.getString(TRANSFUR_TYPE_KEY)); else transfurType=null;
        isTransfurred = tag.getBoolean(TRANSFURRED_KEY);
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        CompoundTag tag=new CompoundTag();
        tag.putUUID(UUID_KEY,playerId);
        tag.putInt(TRANSFUR_PROGRESS_KEY, transfurProgress);
        if(transfurType!=null) tag.putString(TRANSFUR_TYPE_KEY, transfurType.toString());
        tag.putBoolean(TRANSFURRED_KEY, isTransfurred);
        buffer.writeNbt(tag);
    }

    @Override
    public void handleMainThread(NetworkEvent.Context context) {
        Player player = Objects.requireNonNull(Minecraft.getInstance().level).getPlayerByUUID(playerId);
        if(player==null){
            LOGGER.warn("No player found with uuid "+playerId+"!");
            return;
        }
        ITransfurHandler capability = player.getCapability(TransfurCapability.CAPABILITY).orElseThrow(TransfurCapability.NO_CAPABILITY_EXC);
        if(!isTransfurred){
            if(capability.isTransfurred()){
                capability.unTransfur();
            } else capability.setTransfurProgress(transfurProgress,transfurType);
            return;
        }
        capability.transfur(transfurType);
    }
}