package net.zaharenko424.a_changed.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.TransfurDamageSource;
import net.zaharenko424.a_changed.TransfurManager;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.registry.SoundRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ServerboundTransfurChoicePacket implements SimpleMessage {

    private final boolean becomeTransfur;

    public ServerboundTransfurChoicePacket(boolean becomeTransfur){
        this.becomeTransfur =becomeTransfur;
    }

    public ServerboundTransfurChoicePacket(FriendlyByteBuf buffer){
        becomeTransfur =buffer.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(becomeTransfur);
    }

    @Override
    public void handleMainThread(NetworkEvent.Context context) {
        ServerPlayer player=context.getSender();
        if(player==null){
            AChanged.LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        ITransfurHandler handler=player.getCapability(TransfurCapability.CAPABILITY).orElseThrow(TransfurCapability.NO_CAPABILITY_EXC);
        if(becomeTransfur){
            player.level().playSound(null,player, SoundRegistry.TRANSFUR.get(), SoundSource.PLAYERS,1,1);
            handler.transfur(handler.getTransfurType());
            TransfurManager.updatePlayer(player,handler);
            return;
        }
        TransfurManager.spawnLatex(handler.getTransfurType(),player.level(),player.blockPosition());
        handler.unTransfur();
        TransfurManager.updatePlayer(player,handler);
        player.hurt(TransfurDamageSource.transfur(player,null),Float.MAX_VALUE);
    }
}