package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import org.jetbrains.annotations.NotNull;

public class Special extends TransfurType {

    public Special(@NotNull Properties properties) {
        super(properties.maxHealthModifier(4).organic(true));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {}
}