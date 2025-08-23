package net.zaharenko424.a_changed.ability;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;

@ParametersAreNonnullByDefault
public interface AbilityHolder {

    /**
     * @return Selected ability.
     */
    Ability getSelectedAbility();

    /**
     * @return Ability list view.
     */
    @NotNull List<? extends Ability> getAbilities();

    /**
     * @return Whether this holder has specified ability.
     */
    default boolean hasAbility(DeferredHolder<Ability, ? extends Ability> ability) {
        return hasAbility(ability.get());
    }

    /**
     * @return Whether this holder has specified ability.
     */
    default boolean hasAbility(Ability ability){
        return getAbilities().contains(ability);
    }

    /**
     * @return Whether the abilities were set.
     */
    default boolean setAbilities(Set<Ability> abilities){
        return false;
    }

    /**
     *  Selects ability with specified abilityId.
     */
    default void selectAbility(ResourceLocation abilityId){
        selectAbility(AbilityUtils.abilityOf(abilityId));
    }

    /**
     * Selects specified ability.
     */
    default void selectAbility(DeferredHolder<Ability, ? extends Ability> ability){
        selectAbility(ability.get());
    }

    /**
     * Selects specified ability.
     */
    void selectAbility(Ability ability);
}