package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexWolf extends TransfurType {

    public LatexWolf(@NotNull Properties properties){
        super(properties
                .maxHealthModifier(4)
                .addAbility(AbilityRegistry.GRAB_ABILITY).addAbility(AbilityRegistry.WOLF_PASSIVE));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, ClientOnly.latexWolfModel(gender, id));
    }
}