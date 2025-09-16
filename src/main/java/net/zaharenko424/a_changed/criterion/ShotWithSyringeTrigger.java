package net.zaharenko424.a_changed.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.zaharenko424.a_changed.registry.CriterionTriggerRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ShotWithSyringeTrigger extends SimpleCriterionTrigger<ShotWithSyringeTrigger.TriggerInstance> {

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack syringe, Entity entity) {
        trigger(player, instance -> instance.matches(syringe, EntityPredicate.createContext(player, entity)));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> syringe,
                                  Optional<ContextAwarePredicate> entity) implements SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                builder -> builder.group(
                        Codec.optionalField("player", EntityPredicate.ADVANCEMENT_CODEC, false).forGetter(TriggerInstance::player),
                        Codec.optionalField("syringe", ItemPredicate.CODEC, false).forGetter(TriggerInstance::syringe),
                        Codec.optionalField("entity", EntityPredicate.ADVANCEMENT_CODEC, false).forGetter(TriggerInstance::entity)
                ).apply(builder, TriggerInstance::new)
        );

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> playerShotWithSyringe(){
            return CriterionTriggerRegistry.PLAYER_SHOT_WITH_SYRINGE.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> playerShotWithSyringe(ItemPredicate syringe){
            return CriterionTriggerRegistry.PLAYER_SHOT_WITH_SYRINGE.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.empty()));
        }

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> playerShotWithSyringe(ItemPredicate syringe, ContextAwarePredicate shooter){
            return CriterionTriggerRegistry.PLAYER_SHOT_WITH_SYRINGE.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.of(shooter)));
        }



        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> playerShotEntityWithSyringe(){
            return CriterionTriggerRegistry.ENTITY_SHOT_WITH_SYRINGE.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> playerShotEntityWithSyringe(ItemPredicate syringe){
            return CriterionTriggerRegistry.ENTITY_SHOT_WITH_SYRINGE.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.empty()));
        }

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> playerShotEntityWithSyringe(ContextAwarePredicate entity){
            return CriterionTriggerRegistry.ENTITY_SHOT_WITH_SYRINGE.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(entity)));
        }

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> playerShotEntityWithSyringe(ItemPredicate syringe, ContextAwarePredicate entity){
            return CriterionTriggerRegistry.ENTITY_SHOT_WITH_SYRINGE.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.of(entity)));
        }



        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> shotSyringeBounced(){
            return CriterionTriggerRegistry.SHOT_SYRINGE_BOUNCED.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> shotSyringeBounced(ItemPredicate syringe){
            return CriterionTriggerRegistry.SHOT_SYRINGE_BOUNCED.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.empty()));
        }

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> shotSyringeBounced(ItemPredicate syringe, ContextAwarePredicate entity){
            return CriterionTriggerRegistry.SHOT_SYRINGE_BOUNCED.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.of(entity)));
        }



        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> syringeBouncedOffPlayer(){
            return CriterionTriggerRegistry.SYRINGE_BOUNCED_OFF_PLAYER.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> syringeBouncedOffPlayer(ItemPredicate syringe){
            return CriterionTriggerRegistry.SYRINGE_BOUNCED_OFF_PLAYER.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.empty()));
        }

        public static Criterion<ShotWithSyringeTrigger.TriggerInstance> syringeBouncedOffPlayer(ItemPredicate syringe, ContextAwarePredicate shooter){
            return CriterionTriggerRegistry.SYRINGE_BOUNCED_OFF_PLAYER.get()
                    .createCriterion(new ShotWithSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.of(shooter)));
        }

        public boolean matches(ItemStack syringe, LootContext context){
            if(this.syringe().isPresent() && !this.syringe.get().test(syringe)) return false;

            return entity.isEmpty() || entity.get().matches(context);
        }

        @Override
        public void validate(@NotNull CriterionValidator validator) {
            SimpleInstance.super.validate(validator);
            validator.validateEntity(entity, ".entity");
        }
    }
}
