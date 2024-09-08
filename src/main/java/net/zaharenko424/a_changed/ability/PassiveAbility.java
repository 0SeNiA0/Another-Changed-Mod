package net.zaharenko424.a_changed.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface PassiveAbility extends Ability {

    @Override
    default boolean isActive() {
        return false;
    }

    @Override
    default void activate(@NotNull LivingEntity holder, boolean oneShot, @NotNull FriendlyByteBuf additionalData) {}

    @Override
    default void deactivate(@NotNull LivingEntity holder) {}

    @Override
    default void select(@NotNull LivingEntity holder) {}

    @Override
    default void unselect(@NotNull LivingEntity holder) {}
}