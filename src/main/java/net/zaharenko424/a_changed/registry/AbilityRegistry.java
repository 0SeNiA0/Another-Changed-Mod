package net.zaharenko424.a_changed.registry;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.ability.FallFlyingAbility;
import net.zaharenko424.a_changed.ability.GrabAbility;
import net.zaharenko424.a_changed.ability.HypnosisAbility;

public class AbilityRegistry {

    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(AChanged.resourceLoc("transfur_abilities"), AChanged.MODID);
    public static final Registry<Ability> ABILITY_REGISTRY = ABILITIES.makeRegistry(builder -> {});

    public static final DeferredHolder<Ability, FallFlyingAbility> FALL_FLYING_ABILITY = ABILITIES
            .register("fall_flying_ability", FallFlyingAbility::new);

    public static final DeferredHolder<Ability, GrabAbility> GRAB_ABILITY = ABILITIES
            .register("grab_ability", GrabAbility::new);

    public static final DeferredHolder<Ability, HypnosisAbility> HYPNOSIS_ABILITY = ABILITIES
            .register("hypnosis_ability", HypnosisAbility::new);
}