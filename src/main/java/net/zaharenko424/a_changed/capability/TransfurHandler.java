package net.zaharenko424.a_changed.capability;

import net.minecraft.core.HolderLookup;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.ability.AbilityHolder;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.event.custom.AddTransfurProgressEvent;
import net.zaharenko424.a_changed.event.custom.TransfurredEvent;
import net.zaharenko424.a_changed.event.custom.UnTransfurredEvent;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundSelectAbilityPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundTransfurSyncPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.TransfurResult;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.AbilityUtils;
import net.zaharenko424.a_changed.util.TransfurUtils;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.*;

public class TransfurHandler implements AbilityHolder {

    public static final Supplier<RuntimeException> NO_CAPABILITY_EXC = ()-> new RuntimeException("Transfur capability was expected but not found!");

    public static final Serializer SERIALIZER = new Serializer();
    public static final IAttachmentCopyHandler<TransfurHandler> COPY_HANDLER = (attachment, holder, lookup) -> {
        if(((LivingEntity)holder).level().getGameRules().getBoolean(AChanged.KEEP_TRANSFUR) && attachment.isTransfurred()) {
            CompoundTag tag = SERIALIZER.write(attachment, lookup);
            return tag != null ? SERIALIZER.read(holder, tag, lookup) : null;
        }
        return null;
    };

    public static @Nullable TransfurHandler of(@NotNull LivingEntity entity){
        if(!entity.getType().is(TRANSFURRABLE_TAG)) return null;
        return entity.getData(AttachmentRegistry.TRANSFUR_HANDLER);
    }

    public static @NotNull TransfurHandler nonNullOf(@NotNull LivingEntity entity){
        return Utils.nonNullOrThrow(of(entity), NO_CAPABILITY_EXC.get());
    }

        private final LivingEntity holder;

        //Synced data
        private Ability selectedAbility;
        private float transfurProgress = 0;
        private TransfurType transfurType = null;
        private boolean isTransfurred = false;

        //Synced, not saved (used to tell client which model to remove)
        TransfurType transfurTypeO = null;

        static final int ticksUntilTFProgressDecrease = 200;
        static final int ticksBetweenTFProgressDecrease = 20;
        int i0 = 0;

        //Server only data
        boolean isBeingTransfurred = false;
        static final int ticksUntilDeathTF = 400;
        int beingTransfurredTimer;

        @ApiStatus.Internal
        public TransfurHandler(IAttachmentHolder holder){
            if(!(holder instanceof LivingEntity living) || !living.getType().is(AChanged.TRANSFURRABLE_TAG))
                throw new IllegalStateException("Tried to create TransfurHandler for unsupported holder: " + holder);
            this.holder = living;
            if(living instanceof Player && !living.level().isClientSide) selectedAbility = AbilityRegistry.GRAB_ABILITY.get();//make sure that players have access to (don't)wantToBeGrabbed screen
        }

        @Override
        public Ability getSelectedAbility(){
            return selectedAbility;
        }

        @Override
        public @NotNull List<? extends Ability> getAllowedAbilities() {
            return isTransfurred() ? transfurType.abilities : List.of();
        }

        @Override
        public void selectAbility(@NotNull Ability ability) {
            if(!isTransfurred() || !transfurType.abilities.contains(ability) || ability == selectedAbility) return;

            if(holder.level().isClientSide){
                PacketDistributor.sendToServer(new ServerboundSelectAbilityPacket(AbilityUtils.abilityIdOf(ability)));
                return;
            }

            if(selectedAbility != null) selectedAbility.unselect(holder);
            selectedAbility = ability;
            selectedAbility.select(holder);
            syncClients();
        }

        public float getTransfurProgress() {
            return transfurProgress;
        }

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

        public @Nullable TransfurType getTransfurType() {
            return transfurType;
        }

        public void setTransfurType(@NotNull TransfurType transfurType) {
            this.transfurType = transfurType;
        }

        public boolean isTransfurred() {
            return isTransfurred && transfurType != null;
        }

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

            TransfurResult result = context.result();

            if(isTransfurred() && !player.isCreative() && result != TransfurResult.TRANSFUR) return;
            if(onTransfurSound != null) level.playSound(null, player, onTransfurSound, SoundSource.PLAYERS,1,1);


            if(player.isCreative() || player.isSpectator() || result == TransfurResult.TRANSFUR){
                actuallyTransfur(transfurType);
                return;
            }
            switch (result != null ? result
                    : level.getGameRules().getBoolean(TRANSFUR_IS_DEATH) ? TransfurResult.DEATH
                    : level.getGameRules().getBoolean(CHOOSE_TF_OR_DIE) ? TransfurResult.PROMPT
                    : TransfurResult.TRANSFUR){
                case DEATH -> {
                    AbstractLatexBeast latexBeast = TransfurUtils.spawnLatex(transfurType, (ServerLevel) level, player.blockPosition());
                    latexBeast.copyEquipment(holder);
                    player.setInvulnerable(false);
                    player.hurt(DamageSources.transfur(null, Objects.requireNonNullElse(player.getLastHurtByMob(), player)), Float.MAX_VALUE);

                    NeoForge.EVENT_BUS.post(new TransfurredEvent(latexBeast, latexBeast, transfurType));
                }
                case PROMPT -> {
                    setBeingTransfurred(true);
                    this.transfurType = transfurType;
                    PacketDistributor.sendToPlayer(player, new ClientboundOpenTransfurScreenPacket());
                }
                case TRANSFUR -> actuallyTransfur(transfurType);
            }
        }

        private void actuallyTransfur(TransfurType transfurType){
            setBeingTransfurred(false);

            if(isTransfurred()){
                TransfurUtils.removeModifiers(holder, this.transfurType);
                this.transfurType.onUnTransfur(holder);
                transfurTypeO = this.transfurType;// <- needed to tell client what player model to remove
            }

            loadSyncedData(transfurType.abilities.isEmpty() ? null : transfurType.abilities.get(0),
                    TRANSFUR_TOLERANCE, true, transfurType);

            TransfurUtils.addModifiers(holder, transfurType);
            transfurType.onTransfur(holder);

            syncClients();

            NeoForge.EVENT_BUS.post(new TransfurredEvent(holder, null, transfurType));
        }

        public void unTransfur(@NotNull TransfurContext context) {
            if(holder.level().isClientSide) return;

            setBeingTransfurred(false);

            transfurTypeO = transfurType;//needed to tell client what player model to remove
            if(isTransfurred()) {
                TransfurUtils.removeModifiers(holder, transfurType);
                transfurType.onUnTransfur(holder);
            }

            loadSyncedData(AbilityRegistry.GRAB_ABILITY.get(),0, false, null);//assign grab ability to be able to switch (don't)wantToBeGrabbed
            syncClients();

            if(context.sound() != null)
                holder.level().playSound(null, holder.blockPosition(), context.sound(), SoundSource.PLAYERS);

            if(transfurTypeO != null) NeoForge.EVENT_BUS.post(new UnTransfurredEvent((Player) holder, transfurTypeO));
        }

        public boolean isBeingTransfurred() {
            return isBeingTransfurred;
        }

        public void setBeingTransfurred(boolean isBeingTransfurred) {
            if(this.isBeingTransfurred != isBeingTransfurred){
                beingTransfurredTimer = isBeingTransfurred ? ticksUntilDeathTF : 0;
            }
            this.isBeingTransfurred = isBeingTransfurred;
        }

        public void tick() {
            if(selectedAbility != null) {
                selectedAbility.serverTick(holder);

                getAllowedAbilities().forEach(abilityUnselected -> {
                    if (abilityUnselected == selectedAbility) return;
                    abilityUnselected.serverTickUnselected(holder);
                });
            }

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
            PacketDistributor.sendToPlayer(packetReceiver, packet());
        }

        public void syncClients(){
            holder.refreshDimensions();
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(holder, packet());
        }

        ClientboundTransfurSyncPacket packet(){
            return new ClientboundTransfurSyncPacket(holder.getId(),
                    selectedAbility != null ? AbilityRegistry.ABILITY_REGISTRY.getKey(selectedAbility) : Utils.NULL_LOC, transfurProgress,
                    isTransfurred, transfurType, holder instanceof Player ? transfurTypeO : null);
        }

        @ApiStatus.Internal
        public void loadSyncedData(@Nullable Ability ability, float transfurProgress, boolean isTransfurred, TransfurType transfurType){
            this.selectedAbility = ability;
            this.transfurProgress = transfurProgress;
            this.isTransfurred = isTransfurred;
            this.transfurType = transfurType;
        }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, TransfurHandler> {

        private Serializer(){}

        @Override
        public @NotNull TransfurHandler read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
            TransfurHandler handler = new TransfurHandler(holder);

            handler.loadSyncedData(tag.contains("ability") ? AbilityRegistry.ABILITY_REGISTRY.get(ResourceLocation.parse(tag.getString("ability"))) : null,
                    tag.getFloat(TRANSFUR_PROGRESS_KEY), tag.getBoolean(TRANSFURRED_KEY),
                    TransfurManager.getTransfurType(ResourceLocation.parse(tag.getString(TRANSFUR_TYPE_KEY))));

            if(holder instanceof Player) handler.setBeingTransfurred(tag.getBoolean(BEING_TRANSFURRED_KEY));

            if(handler.isTransfurred()) TransfurUtils.addModifiers((LivingEntity) holder, handler.transfurType);
            return handler;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull TransfurHandler attachment, HolderLookup.@NotNull Provider lookup) {
            CompoundTag tag = new CompoundTag();
            if(attachment.selectedAbility != null) tag.putString("ability", AbilityRegistry.ABILITY_REGISTRY.getKey(attachment.selectedAbility).toString());

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