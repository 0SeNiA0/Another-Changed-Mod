package net.zaharenko424.a_changed.ability.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.a_changed.ability.network.packets.ClientboundAbilityPacket;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ClientPacketHandler {

    public static final ClientPacketHandler INSTANCE = new ClientPacketHandler();

    private final Minecraft minecraft = Minecraft.getInstance();

    public void handleAbilityData(ClientboundAbilityPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            Entity target = minecraft.level.getEntity(packet.holderId());
            if(!(target instanceof LivingEntity living)) return;

            packet.ability(ability -> ability.handleData(living, packet.buffer(), context));
        });
    }
}
