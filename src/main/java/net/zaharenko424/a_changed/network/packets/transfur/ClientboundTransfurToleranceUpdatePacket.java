package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ClientboundTransfurToleranceUpdatePacket implements SimpleMessage {

    private final float transfurTolerance;

    public ClientboundTransfurToleranceUpdatePacket(){
        transfurTolerance=TransfurManager.TRANSFUR_TOLERANCE;
    }

    public ClientboundTransfurToleranceUpdatePacket(FriendlyByteBuf buffer){
        transfurTolerance=buffer.readFloat();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeFloat(transfurTolerance);
    }

    @Override
    public void handleMainThread(NetworkEvent.Context context) {
        TransfurManager.TRANSFUR_TOLERANCE=transfurTolerance;
    }
}