package net.zaharenko424.a_changed.ability.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.SequencedSet;

@ParametersAreNonnullByDefault
public interface AbilityHolder {

    LivingEntity asEntity();

    default InputController getInputController(){
        return null;
    }

    /**
     * @return Selected ability.
     */
    @Nullable Ability getSelectedAbility();

    /**
     * @return Ability set view.
     */
    @NotNull SequencedSet<? extends Ability> getAbilities();

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

    //-----------------------------     <@ApiStatus.Experimental>     -----------------------------//

    /**
     * @param key Key of the ability provider.
     */
    default boolean hasAbility(Ability ability, ResourceLocation key){
        return hasAbility(ability);
    }

    /**
     * @param key Key of the ability provider.
     * @return Whether any new abilities were added.
     */
    default boolean addAbility(DeferredHolder<Ability, ? extends Ability> ability, ResourceLocation key){
        return addAbility(ability.get(), key);
    }

    /**
     * @param key Key of the ability provider.
     * @return Whether any new abilities were added.
     */
    default boolean addAbility(Ability ability, ResourceLocation key){
        return false;
    }

    /**
     * @param key Key of the ability provider.
     * @return Whether any new abilities were added.
     */
    default boolean addAbilities(SequencedSet<Ability> abilities, ResourceLocation key){
        return false;
    }

    default void replaceAbilities(DeferredHolder<Ability, ? extends Ability> add, ResourceLocation key){
        replaceAbilities(add.get(), key);
    }

    default void replaceAbilities(Ability add, ResourceLocation key){}

    default void replaceAbilities(SequencedSet<Ability> add, ResourceLocation key){}

    default void replaceAbilities(SequencedSet<Ability> remove, SequencedSet<Ability> add, ResourceLocation key){}

    /**
     * @param key Key of the ability provider.
     * @return Whether any abilities were removed.
     */
    default boolean removeAbility(DeferredHolder<Ability, ? extends Ability> ability, ResourceLocation key){
        return removeAbility(ability.get(), key);
    }

    /**
     * @param key Key of the ability provider.
     * @return Whether any abilities were removed.
     */
    default boolean removeAbility(Ability ability, ResourceLocation key){
        return false;
    }

    /**
     * @param key Key of the ability provider.
     * @return Whether any abilities were removed.
     */
    default boolean removeAbilities(SequencedSet<Ability> abilities, ResourceLocation key){
        return false;
    }

    /**
     * @param key Key of the ability provider.
     * @return Whether any abilities were removed.
     */
    default boolean removeAbilities(ResourceLocation key){
        return false;
    }

    default void syncAbilities(){
        Ability selected = getSelectedAbility();
        if(selected == null) return;

        AbilityData data = selected.getAbilityData(asEntity());
        if(data != null) data.sync();
    }

    default void inputTick(){}

    default void serverTick(){}
    //-----------------------------     <@ApiStatus.Experimental/>    -----------------------------//

    /**
     * Selects specified ability.
     */
    default void selectAbility(DeferredHolder<Ability, ? extends Ability> ability){
        selectAbility(ability.get());
    }

    /**
     * Selects specified ability.
     */
    void selectAbility(@Nullable Ability ability);
}