package net.zaharenko424.testmod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.testmod.network.PacketHandler;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerTransfurUpdatePacket;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerTransfurredPacket;
import net.zaharenko424.testmod.network.packets.ClientboundPlayerUnTransfurredPacket;
import org.jetbrains.annotations.NotNull;

public class TransfurManager {

    public static final String TRANSFUR_TYPE_KEY ="transfur_type";
    public static final String TRANSFUR_PROGRESS_KEY="transfur_progress";
    public static final String TRANSFURRED_KEY="transfurred";
    public static final int TRANSFUR_TOLERANCE=20;

    public static boolean isTransfurred(@NotNull Player player){
        return  ((TransfurHolder)player).mod$isTransfurred();
    }

    public static void addTransfurProgress(@NotNull Player player, int amount,@NotNull String transfurType){
        Level level=player.getCommandSenderWorld();
        if(level.isClientSide) return;
        TransfurHolder holder=(TransfurHolder) player;
        int progress=holder.mod$getTransfurProgress()+amount;
        if(progress>TRANSFUR_TOLERANCE) progress=TRANSFUR_TOLERANCE;
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) player),
                new ClientboundPlayerTransfurUpdatePacket(progress,transfurType));
        if(progress==TRANSFUR_TOLERANCE) transfur(player,transfurType);
    }

    public static void transfur(@NotNull Player player,String transfurType){
        if(player.getCommandSenderWorld().isClientSide) return;
        ((TransfurHolder)player).mod$transfur(transfurType);
        //TODO send update packet to client & everyone who can see them
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) player),
                new ClientboundPlayerTransfurredPacket(transfurType));
    }

    public static void unTransfur(@NotNull Player player){
        if(player.getCommandSenderWorld().isClientSide) return;
        ((TransfurHolder)player).mod$unTransfur();
        //TODO send update packet
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) player),
                new ClientboundPlayerUnTransfurredPacket());
    }
}