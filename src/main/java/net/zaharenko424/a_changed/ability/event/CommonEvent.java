package net.zaharenko424.a_changed.ability.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zaharenko424.a_changed.ability.AbilityHolderAttachment;
import net.zaharenko424.a_changed.ability.network.ClientPacketHandler;
import net.zaharenko424.a_changed.ability.network.packets.BidirectionalAbilityPacket;
import net.zaharenko424.a_changed.ability.network.packets.BidirectionalInputPacket;
import net.zaharenko424.a_changed.ability.network.packets.ClientboundAbilityPacket;
import net.zaharenko424.a_changed.ability.network.packets.ServerboundSelectAbilityPacket;
import net.zaharenko424.a_changed.util.AbilityUtils;

@EventBusSubscriber
public class CommonEvent {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        AbilityHolderAttachment attachment = AbilityHolderAttachment.of(player);
        NeoForge.EVENT_BUS.post(new InitializePlayerAbilitiesEvent(event.getEntity(), attachment));
        attachment.trySelectLoaded();
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event){
        if(event.getEntity().level().isClientSide) return;
        AbilityUtils.deactivateAbilities(event.getEntity());
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event){
        if(event.getEntity().level().isClientSide || !(event.getEntity() instanceof Player player)) return;
        AbilityUtils.of(player).serverTick();
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        if(event.getEntity().level().isClientSide) return;
        AbilityUtils.deactivateAbilities(event.getEntity());
    }

    public static void onRegisterPayloads(PayloadRegistrar registrar){
        registrar.playBidirectional(BidirectionalInputPacket.TYPE, BidirectionalInputPacket.CODEC,
                ((payload, context) -> context.enqueueWork(() ->
                        AbilityUtils.of(context.player()).getInputController().handleData(payload.buffer()))));

        registrar.playToServer(ServerboundSelectAbilityPacket.TYPE, ServerboundSelectAbilityPacket.CODEC,
                (payload, context) -> context.enqueueWork(() ->
                        AbilityUtils.of(context.player()).selectAbility(payload.ability())));

        registrar.playBidirectional(BidirectionalAbilityPacket.TYPE, BidirectionalAbilityPacket.CODEC,
                (payload, context) -> context.enqueueWork(() ->
                        payload.ability(ability -> ability.handleData(context.player(), payload.buffer(), context))));

        registrar.playToClient(ClientboundAbilityPacket.TYPE, ClientboundAbilityPacket.CODEC,
                ((payload, context) ->
                        ClientPacketHandler.INSTANCE.handleAbilityData(payload, context)));
    }
}
