package net.zaharenko424.testmod.events;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.capability.TransfurCapability;
import net.zaharenko424.testmod.commands.Transfur;
import net.zaharenko424.testmod.commands.TransfurTolerance;
import net.zaharenko424.testmod.commands.UnTransfur;
import net.zaharenko424.testmod.network.PacketHandler;
import net.zaharenko424.testmod.network.packets.ClientboundRemotePlayerTransfurUpdatePacket;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.testmod.TestMod.LOGGER;
import static net.zaharenko424.testmod.capability.TransfurCapability.CAPABILITY;
import static net.zaharenko424.testmod.capability.TransfurCapability.NO_CAPABILITY_EXC;

@Mod.EventBusSubscriber(modid = TestMod.MODID)
public class CommonEvent {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public static void onRegisterCommands(@NotNull RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher=event.getDispatcher();
        Transfur.register(dispatcher);
        UnTransfur.register(dispatcher);
        TransfurTolerance.register(dispatcher);
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.@NotNull PlayerLoggedInEvent event){
        if(event.getEntity().level().isClientSide) return;
        TransfurManager.updatePlayer((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void onAttachCapabilities(@NotNull AttachCapabilitiesEvent<Entity> event){
        if(!(event.getObject() instanceof LivingEntity entity)) return;
        if(entity instanceof Player||entity.getType().is(TestMod.TRANSFURRABLE_TAG)){
            event.addCapability(TransfurCapability.KEY,TransfurCapability.createProvider(entity));
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.@NotNull StartTracking event){
        if(!(event.getTarget() instanceof Player remotePlayer)) return;
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) event.getEntity()),
                new ClientboundRemotePlayerTransfurUpdatePacket(remotePlayer.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC),remotePlayer.getUUID()));
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.@NotNull Clone event){
        if(!event.isWasDeath()) return;
        event.getEntity().getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC)
                .load(event.getOriginal().getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).save());
    }
}