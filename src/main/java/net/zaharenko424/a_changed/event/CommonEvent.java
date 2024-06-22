package net.zaharenko424.a_changed.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.block.blocks.Note;
import net.zaharenko424.a_changed.block.blocks.PileOfOranges;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.IGrabHandler;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.commands.GiveDNASample;
import net.zaharenko424.a_changed.commands.Transfur;
import net.zaharenko424.a_changed.commands.TransfurTolerance;
import net.zaharenko424.a_changed.commands.UnTransfur;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.item.Chisel;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundPlayerTransfurSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurToleranceSyncPacket;
import net.zaharenko424.a_changed.registry.*;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.TransfurToleranceData;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractLatexCat;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Timer;
import java.util.TimerTask;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AChanged.MODID)
public class CommonEvent {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        GiveDNASample.register(dispatcher);
        Transfur.register(dispatcher);
        UnTransfur.register(dispatcher);
        TransfurTolerance.register(dispatcher);
    }

    /**
     * Load transfur tolerance value
     */
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event){
        event.getServer().overworld().getDataStorage().computeIfAbsent(TransfurToleranceData.FACTORY, "transfur_tolerance");
    }

    /**
     * Send capability data to player
     */
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if(event.getEntity().level().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        PacketDistributor.PLAYER.with(player).send(new ClientboundTransfurToleranceSyncPacket());
        TransfurEvent.updatePlayer(player);
        player.refreshDimensions();
        if(TransfurManager.isBeingTransfurred(player)) PacketDistributor.PLAYER.with(player).send(new ClientboundOpenTransfurScreenPacket());
        GrabCapability.nonNullOf(player).updatePlayer();
        TransfurEvent.RECALCULATE_PROGRESS.accept(player);
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event){
        Player player = event.getEntity();
        if(player.level().isClientSide) return;
        IGrabHandler handler = GrabCapability.of(player);
        if(handler == null) return;
        if(handler.getTarget() != null) handler.drop();
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event){
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        if(TransfurManager.isHoldingEntity(player)) GrabCapability.nonNullOf(player).drop();
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        Level level = event.getLevel();
        if(level.isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        ItemStack item = player.getItemInHand(event.getHand());
        Direction direction = player.getDirection().getOpposite();
        BlockPos pos = event.getPos();
        if(!player.isCrouching()) {
            BlockState above = level.getBlockState(pos.above());
            if(above.getBlock() instanceof PileOfOranges oranges){
                event.setCancellationResult(oranges.use(above, level, pos.above(), player, event.getHand(), event.getHitVec()));
                event.setCanceled(true);
                return;
            }
            return;
        }
        if(item.is(ItemTags.BOOKSHELF_BOOKS)){
            if(!level.getBlockState(pos).canBeReplaced()) {
                pos = pos.above();
                if(!level.getBlockState(pos).canBeReplaced()) return;
            }
            if(!level.setBlock(pos, BlockRegistry.BOOK_STACK.get().defaultBlockState(), 3)) return;
            level.getBlockEntity(pos, BlockEntityRegistry.BOOK_STACK_ENTITY.get()).ifPresent((entity ->
                    entity.addBook(item, (int) player.yHeadRot, !player.isCreative())));
            denyEvent(event);
            return;
        }
        if(item.is(ItemRegistry.ORANGE_ITEM)){
            if(!level.getBlockState(pos).canBeReplaced()) {
                pos = pos.above();
                if(!level.getBlockState(pos).canBeReplaced()) return;
            }
            if(!level.setBlock(pos, BlockRegistry.PILE_OF_ORANGES.get().defaultBlockState(), 3)) return;
            level.getBlockEntity(pos, BlockEntityRegistry.PILE_OF_ORANGES_ENTITY.get()).ifPresent((entity -> {
                entity.addOrange(event.getHitVec().getLocation(), (int) player.yHeadRot);
                if(!player.isCreative()) item.shrink(1);
            }));
            denyEvent(event);
            return;
        }
        if(item.is(Items.PAPER)){
            if(!level.getBlockState(pos).canBeReplaced()) {
                pos = pos.relative(direction);
                if(!level.getBlockState(pos).canBeReplaced()) return;
            }
            if(!level.setBlock(pos, BlockRegistry.NOTE.get().defaultBlockState().setValue(Note.FACING, direction),3)) return;
            if(!player.isCreative()) player.getItemInHand(event.getHand()).shrink(1);
            denyEvent(event);
        }
    }

    private static void denyEvent(PlayerInteractEvent.RightClickBlock event){
        event.setUseBlock(Event.Result.DENY);
        event.setUseItem(Event.Result.DENY);
        event.setCanceled(true);
    }

    /**
     * Handle chisel in survival
     */
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        if(event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        Player player = event.getEntity();
        ItemStack item = player.getMainHandItem();
        if(player.level().isClientSide || player.isCreative() || !(item.getItem() instanceof Chisel chisel)) return;
        Level level = player.level();
        BlockPos pos = event.getPos();
        chisel.canAttackBlock(level.getBlockState(pos), level, pos, player);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event){
        if(event.getEntity().level().isClientSide) return;
        LivingEntity entity = event.getEntity();

        IGrabHandler grabHandler = GrabCapability.of(entity);
        if(grabHandler != null) grabHandler.tick();

        ITransfurHandler transfurHandler = TransfurCapability.of(entity);
        if(transfurHandler != null){
            transfurHandler.tick();
            if(!(entity instanceof Player player) || (!TransfurManager.isTransfurred(player) && !TransfurManager.isBeingTransfurred(player))){
                if(entity.isInFluidType(FluidRegistry.DARK_LATEX_TYPE.get())){
                    if(entity.hurt(DamageSources.transfur(entity.level(), null,null),0.1f))
                        TransfurEvent.ADD_TRANSFUR_DEF.accept(entity, TransfurRegistry.DARK_LATEX_WOLF_M_TF.get(), 4f);
                    return;
                }
                if(entity.isInFluidType(FluidRegistry.WHITE_LATEX_TYPE.get())){
                    if(entity.hurt(DamageSources.transfur(entity.level(), null,null),0.1f))
                        TransfurEvent.ADD_TRANSFUR_DEF.accept(entity, TransfurRegistry.PURE_WHITE_LATEX_WOLF_TF.get(), 4f);
                    return;
                }
            }
        }
        if(!entity.isInFluidType(FluidRegistry.LATEX_SOLVENT_TYPE.get())) return;
        if(entity instanceof AbstractLatexBeast || (entity instanceof Player player && TransfurManager.isTransfurred(player)))
            entity.addEffect(new MobEffectInstance(MobEffectRegistry.LATEX_SOLVENT.get(),200));
    }

    /**
     * Reduce fall damage taken by cat transfurs
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        LivingEntity entity = event.getEntity();
        if(event.getSource().is(DamageTypeTags.IS_FALL)){
            if((entity instanceof Player player && TransfurManager.isTransfurred(player)
                        && TransfurManager.getTransfurType(player) instanceof AbstractLatexCat)
                    || (entity instanceof AbstractLatexBeast latex && latex.transfurType instanceof AbstractLatexCat))
                event.setAmount(event.getAmount() / 2);
        }
    }

    /**
     * Send data about remote player to other player
     */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event){
        if(!(event.getTarget() instanceof Player remotePlayer)) return;//TODO check for entities with capabilities to sync!!
        ServerPlayer player = (ServerPlayer) event.getEntity();

        PacketDistributor.PLAYER.with(player).send(
                new ClientboundPlayerTransfurSyncPacket(remotePlayer.getId(), TransfurCapability.nonNullOf(remotePlayer)));
        GrabCapability.nonNullOf(remotePlayer).updateRemotePlayer(player);
    }

    /**
     * Clone capability data on respawn etc.
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if(!event.isWasDeath()) return;
        boolean keepTf = event.getEntity().level().getGameRules().getBoolean(AChanged.KEEP_TRANSFUR);
        ServerPlayer og = (ServerPlayer) event.getOriginal();
        ServerPlayer player = (ServerPlayer) event.getEntity();

        if(keepTf) TransfurCapability.nonNullOf(player).load(TransfurCapability.nonNullOf(og).save());
        GrabCapability.nonNullOf(player).load(GrabCapability.nonNullOf(og).save());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(keepTf) {
                    if(!TransfurManager.isTransfurred(player)) TransfurEvent.UNTRANSFUR_SILENT.accept(player);
                    else TransfurEvent.updatePlayer(player);
                }
                GrabCapability.nonNullOf(player).updatePlayer();
            }
        },25);
    }
}