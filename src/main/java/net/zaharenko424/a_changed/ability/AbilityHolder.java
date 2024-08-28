package net.zaharenko424.a_changed.ability;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AbilityHolder {

    Ability getSelectedAbility();

    /**
     * @return unmodifiable list.
     */
    @NotNull List<? extends Ability> getAllowedAbilities();

    default boolean hasAbility(DeferredHolder<Ability, ? extends Ability> ability) {
        return getAllowedAbilities().contains(ability.get());
    }

    default boolean hasAbility(Ability ability){
        return getAllowedAbilities().contains(ability);
    }

    default void selectAbility(@NotNull ResourceLocation abilityId){
        selectAbility(AbilityUtils.abilityOf(abilityId));
    }

    default void selectAbility(@NotNull DeferredHolder<Ability, ? extends Ability> ability){
        selectAbility(ability.get());
    }

    void selectAbility(@NotNull Ability ability);
}