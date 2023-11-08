package net.zaharenko424.testmod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.testmod.entity.TransfurHolder;
import net.zaharenko424.testmod.network.PacketHandler;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerTransfurUpdatePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransfurManager {

    public static final String TRANSFUR_TYPE_KEY ="transfur_type";
    public static final String TRANSFUR_PROGRESS_KEY="transfur_progress";
    public static final String TRANSFURRED_KEY="transfurred";
    public static final int TRANSFUR_TOLERANCE=20;

    public static void updatePlayer(@NotNull Player player){
        if(player.getCommandSenderWorld().isClientSide) return;
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) player),
                new ClientboundPlayerTransfurUpdatePacket(getTransfurProgress(player),
                        getTransfurType(player),isTransfurred(player)));
        //TODO send update packet to everyone who can see @player
    }

    public static boolean isTransfurred(@NotNull Player player){
        return  ((TransfurHolder)player).mod$isTransfurred();
    }

    public static int getTransfurProgress(@NotNull Player player){
        return ((TransfurHolder)player).mod$getTransfurProgress();
    }

    public static void addTransfurProgress(@NotNull Player player, int amount,@NotNull String transfurType){
        Level level=player.getCommandSenderWorld();
        if(level.isClientSide) return;
        TransfurHolder holder=(TransfurHolder) player;
        int progress=holder.mod$getTransfurProgress()+amount;
        if(progress>=TRANSFUR_TOLERANCE) {
            transfur(player,transfurType);
            return;
        }
        holder.mod$setTransfurProgress(progress,transfurType);
        updatePlayer(player);
    }

    public static @NotNull String getTransfurType(@NotNull Player player){
        return ((TransfurHolder)player).mod$getTransfurType();
    }

    public static void transfur(@NotNull Player player,String transfurType){
        if(player.getCommandSenderWorld().isClientSide) return;
        ((TransfurHolder)player).mod$transfur(transfurType);
        updatePlayer(player);
    }

    public static void unTransfur(@NotNull Player player){
        if(player.getCommandSenderWorld().isClientSide) return;
        ((TransfurHolder)player).mod$unTransfur();
        updatePlayer(player);
    }

    private static final String KEY="changed";

    public static @NotNull CompoundTag modTag(@NotNull CompoundTag tag){
        if(tag.contains(KEY)) return tag.getCompound(KEY);
        tag.put(KEY,new CompoundTag());
        return tag.getCompound(KEY);
    }

    public static boolean hasModTag(@Nullable CompoundTag tag){
        return tag != null && tag.contains(KEY);
    }
}