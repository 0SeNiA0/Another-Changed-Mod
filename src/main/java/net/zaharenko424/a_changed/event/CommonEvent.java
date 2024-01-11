package net.zaharenko424.a_changed.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.commands.Transfur;
import net.zaharenko424.a_changed.commands.TransfurTolerance;
import net.zaharenko424.a_changed.commands.UnTransfur;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.network.PacketHandler;
import net.zaharenko424.a_changed.network.packets.ClientboundRemotePlayerTransfurUpdatePacket;
import net.zaharenko424.a_changed.network.packets.ClientboundTransfurToleranceUpdatePacket;
import net.zaharenko424.a_changed.registry.*;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Timer;
import java.util.TimerTask;

import static net.zaharenko424.a_changed.capability.TransfurCapability.CAPABILITY;
import static net.zaharenko424.a_changed.capability.TransfurCapability.NO_CAPABILITY_EXC;
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AChanged.MODID)
public class CommonEvent {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher=event.getDispatcher();
        Transfur.register(dispatcher);
        UnTransfur.register(dispatcher);
        TransfurTolerance.register(dispatcher);
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if(event.getEntity().level().isClientSide) return;
        ServerPlayer player= (ServerPlayer) event.getEntity();
        TransfurEvent.RECALCULATE_PROGRESS.accept(player);
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->player),new ClientboundTransfurToleranceUpdatePacket());
        TransfurEvent.updatePlayer(player);
        player.refreshDimensions();
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        Level level=event.getLevel();
        if(level.isClientSide) return;
        ServerPlayer player=(ServerPlayer) event.getEntity();
        ItemStack item=player.getItemInHand(event.getHand());
        if(!player.isCrouching()) return;
        Direction direction=player.getDirection().getOpposite();
        BlockPos pos=event.getPos();
        if(item.is(ItemTags.BOOKSHELF_BOOKS)){
            if(!level.getBlockState(pos).canBeReplaced()) {
                pos=pos.above();
                if(!level.getBlockState(pos).canBeReplaced()) return;
            }
            if(!level.setBlock(pos, BlockRegistry.BOOK_STACK.get().defaultBlockState(), 3)) return;
            level.getBlockEntity(pos, BlockEntityRegistry.BOOK_STACK_ENTITY.get()).ifPresent((entity->entity.addBook(item, (int) -player.yHeadRot,!player.isCreative())));
            denyEvent(event);
            return;
        }
        if(item.is(Items.PAPER)){
            if(!level.getBlockState(pos).canBeReplaced()) {
                pos=pos.relative(direction);
                if(!level.getBlockState(pos).canBeReplaced()) return;
            }
            if(!level.setBlock(pos, BlockRegistry.NOTE.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING,direction),3)) return;
            if(!player.isCreative()) player.getItemInHand(event.getHand()).shrink(1);
            denyEvent(event);
        }
    }

    private static void denyEvent(PlayerInteractEvent.RightClickBlock event){
        event.setUseBlock(Event.Result.DENY);
        event.setUseItem(Event.Result.DENY);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event){
        if(event.getEntity().level().isClientSide) return;
        LivingEntity entity=event.getEntity();
        if(DamageSources.checkTarget(entity)){
            entity.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).tick();
            if(entity.isInFluidType(FluidRegistry.DARK_LATEX_TYPE.get())){
                if(entity.hurt(DamageSources.transfur(entity,null),0.1f))
                    TransfurEvent.ADD_TRANSFUR_DEF.accept(entity, TransfurRegistry.DARK_LATEX_WOLF_M_TF.get(), 4f);
                return;
            }
            if(entity.isInFluidType(FluidRegistry.WHITE_LATEX_TYPE.get())){
                if(entity.hurt(DamageSources.transfur(entity,null),0.1f))
                    TransfurEvent.ADD_TRANSFUR_DEF.accept(entity, TransfurRegistry.PURE_WHITE_LATEX_WOLF_TF.get(), 4f);
                return;
            }
        }
        if(!entity.isInFluidType(FluidRegistry.LATEX_SOLVENT_TYPE.get())) return;
        if(entity instanceof AbstractLatexBeast||(entity instanceof Player player&&TransfurManager.isTransfurred(player)))
            entity.addEffect(new MobEffectInstance(MobEffectRegistry.LATEX_SOLVENT.get(),200));
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event){
        if(!(event.getObject() instanceof LivingEntity entity)) return;
        if(entity.getType().is(AChanged.TRANSFURRABLE_TAG)){
            event.addCapability(TransfurCapability.KEY,TransfurCapability.createProvider(entity));
        }
    }

    /**
     * Server event
     */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event){
        if(!(event.getTarget() instanceof Player remotePlayer)) return;
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) event.getEntity()),
                new ClientboundRemotePlayerTransfurUpdatePacket(remotePlayer.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC),remotePlayer.getUUID()));
    }

    /**
     * Server event
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if(!event.isWasDeath() || !event.getEntity().level().getGameRules().getBoolean(AChanged.KEEP_TRANSFUR)) return;
        ServerPlayer og=(ServerPlayer) event.getOriginal();
        ServerPlayer player=(ServerPlayer) event.getEntity();
        og.reviveCaps();
        player.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC)
                .load(og.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).save());
        boolean isBeingTransfurred=TransfurManager.isBeingTransfurred(og);
        og.invalidateCaps();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(isBeingTransfurred) TransfurEvent.UNTRANSFUR.accept(player); else TransfurEvent.updatePlayer(player);
            }
        },25);
    }
}