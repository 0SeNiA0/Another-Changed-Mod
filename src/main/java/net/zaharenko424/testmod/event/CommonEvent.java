package net.zaharenko424.testmod.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.TransfurDamageSource;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.capability.TransfurCapability;
import net.zaharenko424.testmod.commands.Transfur;
import net.zaharenko424.testmod.commands.TransfurTolerance;
import net.zaharenko424.testmod.commands.UnTransfur;
import net.zaharenko424.testmod.entity.AbstractLatexBeast;
import net.zaharenko424.testmod.network.PacketHandler;
import net.zaharenko424.testmod.network.packets.ClientboundRemotePlayerTransfurUpdatePacket;
import net.zaharenko424.testmod.registry.BlockEntityRegistry;
import net.zaharenko424.testmod.registry.BlockRegistry;
import net.zaharenko424.testmod.registry.FluidRegistry;
import net.zaharenko424.testmod.registry.TransfurRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

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
        ServerPlayer player= (ServerPlayer) event.getEntity();
        TransfurManager.updatePlayer(player);
        player.refreshDimensions();
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.@NotNull RightClickBlock event){
        Level level=event.getLevel();
        if(level.isClientSide) return;
        ServerPlayer player=(ServerPlayer) event.getEntity();
        ItemStack item=player.getItemInHand(event.getHand());
        if(item.is(ItemTags.BOOKSHELF_BOOKS)&&player.isCrouching()){
            BlockPos pos=event.getPos().above();
            if(!level.getBlockState(pos).canBeReplaced()) return;
            if(!level.setBlock(pos, BlockRegistry.BOOK_STACK.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING,player.getDirection().getOpposite()), 3)) return;
            level.getBlockEntity(pos, BlockEntityRegistry.BOOK_STACK_ENTITY.get()).ifPresent((entity->entity.addBook(item,!player.isCreative())));
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.@NotNull LivingTickEvent event){
        LivingEntity entity=event.getEntity();
        if(TransfurDamageSource.checkTarget(entity)){
            if(entity.isInFluidType(FluidRegistry.WHITE_LATEX_TYPE.get())){
                if(entity.hurt(TransfurDamageSource.transfur(entity,null),0.1f)) TransfurManager.addTransfurProgress(entity,4, TransfurRegistry.WHITE_LATEX_WOLF_M_TF.get().location);
                return;
            }
            if(entity.isInFluidType(FluidRegistry.DARK_LATEX_TYPE.get())){
                if(entity.hurt(TransfurDamageSource.transfur(entity,null),0.1f)) TransfurManager.addTransfurProgress(entity,4,TransfurRegistry.DARK_LATEX_WOLF_M_TF.get().location);
                return;
            }
        }
        if(!entity.isInFluidType(FluidRegistry.LATEX_SOLVENT_TYPE.get())) return;
        if(entity instanceof AbstractLatexBeast||(entity instanceof Player player&&TransfurManager.isTransfurred(player)))
            entity.addEffect(new MobEffectInstance(TestMod.LATEX_SOLVENT.get(),200));
    }

    @SubscribeEvent
    public static void onAttachCapabilities(@NotNull AttachCapabilitiesEvent<Entity> event){
        if(!(event.getObject() instanceof LivingEntity entity)) return;
        if(entity instanceof Player||entity.getType().is(TestMod.TRANSFURRABLE_TAG)){
            event.addCapability(TransfurCapability.KEY,TransfurCapability.createProvider(entity));
        }
    }

    /**
     * Server event
     */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.@NotNull StartTracking event){
        if(!(event.getTarget() instanceof Player remotePlayer)) return;
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) event.getEntity()),
                new ClientboundRemotePlayerTransfurUpdatePacket(remotePlayer.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC),remotePlayer.getUUID()));
    }

    /**
     * Server event
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.@NotNull Clone event){
        if(!event.isWasDeath()) return;
        ServerPlayer og=(ServerPlayer) event.getOriginal();
        ServerPlayer player=(ServerPlayer) event.getEntity();
        player.respawn();
        og.reviveCaps();
        player.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC)
                .load(og.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).save());
        og.invalidateCaps();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                TransfurManager.updatePlayer(player);
            }
        },25);
    }
}