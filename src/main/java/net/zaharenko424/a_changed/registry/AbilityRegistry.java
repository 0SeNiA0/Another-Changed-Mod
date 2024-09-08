package net.zaharenko424.a_changed.registry;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.*;

public class AbilityRegistry {

    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(AChanged.resourceLoc("transfur_abilities"), AChanged.MODID);
    public static final Registry<Ability> ABILITY_REGISTRY = ABILITIES.makeRegistry(builder -> {});

    public static final DeferredHolder<Ability, CatAbility> CAT_PASSIVE = ABILITIES
            .register("cat_ability", CatAbility::new);

    public static final DeferredHolder<Ability, FallFlyingAbility> FALL_FLYING_PASSIVE = ABILITIES
            .register("fall_flying_ability", FallFlyingAbility::new);

    public static final DeferredHolder<Ability, ? extends Ability> FISH_PASSIVE = ABILITIES
            .register("fish_ability", FishAbility::new);

    public static final DeferredHolder<Ability, GrabAbility> GRAB_ABILITY = ABILITIES
            .register("grab_ability", GrabAbility::new);

    public static final DeferredHolder<Ability, HypnosisAbility> HYPNOSIS_ABILITY = ABILITIES
            .register("hypnosis_ability", HypnosisAbility::new);

    public static final DeferredHolder<Ability, ? extends Ability> WOLF_PASSIVE = ABILITIES
            .register("wolf_ability", WolfAbility::new);
}