package net.zaharenko424.a_changed.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.GrabChanceData;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.block.blocks.Note;
import net.zaharenko424.a_changed.block.blocks.PileOfOranges;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.commands.*;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurToleranceSyncPacket;
import net.zaharenko424.a_changed.registry.*;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.TransfurToleranceData;
import net.zaharenko424.a_changed.util.CoveredWith;
import net.zaharenko424.a_changed.util.TransfurUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Timer;
import java.util.TimerTask;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = AChanged.MODID)
public class CommonEvent {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        GiveDNASample.register(dispatcher);
        LatexGrabChance.register(dispatcher);
        Transfur.register(dispatcher);
        UnTransfur.register(dispatcher);
        TransfurTolerance.register(dispatcher);
    }

    /**
     * Load saved data.
     */
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event){
        ServerLevel level = event.getServer().overworld();
        GrabChanceData.of(level);
        TransfurToleranceData.of(level);
    }

    /**
     * Send capability data to player
     */
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if(event.getEntity().level().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();

        PacketDistributor.sendToPlayer(player, new ClientboundTransfurToleranceSyncPacket());

        TransfurHandler handler = TransfurHandler.nonNullOf(player);
        handler.syncClients();
        if(handler.isBeingTransfurred()) PacketDistributor.sendToPlayer(player, new ClientboundOpenTransfurScreenPacket());

        if(handler.getSelectedAbility() != null) handler.getSelectedAbility().getAbilityData(player).syncClients();

        TransfurUtils.RECALCULATE_PROGRESS.accept(player);
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event){
        Player player = event.getEntity();
        if(player.level().isClientSide) return;

        TransfurHandler handler = TransfurHandler.nonNullOf(player);
        if(handler.getSelectedAbility() != null) handler.getSelectedAbility().deactivate(player);
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event){
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        if(TransfurManager.isHoldingEntity(player)) AbilityRegistry.GRAB_ABILITY.get().deactivate(player);
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
            if(above.getBlock() instanceof PileOfOranges){
                if(item.isEmpty()) {
                    event.setCancellationResult(above.useWithoutItem(level, player, event.getHitVec().withPosition(pos.above())));
                } else event.setCancellationResult(above.useItemOn(item, level, player, event.getHand(), event.getHitVec().withPosition(pos.above())).result());
                event.setCanceled(true);
                return;
            }
            return;
        }

        if(item.is(ItemTags.BOOKSHELF_BOOKS)){
            handleBookRMB(level, player, item, pos);
            denyEvent(event);
            return;
        }

        if(item.is(ItemRegistry.ORANGE_ITEM)){
            handleOrangeRMB(level, player, event.getHitVec().getLocation(), item, pos);
            denyEvent(event);
            return;
        }

        if(item.is(Items.PAPER)){
            handlePaperRMB(level, player, item, pos, direction);
            denyEvent(event);
        }
    }

    static void handleBookRMB(Level level, Player player, ItemStack item, BlockPos pos){
        if(!level.getBlockState(pos).canBeReplaced()) {
            pos = pos.above();
            if(!level.getBlockState(pos).canBeReplaced()) return;
        }
        if(!level.setBlock(pos, BlockRegistry.BOOK_STACK.get().defaultBlockState(), 3)) return;
        level.getBlockEntity(pos, BlockEntityRegistry.BOOK_STACK_ENTITY.get()).ifPresent((entity ->
                entity.addBook(item, (int) player.yHeadRot, !player.isCreative())));
    }

    static void handleOrangeRMB(Level level, Player player, Vec3 hitVec, ItemStack item, BlockPos pos){
        if(!level.getBlockState(pos).canBeReplaced()) {
            pos = pos.above();
            if(!level.getBlockState(pos).canBeReplaced()) return;
        }
        if(!level.setBlock(pos, BlockRegistry.PILE_OF_ORANGES.get().defaultBlockState(), 3)) return;
        level.getBlockEntity(pos, BlockEntityRegistry.PILE_OF_ORANGES_ENTITY.get()).ifPresent((entity -> {
            entity.addOrange(hitVec, (int) player.yHeadRot);
            if(!player.isCreative()) item.shrink(1);
        }));
    }

    static void handlePaperRMB(Level level, Player player, ItemStack item, BlockPos pos, Direction direction){
        if(!level.getBlockState(pos).canBeReplaced()) {
            pos = pos.relative(direction);
            if(!level.getBlockState(pos).canBeReplaced()) return;
        }
        if(!level.setBlock(pos, BlockRegistry.NOTE.get().defaultBlockState().setValue(Note.FACING, direction),3)) return;
        if(!player.isCreative()) item.shrink(1);
    }

    private static void denyEvent(PlayerInteractEvent.RightClickBlock event){
        event.setUseBlock(TriState.FALSE);
        event.setUseItem(TriState.FALSE);
        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBreak(BlockEvent.BreakEvent event){
        Level level = (Level) event.getLevel();
        Player player = event.getPlayer();

        if(level.isClientSide || player.isCreative() || !player.hasCorrectToolForDrops(event.getState(), level, event.getPos())) return;

        BlockPos pos = event.getPos();
        CoveredWith coveredWith = LatexCoveredData.of(level.getChunkAt(pos)).getCoveredWith(pos);

        if(coveredWith == CoveredWith.NOTHING) return;

        Block.popResource(level, pos, coveredWith == CoveredWith.DARK_LATEX ? ItemRegistry.DARK_LATEX_ITEM.toStack()
                : ItemRegistry.WHITE_LATEX_ITEM.toStack());
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event){
        if(event.getEntity().level().isClientSide || !(event.getEntity() instanceof LivingEntity entity)) return;

        TransfurHandler tfHandler = TransfurHandler.of(entity);
        if(tfHandler != null){
            tfHandler.tick();
            if(DamageSources.checkTarget(entity)){
                if(entity.isInFluidType(FluidRegistry.DARK_LATEX_TYPE.get())){
                    if(entity.hurt(DamageSources.transfur(entity.level(), null,null),0.1f))
                        tfHandler.addTransfurProgress(4f, TransfurRegistry.DARK_LATEX_WOLF_M_TF.get(), TransfurContext.ADD_PROGRESS_DEF);
                    return;
                }
                if(entity.isInFluidType(FluidRegistry.WHITE_LATEX_TYPE.get())){
                    if(entity.hurt(DamageSources.transfur(entity.level(), null,null),0.1f))
                        tfHandler.addTransfurProgress(4f, TransfurRegistry.PURE_WHITE_LATEX_WOLF_TF.get(), TransfurContext.ADD_PROGRESS_DEF);
                    return;
                }
            }
        }

        if(!entity.isInFluidType(FluidRegistry.LATEX_SOLVENT_TYPE.get())) return;
        if(entity instanceof AbstractLatexBeast || (entity instanceof Player player && TransfurManager.isTransfurred(player)))
            entity.addEffect(new MobEffectInstance(MobEffectRegistry.LATEX_SOLVENT,200));
    }

    /**
     * Reduce fall damage taken by cat transfurs
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event){
        LivingEntity entity = event.getEntity();
        if(event.getSource().is(DamageTypeTags.IS_FALL)){
            if((entity instanceof Player player && TransfurManager.isTransfurred(player) && TransfurManager.hasCatAbility(player))
                    || (entity instanceof AbstractLatexBeast latex && latex.transfurType.abilities.contains(AbilityRegistry.CAT_PASSIVE.get())))
                event.setNewDamage(event.getNewDamage() / 2);
        }
    }

    /**
     * Transfurs the entity if it died from tf attack.
     */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        LivingEntity entity = event.getEntity();
        if(entity.level().isClientSide) return;
        if(entity instanceof Player || !event.getSource().is(DamageSources.transfur) || !DamageSources.checkTarget(entity)) return;

        TransfurHandler handler = TransfurHandler.nonNullOf(entity);
        if(handler.getTransfurProgress() == 0 || handler.getTransfurType() == null) return;

        handler.transfur(handler.getTransfurType(), TransfurContext.TRANSFUR_DEF);
    }

    /**
     * Send data about remote player to other player
     */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event){
        if(!(event.getTarget() instanceof LivingEntity target)) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();

        TransfurHandler handler = TransfurHandler.of(target);
        if(handler != null) {
            handler.syncClient(player);
            if(handler.getSelectedAbility() != null) handler.getSelectedAbility().getAbilityData(target).syncClient(player);
        }
    }

    /**
     * Send data about latex coveredndess of the chunk.
     */
    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Sent event){
        LatexCoveredData data = LatexCoveredData.of(event.getChunk());
        if(data.isEmpty()) return;
        PacketDistributor.sendToPlayer(event.getPlayer(), data.getPacket(null));
    }

    /**
     * Clone capability data on respawn etc.
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if(!event.isWasDeath()) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                TransfurHandler handler = TransfurHandler.nonNullOf(player);
                handler.syncClients();
                if(handler.getSelectedAbility() != null) handler.getSelectedAbility().getAbilityData(player).syncClients();
            }
        },25);
    }
}