package net.zaharenko424.a_changed.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TransfurTrigger extends SimpleCriterionTrigger<TransfurTrigger.TriggerInstance> {

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, DamageSource damageSource, TransfurType transfurType){
        trigger(player, instance -> instance.matches(player, EntityPredicate.createContext(player, player), damageSource, transfurType));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player, Optional<DamageSourcePredicate> damagePredicate,
            Optional<TransfurTypePredicate> transfurType) implements SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                builder -> builder.group(
                        Codec.optionalField("player", EntityPredicate.ADVANCEMENT_CODEC, false).forGetter(TriggerInstance::player),
                        Codec.optionalField("damage_source", DamageSourcePredicate.CODEC, false).forGetter(TriggerInstance::damagePredicate),
                        Codec.optionalField("transfur_type_predicate", TransfurTypePredicate.CODEC, false).forGetter(TriggerInstance::transfurType)
                ).apply(builder, TriggerInstance::new));

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurred(){
            return AChanged.PLAYER_TRANSFURRED.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurred(TransfurTypePredicate transfurType){
            return AChanged.PLAYER_TRANSFURRED.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(transfurType)));
        }

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurredEntity(){
            return AChanged.PLAYER_TRANSFURRED_ENTITY.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurredEntity(TransfurTypePredicate transfurType){
            return AChanged.PLAYER_TRANSFURRED_ENTITY.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(transfurType)));
        }

        /*public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurredEntity(){
            return Triggers.PLAYER_TRANSFURRED_ENTITY
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurredEntity(EntityPredicate.Builder entityPredicate){
            return Triggers.PLAYER_TRANSFURRED_ENTITY
                    .createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(entityPredicate)), Optional.empty(), Optional.empty()));
        }

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurredEntity(TransfurTypePredicate transfurType){
            return Triggers.PLAYER_TRANSFURRED_ENTITY
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(transfurType)));
        }

        public static Criterion<TransfurTrigger.TriggerInstance> entityTransfurredPlayer(){
            return Triggers.ENTITY_TRANSFURRED_PLAYER
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }*/

        public boolean matches(ServerPlayer player, LootContext context, DamageSource source, TransfurType transfurType){
            if(this.player.isPresent() && !this.player.get().matches(context)) return false;
            if(damagePredicate.isPresent()) {//TODO do something with this ?
                if(source == null){
                    AChanged.LOGGER.warn("Expected nonNull DamageSource for TransfurTrigger!");
                    return false;
                }
                if(!damagePredicate.get().matches(player, source)) return false;
            }

            return this.transfurType.isEmpty() || this.transfurType.get().matches(transfurType);
        }
    }
}