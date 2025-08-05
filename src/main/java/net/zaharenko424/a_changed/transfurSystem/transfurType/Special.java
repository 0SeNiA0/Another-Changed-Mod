package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import org.jetbrains.annotations.NotNull;

public class Special extends TransfurType<LatexBeast> {

    public Special(@NotNull Properties<LatexBeast> properties) {
        super(properties.maxHealthModifier(4).organic(true));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {}
}