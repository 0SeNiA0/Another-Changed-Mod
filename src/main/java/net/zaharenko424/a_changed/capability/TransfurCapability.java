package net.zaharenko424.a_changed.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.event.custom.AddTransfurProgressEvent;
import net.zaharenko424.a_changed.event.custom.TransfurredEvent;
import net.zaharenko424.a_changed.event.custom.UnTransfurredEvent;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurSyncPacket;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.TransfurResult;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.TransfurUtils;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.*;

public class TransfurCapability {

    public static final ResourceLocation KEY = AChanged.resourceLoc("transfur_capability");
    public static final EntityCapability<ITransfurHandler, Void> CAPABILITY = EntityCapability.createVoid(KEY, ITransfurHandler.class);
    public static final Supplier<RuntimeException> NO_CAPABILITY_EXC = ()-> new RuntimeException("Transfur capability was expected but not found!");

    public static final Serializer SERIALIZER = new Serializer();
    public static final IAttachmentCopyHandler<TransfurHandler> COPY_HANDLER = (holder, attachment) -> {
        if(((LivingEntity)holder).level().getGameRules().getBoolean(AChanged.KEEP_TRANSFUR) && attachment.isTransfurred()) {
            CompoundTag tag = SERIALIZER.write(attachment);
            return tag != null ? SERIALIZER.read(holder, tag) : null;
        }
        return null;
    };

    public static @Nullable ITransfurHandler of(@NotNull LivingEntity entity){
        return entity.getCapability(CAPABILITY);
    }

    public static @NotNull ITransfurHandler nonNullOf(@NotNull LivingEntity entity){
        return Utils.nonNullOrThrow(entity.getCapability(CAPABILITY), NO_CAPABILITY_EXC.get());
    }

    public static class TransfurHandler implements ITransfurHandler {

        private final LivingEntity holder;

        //Synced data
        float transfurProgress = 0;
        TransfurType transfurType = null;
        boolean isTransfurred = false;

        //Synced, runtime only (used to tell client to remove model)
        TransfurType transfurTypeO = null;

        static final int ticksUntilTFProgressDecrease = 200;
        static final int ticksBetweenTFProgressDecrease = 20;
        int i0 = 0;

        //Server only data
        boolean isBeingTransfurred = false;
        static final int ticksUntilDeathTF = 400;
        int beingTransfurredTimer;

        public TransfurHandler(IAttachmentHolder holder){
            if(!(holder instanceof LivingEntity living) || !living.getType().is(AChanged.TRANSFURRABLE_TAG))
                throw new IllegalStateException("Tried to create TransfurHandler for unsupported holder: " + holder);
            this.holder = living;
        }

        @Override
        public float getTransfurProgress() {
            return transfurProgress;
        }

        @Override
        public void addTransfurProgress(float amount, @NotNull TransfurType transfurType, @NotNull TransfurContext context) {
            if(holder.level().isClientSide) return;
            if(isBeingTransfurred() || isTransfurred()) return;

            AddTransfurProgressEvent event = new AddTransfurProgressEvent(holder, transfurType, amount);
            NeoForge.EVENT_BUS.post(event);
            if(event.isCanceled()) return;

            float progress;
            if(event.getProgressToAdd() > 0) {
                progress = event.getProgressToAdd();
            } else {
                if (context.checkResistance()) {
                    float resistance = (float) holder.getAttributeValue(LATEX_RESISTANCE);
                    amount *= 1 - resistance;
                }
                progress = getTransfurProgress() + amount;
            }

            if(progress >= TRANSFUR_TOLERANCE) {
                transfur(transfurType, context);
                return;
            }

            i0 = ticksUntilTFProgressDecrease;
            transfurProgress = progress;
            this.transfurType = transfurType;

            syncClients();
        }

        @Override
        public @Nullable TransfurType getTransfurType() {
            return transfurType;
        }

        @Override
        public void setTransfurType(@NotNull TransfurType transfurType) {
            this.transfurType = transfurType;
        }

        @Override
        public boolean isTransfurred() {
            return isTransfurred && transfurType != null;
        }

        @Override
        public void transfur(@NotNull TransfurType transfurType, @NotNull TransfurContext context) {
            Level level = holder.level();
            if(level.isClientSide) return;

            SoundEvent onTransfurSound = context.onTransfurSound();

            if(!(holder instanceof ServerPlayer player)){
                AbstractLatexBeast latexBeast = TransfurUtils.spawnLatex(transfurType, (ServerLevel) level, holder.blockPosition());
                latexBeast.copyEquipment(holder);
                if(onTransfurSound != null) holder.playSound(onTransfurSound);
                holder.discard();

                NeoForge.EVENT_BUS.post(new TransfurredEvent(holder, latexBeast, transfurType));
                return;
            }

            if(isTransfurred() && !player.isCreative()) return;
            if(onTransfurSound != null) level.playSound(null, player, onTransfurSound, SoundSource.PLAYERS,1,1);

            TransfurResult result = context.result();
            if(player.isCreative() || player.isSpectator() || result == TransfurResult.TRANSFUR){
                actuallyTransfur(transfurType);
                return;
            }
            switch (result != null ? result
                    : level.getGameRules().getBoolean(TRANSFUR_IS_DEATH) ? TransfurResult.DEATH
                    : level.getGameRules().getBoolean(CHOOSE_TF_OR_DIE) ? TransfurResult.PROMPT
                    : TransfurResult.TRANSFUR){
                case DEATH -> {
                    player.setInvulnerable(false);
                    player.hurt(DamageSources.transfur(null, Objects.requireNonNullElse(player.getLastHurtByMob(), player)), Float.MAX_VALUE);
                    AbstractLatexBeast latexBeast = TransfurUtils.spawnLatex(transfurType, (ServerLevel) level, player.blockPosition());

                    NeoForge.EVENT_BUS.post(new TransfurredEvent(latexBeast, latexBeast, transfurType));
                }
                case PROMPT -> {
                    setBeingTransfurred(true);
                    this.transfurType = transfurType;
                    PacketDistributor.PLAYER.with(player).send(new ClientboundOpenTransfurScreenPacket());
                }
                case TRANSFUR -> actuallyTransfur(transfurType);
            }
        }

        private void actuallyTransfur(TransfurType transfurType){
            setBeingTransfurred(false);

            if(isTransfurred()){
                TransfurUtils.removeModifiers(holder, this.transfurType.modifiers);
                this.transfurType.onUnTransfur(holder);
                transfurTypeO = this.transfurType;//TMP needed to tell client what player model to remove
            }

            loadSyncedData(TRANSFUR_TOLERANCE, true, transfurType);
            TransfurUtils.addModifiers(holder, transfurType);
            transfurType.onTransfur(holder);

            syncClients();

            NeoForge.EVENT_BUS.post(new TransfurredEvent(holder, null, transfurType));
        }

        @Override
        public void unTransfur(@NotNull TransfurContext context) {
            if(holder.level().isClientSide) return;

            setBeingTransfurred(false);

            transfurTypeO = transfurType;//TMP needed to tell client what player model to remove
            if(isTransfurred()) {
                TransfurUtils.removeModifiers(holder, transfurType.modifiers);
                transfurType.onUnTransfur(holder);
            }

            loadSyncedData(0, false, null);
            syncClients();

            if(context.sound() != null)
                holder.level().playSound(null, holder.blockPosition(), context.sound(), SoundSource.PLAYERS);

            if(transfurTypeO != null) NeoForge.EVENT_BUS.post(new UnTransfurredEvent((Player) holder, transfurTypeO));
        }

        @Override
        public boolean isBeingTransfurred() {
            return isBeingTransfurred;
        }

        @Override
        public void setBeingTransfurred(boolean isBeingTransfurred) {
            if(this.isBeingTransfurred != isBeingTransfurred){
                beingTransfurredTimer = isBeingTransfurred ? ticksUntilDeathTF : 0;
            }
            this.isBeingTransfurred = isBeingTransfurred;
        }

        @Override
        public void tick() {
            if(isTransfurred() || transfurProgress <= 0) return;

            if(isBeingTransfurred){
                if(beingTransfurredTimer > 0){
                    beingTransfurredTimer--;
                } else transfur(transfurType, TransfurContext.TRANSFUR_DEATH);
                return;
            }

            if(i0 > 0) {
                i0--;
                return;
            }
            if(holder.tickCount % ticksBetweenTFProgressDecrease != 0) return;
            transfurProgress = Math.max(0, transfurProgress - 1);

            syncClients();
        }

        public void syncClient(ServerPlayer packetReceiver) {
            PacketDistributor.PLAYER.with(packetReceiver).send(packet());
        }

        public void syncClients(){
            holder.refreshDimensions();
            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(holder).send(packet());
        }

        ClientboundTransfurSyncPacket packet(){
            return new ClientboundTransfurSyncPacket(holder.getId(), transfurProgress, isTransfurred, transfurType,
                    holder instanceof Player ? transfurTypeO : null);
        }

        @ApiStatus.Internal
        public void loadSyncedData(float transfurProgress, boolean isTransfurred, TransfurType transfurType){
            this.transfurProgress = transfurProgress;
            this.isTransfurred = isTransfurred;
            this.transfurType = transfurType;
        }
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, TransfurHandler> {

        private Serializer(){}

        @Override
        public @NotNull TransfurHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag) {
            TransfurHandler handler = new TransfurHandler(holder);

            handler.loadSyncedData(tag.getFloat(TRANSFUR_PROGRESS_KEY), tag.getBoolean(TRANSFURRED_KEY),
                    TransfurManager.getTransfurType(new ResourceLocation(tag.getString(TRANSFUR_TYPE_KEY))));

            if(holder instanceof Player) handler.setBeingTransfurred(tag.getBoolean(BEING_TRANSFURRED_KEY));

            if(handler.isTransfurred()) TransfurUtils.addModifiers((LivingEntity) holder, handler.transfurType);
            return handler;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull TransfurHandler attachment) {
            CompoundTag tag = new CompoundTag();
            tag.putFloat(TRANSFUR_PROGRESS_KEY, attachment.transfurProgress);

            TransfurType transfurType = attachment.transfurType;
            if(transfurType != null) {
                tag.putBoolean(TRANSFURRED_KEY, attachment.isTransfurred);
                tag.putString(TRANSFUR_TYPE_KEY, transfurType.id.toString());
            }

            if(attachment.holder instanceof Player) tag.putBoolean(BEING_TRANSFURRED_KEY, attachment.isBeingTransfurred);
            return tag;
        }
    }
}