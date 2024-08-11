package net.zaharenko424.a_changed.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public interface AbilityData {
    
    void syncClients();
    
    void syncClient(ServerPlayer receiver);

    void fromPacket(FriendlyByteBuf packet);
}