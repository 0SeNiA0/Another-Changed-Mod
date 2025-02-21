package net.zaharenko424.a_changed.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.ability.AbilityHolder;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AbilityUtils {

    public static ResourceLocation abilityIdOf(Ability ability) {
        return AbilityRegistry.ABILITY_REGISTRY.getKey(ability);
    }

    public static Ability abilityOf(ResourceLocation abilityId) {
        return AbilityRegistry.ABILITY_REGISTRY.get(abilityId);
    }

    public static @Nullable AbilityHolder of(LivingEntity holder){
        if(holder instanceof AbilityHolder aHolder) return aHolder;
        return TransfurHandler.of(holder);
    }

    public static boolean hasAbility(Ability ability, LivingEntity holder){
        AbilityHolder aHolder = of(holder);
        return aHolder != null && aHolder.hasAbility(ability);
    }

    public static boolean hasAbility(DeferredHolder<Ability, ? extends Ability> ability, LivingEntity holder){
        AbilityHolder aHolder = of(holder);
        return aHolder != null && aHolder.hasAbility(ability);
    }

    public static boolean hasCatAbility(LivingEntity entity){
        return hasAbility(AbilityRegistry.CAT_PASSIVE, entity);
    }

    public static boolean hasDLPupAbilities(LivingEntity entity){
        AbilityHolder aHolder = of(entity);
        if(aHolder == null) return false;

        return aHolder.hasAbility(AbilityRegistry.DL_PUP_AGE) && aHolder.hasAbility(AbilityRegistry.DL_PUP_MELT);
    }

    public static boolean hasFallFlyingAbility(LivingEntity entity){
        return hasAbility(AbilityRegistry.FALL_FLYING_PASSIVE, entity);
    }

    public static boolean hasFishAbility(LivingEntity entity){
        return hasAbility(AbilityRegistry.FISH_PASSIVE, entity);
    }

    public static boolean hasWolfAbility(LivingEntity entity){
        return hasAbility(AbilityRegistry.WOLF_PASSIVE, entity);
    }
}