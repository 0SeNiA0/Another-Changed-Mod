package net.zaharenko424.a_changed.capability;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.common.capabilities.*;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.common.util.NonNullSupplier;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.network.PacketHandler;
import net.zaharenko424.a_changed.network.packets.grab.ClientboundGrabUpdatePacket;
import net.zaharenko424.a_changed.network.packets.grab.ClientboundRemoteGrabUpdatePacket;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrabCapability {

    public static final Capability<IGrabHandler> CAPABILITY = CapabilityManager.get(new Token());
    public static final ResourceLocation KEY = AChanged.resourceLoc("grab_capability");
    public static final NonNullSupplier<RuntimeException> NO_CAPABILITY_EXC = () -> new RuntimeException("Grab capability was expected but not found!");

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ICapabilityProvider createProvider(Player player) {
        return new Provider(player);
    }

    static class Token extends CapabilityToken<IGrabHandler>{}

    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        IGrabHandler handler;
        LazyOptional<IGrabHandler> optional;

        public Provider(Player player){
            handler = new GrabHandler(player);
            optional = LazyOptional.of(() -> handler);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY.orEmpty(cap, optional);
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            handler.load(nbt);
        }

        @Override
        public CompoundTag serializeNBT() {
            return handler.save();
        }
    }

    public static class GrabHandler implements IGrabHandler {

        private static final int grabCooldown = 200;
        private static final int grabDuration = 100;
        final Player player;
        LivingEntity grabbedEntity;
        Player grabbedBy;
        GrabMode mode = GrabMode.ASSIMILATE;
        boolean wantsToBeGrabbed = false;

        public GrabHandler(Player player){
            this.player = player;
        }

        @Override
        public LivingEntity getTarget() {
            return grabbedEntity;
        }

        @Override
        public void grab(@NotNull LivingEntity target) {
            grab(target, false);
        }

        private void grab(@NotNull LivingEntity target, boolean force){
            if(player.level().isClientSide){
                grabbedEntity = target;
                return;
            }

            if(!force && (!canGrab() || !mode.checkTarget(target))) return;
            grabbedEntity = target;
            if(target instanceof ServerPlayer player1) {
                target.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).setGrabbedBy(player);
                if(mode == GrabMode.FRIENDLY) {
                    player1.setGameMode(GameType.SPECTATOR);
                    player1.addEffect(new MobEffectInstance(MobEffectRegistry.FRIENDLY_GRAB.get(), -1, 0, false, false));
                }
            } else grabbedEntity.addTag("a_changed:grabbed");
            if(mode.givesDebuffToTarget) grabbedEntity.addEffect(new MobEffectInstance(MobEffectRegistry.GRABBED_DEBUFF.get(), grabDuration, 0, false, false));
            if(mode.givesDebuffToSelf) player.addEffect(new MobEffectInstance(MobEffectRegistry.HOLDING_DEBUFF.get(), -1, 0, false, false));
            updatePlayer();
        }

        @Override
        public void drop() {
            if(player.level().isClientSide){
                grabbedEntity = null;
                return;
            }

            if(grabbedEntity == null) return;
            if(grabbedEntity.isAlive()){
                if(grabbedEntity instanceof ServerPlayer player1) {
                    grabbedEntity.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC).setGrabbedBy(null);
                    if (mode == GrabMode.FRIENDLY) {
                        player1.setCamera(null);
                        player1.setGameMode(GameType.SURVIVAL);
                        player1.removeEffect(MobEffectRegistry.FRIENDLY_GRAB.get());
                    }
                } else grabbedEntity.removeTag("a_changed:grabbed");
                if(mode.givesDebuffToTarget) grabbedEntity.removeEffect(MobEffectRegistry.GRABBED_DEBUFF.get());
            }
            grabbedEntity = null;
            if(mode.givesDebuffToSelf) player.removeEffect(MobEffectRegistry.HOLDING_DEBUFF.get());
            player.addEffect(new MobEffectInstance(MobEffectRegistry.GRAB_COOLDOWN.get(), grabCooldown, 0, false, false));
            updatePlayer();
        }

        @Override
        public boolean canGrab() {
            return grabbedBy == null && !player.hasEffect(MobEffectRegistry.GRAB_COOLDOWN.get()) && grabbedEntity == null;
        }

        @Override
        public Player getGrabbedBy() {
            return grabbedBy;
        }

        @Override
        public void setGrabbedBy(@Nullable Player player) {
            grabbedBy = player;
            if(this.player.level().isClientSide) return;
            if(grabbedEntity != null) drop();
            else updatePlayer();
        }

        @Override
        public GrabMode grabMode() {
            return mode;
        }

        @Override
        public void setGrabMode(@NotNull GrabMode mode) {
            if(player.level().isClientSide){
                this.mode = mode;
                return;
            }

            if(((this.mode == GrabMode.FRIENDLY || mode == GrabMode.FRIENDLY) && this.mode != mode)
                    || (grabbedEntity instanceof Player player1 && !TransfurManager.wantsToBeGrabbed(player1) && !mode.givesDebuffToTarget)) drop();
            this.mode = mode;
            if(grabbedEntity != null) grab(grabbedEntity, true);
            else updatePlayer();
        }

        @Override
        public boolean wantsToBeGrabbed(){
            return wantsToBeGrabbed;
        }

        @Override
        public void setWantsToBeGrabbed(boolean wantsToBeGrabbed){
            if(player.level().isClientSide) {
                this.wantsToBeGrabbed = wantsToBeGrabbed;
                return;
            }

            if(this.wantsToBeGrabbed != wantsToBeGrabbed && !wantsToBeGrabbed && grabbedBy != null){
                IGrabHandler handler = grabbedBy.getCapability(CAPABILITY).orElseThrow(NO_CAPABILITY_EXC);
                if(!handler.grabMode().givesDebuffToTarget) handler.drop();
            }
            this.wantsToBeGrabbed = wantsToBeGrabbed;
            updatePlayer();
        }

        @Override
        public void load(@NotNull CompoundTag tag) {
            mode = GrabMode.valueOf(tag.getString("mode"));
            wantsToBeGrabbed = tag.getBoolean("wantToBeGrabbed");
        }

        @Override
        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putString("mode", mode.toString());
            tag.putBoolean("wantToBeGrabbed", wantsToBeGrabbed);
            return tag;
        }

        @Override
        public void tick() {
            if(grabbedBy != null || grabbedEntity == null) return;
            if(!grabbedEntity.isAlive()) {
                if(grabbedEntity.getRemovalReason() == Entity.RemovalReason.UNLOADED_WITH_PLAYER)
                    player.displayClientMessage(Component.translatable("message.a_changed.grabbed_player_left"), true);
                else player.displayClientMessage(Component.translatable("message.a_changed.grabbed_entity_died"), true);
                drop();
                return;
            }
            if(mode == GrabMode.FRIENDLY){
                ((ServerPlayer)grabbedEntity).setCamera(player);
                return;
            }
            hold();
            if(mode.givesDebuffToTarget && !grabbedEntity.hasEffect(MobEffectRegistry.GRABBED_DEBUFF.get())) {
                if(mode == GrabMode.ASSIMILATE) {
                    grabbedEntity.hurt(DamageSources.assimilation(grabbedEntity, player), Integer.MAX_VALUE);
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.ASSIMILATION_BUFF.get(), 6000, 0, false, false));
                    player.getFoodData().eat(6, 1);
                } else if(mode == GrabMode.REPLICATE)
                    TransfurEvent.TRANSFUR_TF.accept(grabbedEntity, TransfurManager.getTransfurType(player));
                drop();
            }
        }

        private void hold(){
            float yaw = player.getYHeadRot();
            float x = (float) ((-Mth.sin(Mth.DEG_TO_RAD * yaw) * 1.2) + player.getX());
            float z = (float) ((Mth.cos(Mth.DEG_TO_RAD * yaw) * 1.2) + player.getZ());


            grabbedEntity.teleportTo(x, player.getY(), z);
            grabbedEntity.lookAt(EntityAnchorArgument.Anchor.EYES, player.getEyePosition());
            grabbedEntity.setDeltaMovement(grabbedEntity.getDeltaMovement().with(Direction.Axis.Y, 0));
        }

        public void updatePlayer(){
            if(player.level().isClientSide) return;
            int id0 = grabbedEntity != null ? grabbedEntity.getId() : -1;
            int id1 = grabbedBy != null ? grabbedBy.getId() : -1;

            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) player),
                    new ClientboundGrabUpdatePacket(id0, id1, mode, wantsToBeGrabbed));
            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(()-> player),
                    new ClientboundRemoteGrabUpdatePacket(player.getUUID(), id0, id1, mode, wantsToBeGrabbed));
        }

        @Override
        public void updateRemotePlayer(@NotNull ServerPlayer packetReceiver) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> packetReceiver),
                    new ClientboundRemoteGrabUpdatePacket(player.getUUID(), grabbedEntity != null ? grabbedEntity.getId() : -1,
                            grabbedBy != null ? grabbedBy.getId() : -1, mode, wantsToBeGrabbed));
        }
    }
}