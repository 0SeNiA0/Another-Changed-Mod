package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexWolf extends TransfurType<LatexBeast> {

    public LatexWolf(@NotNull Properties<LatexBeast> properties){
        super(properties
                .maxHealthModifier(4)
                .addAbility(AbilityRegistry.GRAB_ABILITY).addAbility(AbilityRegistry.WOLF_PASSIVE));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, ClientOnly.latexWolfModel(id));
    }
}