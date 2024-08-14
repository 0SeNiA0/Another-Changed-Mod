package net.zaharenko424.a_changed.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * Whoever modifies the data is responsible for its synchronisation!
 */
public interface AbilityData {
    
    void syncClients();
    
    void syncClient(ServerPlayer receiver);

    void fromPacket(FriendlyByteBuf packet);
}