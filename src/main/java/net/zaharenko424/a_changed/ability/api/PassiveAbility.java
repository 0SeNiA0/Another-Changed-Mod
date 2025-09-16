package net.zaharenko424.a_changed.ability.api;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface PassiveAbility extends Ability {

    @Override
    default boolean canUse(@NotNull LivingEntity holder){
        return false;
    }

    @Override
    default ActivationType activationType() {
        return ActivationType.NEVER;
    }

    @Override
    default void select(@NotNull LivingEntity holder) {}

    @Override
    default void unselect(@NotNull LivingEntity holder) {}
}