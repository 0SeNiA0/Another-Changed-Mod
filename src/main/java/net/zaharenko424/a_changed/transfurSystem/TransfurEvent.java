package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.network.PacketHandler;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenTransfurScreenPacket;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.zaharenko424.a_changed.AChanged.*;
import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.*;

/**
 * Serverside use only!
 */
public class TransfurEvent {

    public static final TriConsumer<LivingEntity, AbstractTransfurType, Float> ADD_TRANSFUR_DEF = addTransfurProgress().build();
    public static final TriConsumer<LivingEntity, AbstractTransfurType, Float> ADD_TRANSFUR_CRYSTAL = addTransfurProgress()
            .onTFToleranceReached(transfur -> transfur.withSound(SoundRegistry.TRANSFUR_1.get())).build();
    public static final Consumer<ServerPlayer> UNTRANSFUR = unTransfur().build();
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

        public AddTransfurProgress onTFToleranceReached(Consumer<Transfur> consumer){
            consumer.accept(transfur);
            return this;
        }

        public TriConsumer<LivingEntity, AbstractTransfurType, Float> build() {
            return  (target, transfurType, amount) -> target.getCapability(TransfurCapability.CAPABILITY).ifPresent((handler) -> {
                platformCheck(target);
                if (handler.isBeingTransfurred()) return;
                if (sound != null) target.level().playSound(null, target.blockPosition(), sound, SoundSource.PLAYERS);
                float finalAmount = amount;
                if (checkResistance) {
                    float resistance = (float) target.getAttributeValue(LATEX_RESISTANCE);
                    finalAmount *= 1 - resistance;
                }
                float progress = handler.getTransfurProgress() + finalAmount;
                if (progress >= TRANSFUR_TOLERANCE) {
                    transfur.transfur(target, transfurType);
                    return;
                }
                handler.setTransfurProgress(progress, transfurType);
                if (target instanceof ServerPlayer player) updatePlayer(player, handler);
            });
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

        //Essentially == setResult(TRANSFUR) so not sure if this is needed
        public Transfur ignoreGameRules(boolean b){
            ignoreGameRules = b;
            return this;
        }

        public Transfur setResult(TransfurResult result){
            this.result = result;
            return this;
        }

        public BiConsumer<LivingEntity, AbstractTransfurType> build(){
            if(subEvent) throw new IllegalStateException("Cannot build sub event!");
            return this::transfur;
        }

        void transfur(LivingEntity target, AbstractTransfurType transfurType) {
            platformCheck(target);
            target.getCapability(TransfurCapability.CAPABILITY).ifPresent(handler -> {
                Level level = target.level();
                if (target instanceof ServerPlayer player) {
                    level.playSound(null,player,sound, SoundSource.PLAYERS,1,1);
                    transfurPlayer(player, handler, transfurType, level);
                    return;
                }
                spawnLatex(transfurType, level, target.blockPosition());
                target.playSound(sound);
                target.discard();
            });
        }

        void transfurPlayer(ServerPlayer player, ITransfurHandler handler, AbstractTransfurType transfurType, Level level){
            if(player.isCreative() || (ignoreGameRules && result == null)){
                actuallyTransfurPlayer(player, handler, transfurType);
                return;
            }
            switch (result != null ? result
                    : level.getGameRules().getBoolean(TRANSFUR_IS_DEATH) ? TransfurResult.DEATH
                    : level.getGameRules().getBoolean(CHOOSE_TF_OR_DIE) ? TransfurResult.PROMPT
                    : TransfurResult.TRANSFUR){
                case DEATH -> {
                    handler.unTransfur();
                    updatePlayer(player,handler);
                    player.hurt(TransfurDamageSource.transfur(player,player.getLastHurtByMob()),Float.MAX_VALUE);
                    spawnLatex(transfurType,level,player.blockPosition());
                }
                case PROMPT -> {
                    handler.setBeingTransfurred(true);
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->player),new ClientboundOpenTransfurScreenPacket());
                }
                case TRANSFUR -> actuallyTransfurPlayer(player, handler, transfurType);
            }
        }

        void actuallyTransfurPlayer(ServerPlayer player, ITransfurHandler handler, AbstractTransfurType transfurType){
            handler.transfur(transfurType);
            updatePlayer(player,handler);
        }
    }

    public static class UnTransfur {

        SoundEvent sound;

        public UnTransfur withSound(SoundEvent sound){
            this.sound = sound;
            return this;
        }

        public Consumer<ServerPlayer> build() {
            return player -> player.getCapability(TransfurCapability.CAPABILITY).ifPresent(handler -> {
                platformCheck(player);
                if(sound != null) player.level().playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS);
                handler.unTransfur();
                updatePlayer(player, handler);
            });
        }
    }

    public static class Recalculate {

        public Consumer<LivingEntity> build() {
            return target -> target.getCapability(TransfurCapability.CAPABILITY).ifPresent(handler -> {
                platformCheck(target);
                if(handler.isTransfurred()||handler.isBeingTransfurred()) return;
                if(handler.getTransfurProgress() >= TRANSFUR_TOLERANCE && handler.getTransfurType() != null)
                    new Transfur(false).build().accept(target, handler.getTransfurType());
            });
        }
    }
}