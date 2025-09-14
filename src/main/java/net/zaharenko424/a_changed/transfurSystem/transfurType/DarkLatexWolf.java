package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import org.jetbrains.annotations.NotNull;

public class DarkLatexWolf extends LatexWolf {

    public DarkLatexWolf(@NotNull Properties<LatexBeast> properties){
        super(properties);
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, ClientOnly.darkLatexWolfModel(gender));
    }
}