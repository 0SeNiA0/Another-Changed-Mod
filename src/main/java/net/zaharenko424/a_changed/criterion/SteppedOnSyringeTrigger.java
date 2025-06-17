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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SteppedOnSyringeTrigger extends SimpleCriterionTrigger<SteppedOnSyringeTrigger.TriggerInstance> {

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack syringe, @Nullable Entity entity){
        LootContext context = entity == null ? null : EntityPredicate.createContext(player, entity);
        trigger(player, instance -> instance.matches(syringe, context));
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

        public static Criterion<SteppedOnSyringeTrigger.TriggerInstance> playerSteppedOnSyringe(){
            return CriterionTriggerRegistry.PLAYER_STEPPED_ON_SYRINGE.get()
                    .createCriterion(new SteppedOnSyringeTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<SteppedOnSyringeTrigger.TriggerInstance> playerSteppedOnSyringe(ItemPredicate syringe){
            return CriterionTriggerRegistry.PLAYER_STEPPED_ON_SYRINGE.get()
                    .createCriterion(new SteppedOnSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.empty()));
        }

        public static Criterion<SteppedOnSyringeTrigger.TriggerInstance> playerSteppedOnSyringe(ItemPredicate syringe, ContextAwarePredicate placer){
            return CriterionTriggerRegistry.PLAYER_STEPPED_ON_SYRINGE.get()
                    .createCriterion(new SteppedOnSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.of(placer)));
        }



        public static Criterion<SteppedOnSyringeTrigger.TriggerInstance> entitySteppedOnSyringe(){
            return CriterionTriggerRegistry.ENTITY_STEPPED_ON_SYRINGE.get()
                    .createCriterion(new SteppedOnSyringeTrigger.TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public static Criterion<SteppedOnSyringeTrigger.TriggerInstance> entitySteppedOnSyringe(ItemPredicate syringe){
            return CriterionTriggerRegistry.ENTITY_STEPPED_ON_SYRINGE.get()
                    .createCriterion(new SteppedOnSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.empty()));
        }

        public static Criterion<SteppedOnSyringeTrigger.TriggerInstance> entitySteppedOnSyringe(ItemPredicate syringe, ContextAwarePredicate entity){
            return CriterionTriggerRegistry.ENTITY_STEPPED_ON_SYRINGE.get()
                    .createCriterion(new SteppedOnSyringeTrigger.TriggerInstance(Optional.empty(), Optional.of(syringe), Optional.of(entity)));
        }

        public boolean matches(ItemStack syringe, LootContext context){
            if(this.syringe.isPresent() && !this.syringe.get().test(syringe)) return false;

            return this.entity.isEmpty() || context != null && this.entity.get().matches(context);
        }

        @Override
        public void validate(@NotNull CriterionValidator validator) {
            SimpleInstance.super.validate(validator);

            validator.validateEntity(entity, ".entity");
        }
    }
}
