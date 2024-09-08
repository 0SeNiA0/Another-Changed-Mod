package net.zaharenko424.a_changed.util;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.registry.AbilityRegistry;

public class AbilityUtils {

    public static ResourceLocation abilityIdOf(Ability ability) {
        return AbilityRegistry.ABILITY_REGISTRY.getKey(ability);
    }

    public static Ability abilityOf(ResourceLocation abilityId) {
        return AbilityRegistry.ABILITY_REGISTRY.get(abilityId);
    }
}