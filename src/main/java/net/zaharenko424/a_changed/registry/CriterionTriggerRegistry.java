package net.zaharenko424.a_changed.registry;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.criterion.ShotWithSyringeTrigger;
import net.zaharenko424.a_changed.criterion.SteppedOnSyringeTrigger;
import net.zaharenko424.a_changed.criterion.TransfurTrigger;

public class CriterionTriggerRegistry {

    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, AChanged.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, ShotWithSyringeTrigger> ENTITY_SHOT_WITH_SYRINGE = TRIGGER_TYPES.register("entity_shot_with_syringe", ShotWithSyringeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, ShotWithSyringeTrigger> PLAYER_SHOT_WITH_SYRINGE = TRIGGER_TYPES.register("player_shot_with_syringe", ShotWithSyringeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, ShotWithSyringeTrigger> SHOT_SYRINGE_BOUNCED = TRIGGER_TYPES.register("shot_syringe_bounced", ShotWithSyringeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, ShotWithSyringeTrigger> SYRINGE_BOUNCED_OFF_PLAYER = TRIGGER_TYPES.register("syringe_bounced_off_player", ShotWithSyringeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, SteppedOnSyringeTrigger> ENTITY_STEPPED_ON_SYRINGE = TRIGGER_TYPES.register("entity_stepped_on_syringe", SteppedOnSyringeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, SteppedOnSyringeTrigger> PLAYER_STEPPED_ON_SYRINGE = TRIGGER_TYPES.register("player_stepped_on_syringe", SteppedOnSyringeTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, TransfurTrigger> PLAYER_TRANSFURRED_NO_DEATH = TRIGGER_TYPES.register("player_transfurred_no_death", TransfurTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, TransfurTrigger> PLAYER_TRANSFURRED_ENTITY = TRIGGER_TYPES.register("player_transfurred_entity", TransfurTrigger::new);
}
