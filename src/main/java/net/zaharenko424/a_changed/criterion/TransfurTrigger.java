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
import net.zaharenko424.a_changed.registry.CriterionTriggerRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TransfurTrigger extends SimpleCriterionTrigger<TransfurTrigger.TriggerInstance> {

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, DamageSource damageSource, TransfurType<?> transfurType){
        trigger(player, instance -> instance.matches(player, damageSource, transfurType));
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

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurredNoDeath(){
            return CriterionTriggerRegistry.PLAYER_TRANSFURRED_NO_DEATH.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurredNoDeath(TransfurTypePredicate transfurType){
            return CriterionTriggerRegistry.PLAYER_TRANSFURRED_NO_DEATH.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(transfurType)));
        }

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurredEntity(){
            return CriterionTriggerRegistry.PLAYER_TRANSFURRED_ENTITY.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<TransfurTrigger.TriggerInstance> playerTransfurredEntity(TransfurTypePredicate transfurType){
            return CriterionTriggerRegistry.PLAYER_TRANSFURRED_ENTITY.get()
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

        public boolean matches(ServerPlayer player, DamageSource source, TransfurType<?> transfurType){
            if(damagePredicate.isPresent()) {
                if(source == null || !damagePredicate.get().matches(player, source)) return false;
            }

            return this.transfurType.isEmpty() || this.transfurType.get().matches(transfurType);
        }
    }
}