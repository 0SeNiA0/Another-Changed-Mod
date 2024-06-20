package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.event.custom.AddTransfurProgressEvent;
import net.zaharenko424.a_changed.event.custom.TransfurredEvent;
import net.zaharenko424.a_changed.event.custom.UnTransfurredEvent;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundPlayerTransfurSyncPacket;
import net.zaharenko424.a_changed.network.packets.transfur.ClientboundRemotePlayerTransfurSyncPacket;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.registry.EntityRegistry.WHITE_LATEX_WOLF_MALE;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.TRANSFUR_TOLERANCE;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.getTransfurEntity;


public class TransfurEvent {

    public static final TriConsumer<LivingEntity, TransfurType, Float> ADD_TRANSFUR_DEF = addTransfurProgress().build();
    public static final TriConsumer<LivingEntity, TransfurType, Float> ADD_TRANSFUR_CRYSTAL = addTransfurProgress()
            .onTFToleranceReached(transfur -> transfur.withSound(SoundRegistry.TRANSFUR_1.get())).build();
    public static final BiConsumer<LivingEntity, TransfurType> TRANSFUR_DEF = transfur().build();
    public static final BiConsumer<LivingEntity, TransfurType> TRANSFUR_TF = transfur().setResult(TransfurResult.TRANSFUR).build();
    public static final BiConsumer<LivingEntity, TransfurType> TRANSFUR_DEATH = transfur().setResult(TransfurResult.DEATH).build();
    public static final Consumer<ServerPlayer> UNTRANSFUR = unTransfur().build();
    public static final Consumer<ServerPlayer> UNTRANSFUR_SILENT = unTransfur().withSound(null).build();
    public static final Consumer<LivingEntity> RECALCULATE_PROGRESS = recalculate().build();

    @Contract(value = " -> new", pure = true)
    public static @NotNull AddTransfurProgress addTransfurProgress(){
        return new AddTransfurProgress();
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Transfur transfur(){
        return new Transfur(false);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull UnTransfur unTransfur(){
        return new UnTransfur();
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Recalculate recalculate(){
        return new Recalculate();
    }

    static void platformCheck(@NotNull LivingEntity target){
        if(target.level().isClientSide) throw new IllegalStateException("Cannot run serverside only methods on client!");
    }

    public static AbstractLatexBeast spawnLatex(@NotNull TransfurType transfurType, @NotNull ServerLevel level, @NotNull BlockPos pos){
        return Objects.requireNonNullElseGet(getTransfurEntity(transfurType.id), WHITE_LATEX_WOLF_MALE).spawn(level, pos, MobSpawnType.CONVERSION);
    }

    public static void updatePlayer(@NotNull ServerPlayer player){
        updatePlayer(player, TransfurCapability.nonNullOf(player));
    }

    static void updatePlayer(@NotNull ServerPlayer player, @NotNull ITransfurHandler handler){
        player.refreshDimensions();
        PacketDistributor.PLAYER.with(player).send(new ClientboundPlayerTransfurSyncPacket(handler));
        PacketDistributor.TRACKING_ENTITY.with(player).send(new ClientboundRemotePlayerTransfurSyncPacket(handler, player.getUUID()));
    }

    public static class AddTransfurProgress {

        final Transfur transfur;
        SoundEvent sound;
        boolean checkResistance = true;

        AddTransfurProgress(){
            transfur = new Transfur(true);
        }

        public AddTransfurProgress withSound(SoundEvent sound){
            this.sound = sound;
            return this;
        }

        public AddTransfurProgress checkResistance(boolean b){
            checkResistance = b;
            return this;
        }

        public AddTransfurProgress onTFToleranceReached(@NotNull Consumer<Transfur> consumer){
            consumer.accept(transfur);
            return this;
        }

        public TriConsumer<LivingEntity, TransfurType, Float> build() {
            return  (target, transfurType, amount) -> {
                platformCheck(target);
                ITransfurHandler handler = TransfurCapability.of(target);
                if(handler == null) return;
                if(handler.isBeingTransfurred() || handler.isTransfurred()) return;

                AddTransfurProgressEvent event = new AddTransfurProgressEvent(target, transfurType, amount);
                NeoForge.EVENT_BUS.post(event);
                if(event.isCanceled()) return;

                float progress;
                if(event.getProgressToAdd() > 0) {
                    progress = event.getProgressToAdd();
                } else {
                    if (checkResistance) {
                        float resistance = (float) target.getAttributeValue(LATEX_RESISTANCE);
                        amount *= 1 - resistance;
                    }
                    progress = handler.getTransfurProgress() + amount;
                }

                if (progress >= TRANSFUR_TOLERANCE) {
                    transfur.transfur(target, transfurType);
                    return;
                }
                handler.setTransfurProgress(progress, transfurType);
                if(target instanceof ServerPlayer player) updatePlayer(player, handler);
            };
        }
    }

    @ParametersAreNonnullByDefault
    public static class Transfur {

        final boolean subEvent;
        SoundEvent sound = SoundRegistry.TRANSFUR.get();
        boolean ignoreGameRules = false;
        TransfurResult result;

        Transfur(boolean subEvent){
            this.subEvent = subEvent;
        }

        public Transfur withSound(SoundEvent sound){
            this.sound=sound;
            return this;
        }

        public Transfur setResult(TransfurResult result){
            this.result = result;
            return this;
        }

        public BiConsumer<LivingEntity, TransfurType> build(){
            if(subEvent) throw new IllegalStateException("Cannot build sub event!");
            return this::transfur;
        }

        void transfur(LivingEntity target, TransfurType transfurType) {
            platformCheck(target);
            ITransfurHandler handler = TransfurCapability.of(target);
            if(handler == null) return;
            ServerLevel level = (ServerLevel) target.level();
            if(target instanceof ServerPlayer player) {
                if(sound != null) level.playSound(null, player, sound, SoundSource.PLAYERS,1,1);
                transfurPlayer(player, handler, transfurType, level);
                return;
            }
            AbstractLatexBeast latexBeast = spawnLatex(transfurType, level, target.blockPosition());
            latexBeast.copyEquipment(target);
            if(sound != null) target.playSound(sound);
            target.discard();
            NeoForge.EVENT_BUS.post(new TransfurredEvent(latexBeast, transfurType));
        }

        void transfurPlayer(ServerPlayer player, ITransfurHandler handler, TransfurType transfurType, ServerLevel level){
            if(player.isCreative() || player.isSpectator() || result == TransfurResult.TRANSFUR){
                actuallyTransfurPlayer(player, handler, transfurType);
                return;
            }
            switch (result != null ? result
                    : level.getGameRules().getBoolean(TRANSFUR_IS_DEATH) ? TransfurResult.DEATH
                    : level.getGameRules().getBoolean(CHOOSE_TF_OR_DIE) ? TransfurResult.PROMPT
                    : TransfurResult.TRANSFUR){
                case DEATH -> {
                    handler.unTransfur();
                    updatePlayer(player, handler);
                    player.setInvulnerable(false);
                    player.hurt(DamageSources.transfur(null, Objects.requireNonNullElse(player.getLastHurtByMob(), player)), Float.MAX_VALUE);
                    AbstractLatexBeast latexBeast = spawnLatex(transfurType, level, player.blockPosition());
                    NeoForge.EVENT_BUS.post(new TransfurredEvent(latexBeast, transfurType));
                }
                case PROMPT -> {
                    handler.setBeingTransfurred(true);
                    handler.setTransfurType(transfurType);
                    PacketDistributor.PLAYER.with(player).send(new ClientboundOpenTransfurScreenPacket());
                }
                case TRANSFUR -> actuallyTransfurPlayer(player, handler, transfurType);
            }
        }

        void actuallyTransfurPlayer(ServerPlayer player, ITransfurHandler handler, TransfurType transfurType){
            if(handler.isTransfurred()) handler.getTransfurType().onUnTransfur(player);
            handler.transfur(transfurType);
            transfurType.onTransfur(player);
            updatePlayer(player, handler);
            NeoForge.EVENT_BUS.post(new TransfurredEvent(player, transfurType));
        }
    }

    public static class UnTransfur {

        SoundEvent sound = SoundRegistry.TRANSFUR.get();

        public UnTransfur withSound(SoundEvent sound){
            this.sound = sound;
            return this;
        }

        public Consumer<ServerPlayer> build() {
            return player -> {
                platformCheck(player);
                ITransfurHandler handler = TransfurCapability.of(player);
                if(handler == null) return;

                if(sound != null)
                    player.level().playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS);
                if(handler.isTransfurred()) handler.getTransfurType().onUnTransfur(player);
                TransfurType transfurType = handler.getTransfurType();
                handler.unTransfur();
                updatePlayer(player, handler);
                if(transfurType != null) NeoForge.EVENT_BUS.post(new UnTransfurredEvent(player, transfurType));
            };
        }
    }

    public static class Recalculate {

        public Consumer<LivingEntity> build() {
            return target -> {
                platformCheck(target);
                ITransfurHandler handler = TransfurCapability.of(target);
                if(handler == null) return;

                if(handler.isTransfurred() || handler.isBeingTransfurred()) return;
                if(handler.getTransfurProgress() >= TRANSFUR_TOLERANCE && handler.getTransfurType() != null)
                    new Transfur(false).build().accept(target, handler.getTransfurType());
            };
        }
    }
}