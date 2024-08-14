package net.zaharenko424.a_changed.ability;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.util.AbilityUtils;

import java.util.List;

public interface AbilityHolder {

    Ability getSelectedAbility();

    /**
     * @return unmodifiable list.
     */
    List<? extends Ability> getAllowedAbilities();

    default boolean hasAbility(DeferredHolder<Ability, ? extends Ability> ability) {
        return getAllowedAbilities().contains(ability.get());
    }

    default void selectAbility(ResourceLocation abilityId){
        selectAbility(AbilityUtils.abilityOf(abilityId));
    }

    default void selectAbility(DeferredHolder<Ability, ? extends Ability> ability){
        selectAbility(ability.get());
    }

    void selectAbility(Ability ability);
}