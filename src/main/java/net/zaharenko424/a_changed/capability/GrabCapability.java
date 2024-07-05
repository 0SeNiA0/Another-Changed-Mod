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
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.network.packets.grab.ClientboundGrabSyncPacket;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class GrabCapability {

    public static final ResourceLocation KEY = AChanged.resourceLoc("grab_capability");
    public static final EntityCapability<IGrabHandler, Void> CAPABILITY = EntityCapability.createVoid(KEY, IGrabHandler.class);
    public static final Supplier<RuntimeException> NO_CAPABILITY_EXC = ()-> new RuntimeException("Grab capability was expected but not found!");

    public static final Serializer SERIALIZER = new Serializer();

    public static @Nullable IGrabHandler of(@NotNull LivingEntity player){
        return player.getCapability(CAPABILITY);
    }

    public static @NotNull IGrabHandler nonNullOf(@NotNull LivingEntity player){
        return Utils.nonNullOrThrow(player.getCapability(CAPABILITY), NO_CAPABILITY_EXC.get());
    }

    public static class GrabHandler implements IGrabHandler {

        private static final int grabCooldown = 200;
        private static final int grabDuration = 100;
        private final Player holder;
        LivingEntity grabbedEntity;
        Player grabbedBy;
        GrabMode mode = GrabMode.ASSIMILATE;
        boolean wantsToBeGrabbed = false;

        public GrabHandler(IAttachmentHolder holder){
            if(!(holder instanceof Player player1))
                throw new IllegalStateException("Tried to create GrabHandler for unsupported holder: " + holder);
            this.holder = player1;
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
            if(holder.level().isClientSide){

                grabbedEntity = target;
                return;
            }

            if(!force && (!canGrab() || !mode.checkTarget(target))) return;
            grabbedEntity = target;
            if(target instanceof ServerPlayer player1) {
                nonNullOf(player1).setGrabbedBy(holder);
                if(mode == GrabMode.FRIENDLY) {
                    player1.setGameMode(GameType.SPECTATOR);
                    player1.addEffect(new MobEffectInstance(MobEffectRegistry.FRIENDLY_GRAB.get(), -1, 0, false, false));
                }
            } else grabbedEntity.addTag("a_changed:grabbed");
            if(mode.givesDebuffToTarget) grabbedEntity.addEffect(new MobEffectInstance(MobEffectRegistry.GRABBED_DEBUFF.get(), grabDuration, 0, false, false));
            if(mode.givesDebuffToSelf) holder.addEffect(new MobEffectInstance(MobEffectRegistry.HOLDING_DEBUFF.get(), -1, 0, false, false));
            syncClients();
        }

        @Override
        public void drop() {
            if(holder.level().isClientSide){
                grabbedEntity = null;
                return;
            }

            if(grabbedEntity == null) return;
            if(grabbedEntity.isAlive()){
                if(grabbedEntity instanceof ServerPlayer player1) {
                    nonNullOf(player1).setGrabbedBy(null);
                    if (mode == GrabMode.FRIENDLY) {
                        player1.setCamera(null);
                        player1.setGameMode(GameType.SURVIVAL);
                        player1.removeEffect(MobEffectRegistry.FRIENDLY_GRAB.get());
                    }
                } else grabbedEntity.removeTag("a_changed:grabbed");
                if(mode.givesDebuffToTarget) grabbedEntity.removeEffect(MobEffectRegistry.GRABBED_DEBUFF.get());
            }
            grabbedEntity = null;
            if(mode.givesDebuffToSelf) holder.removeEffect(MobEffectRegistry.HOLDING_DEBUFF.get());
            holder.addEffect(new MobEffectInstance(MobEffectRegistry.GRAB_COOLDOWN.get(), grabCooldown, 0, false, false));
            syncClients();
        }

        @Override
        public boolean canGrab() {
            return grabbedBy == null && !holder.hasEffect(MobEffectRegistry.GRAB_COOLDOWN.get()) && grabbedEntity == null;
        }

        @Override
        public LivingEntity getGrabbedBy() {
            return grabbedBy;
        }

        @Override
        public void setGrabbedBy(@Nullable Player player) {
            grabbedBy = player;
            if(this.holder.level().isClientSide) return;
            if(grabbedEntity != null) drop();
            else syncClients();
        }

        @Override
        public GrabMode grabMode() {
            return mode;
        }

        @Override
        public void setGrabMode(@NotNull GrabMode mode) {
            if(holder.level().isClientSide){
                this.mode = mode;
                return;
            }

            if(((this.mode == GrabMode.FRIENDLY || mode == GrabMode.FRIENDLY) && this.mode != mode)
                    || (grabbedEntity instanceof Player player1 && !TransfurManager.wantsToBeGrabbed(player1) && !mode.givesDebuffToTarget)) drop();
            this.mode = mode;
            if(grabbedEntity != null) grab(grabbedEntity, true);
            else syncClients();
        }

        @Override
        public boolean wantsToBeGrabbed(){
            return wantsToBeGrabbed;
        }

        @Override
        public void setWantsToBeGrabbed(boolean wantsToBeGrabbed){
            if(holder.level().isClientSide) {
                this.wantsToBeGrabbed = wantsToBeGrabbed;
                return;
            }

            if(this.wantsToBeGrabbed != wantsToBeGrabbed && !wantsToBeGrabbed && grabbedBy != null){
                IGrabHandler handler = nonNullOf(grabbedBy);
                if(!handler.grabMode().givesDebuffToTarget) handler.drop();
            }
            this.wantsToBeGrabbed = wantsToBeGrabbed;
            syncClients();
        }

        @Override
        public void tick() {
            if(grabbedBy != null || grabbedEntity == null) return;
            if(!grabbedEntity.isAlive()) {
                if(grabbedEntity.getRemovalReason() == Entity.RemovalReason.UNLOADED_WITH_PLAYER)
                    holder.displayClientMessage(Component.translatable("message.a_changed.grabbed_player_left"), true);
                else holder.displayClientMessage(Component.translatable("message.a_changed.grabbed_entity_died"), true);
                drop();
                return;
            }
            if(mode == GrabMode.FRIENDLY){
                ((ServerPlayer)grabbedEntity).setCamera(holder);
                return;
            }
            hold();
            if(mode.givesDebuffToTarget && !grabbedEntity.hasEffect(MobEffectRegistry.GRABBED_DEBUFF.get())) {
                if(mode == GrabMode.ASSIMILATE) {
                    grabbedEntity.hurt(DamageSources.assimilation(holder, null), Integer.MAX_VALUE);
                    holder.addEffect(new MobEffectInstance(MobEffectRegistry.ASSIMILATION_BUFF.get(), 6000, 0, false, false));
                    holder.getFoodData().eat(6, 1);
                } else if(mode == GrabMode.REPLICATE) {
                    ITransfurHandler handler = TransfurCapability.of(grabbedEntity);
                    if(handler != null) handler.transfur(TransfurManager.getTransfurType(holder), TransfurContext.TRANSFUR_TF);
                }
                drop();
            }
        }

        private void hold(){
            float yaw = holder.getYHeadRot();
            float x = (float) ((-Mth.sin(Mth.DEG_TO_RAD * yaw) * 1.2) + holder.getX());
            float z = (float) ((Mth.cos(Mth.DEG_TO_RAD * yaw) * 1.2) + holder.getZ());


            grabbedEntity.teleportTo(x, holder.getY(), z);
            grabbedEntity.lookAt(EntityAnchorArgument.Anchor.EYES, holder.getEyePosition());
            grabbedEntity.setDeltaMovement(grabbedEntity.getDeltaMovement().with(Direction.Axis.Y, 0));
        }

        public void syncClients(){
            if(holder.level().isClientSide) return;
            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(holder).send(packet());
        }

        @Override
        public void syncClient(@NotNull ServerPlayer packetReceiver) {
            PacketDistributor.PLAYER.with(packetReceiver).send(packet());
        }

        ClientboundGrabSyncPacket packet(){
            return new ClientboundGrabSyncPacket(holder.getId(), grabbedEntity != null ? grabbedEntity.getId() : -1,
                    grabbedBy != null ? grabbedBy.getId() : -1, mode, wantsToBeGrabbed);
        }
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, GrabHandler> {

        private Serializer() {}

        @Override
        public @NotNull GrabHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag) {
            GrabHandler handler = new GrabHandler(holder);
            handler.mode = GrabMode.valueOf(tag.getString("mode"));
            handler.wantsToBeGrabbed = tag.getBoolean("wantToBeGrabbed");
            return handler;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull GrabHandler attachment) {
            CompoundTag tag = new CompoundTag();
            tag.putString("mode", attachment.mode.toString());
            tag.putBoolean("wantToBeGrabbed", attachment.wantsToBeGrabbed);
            return tag;
        }
    }
}