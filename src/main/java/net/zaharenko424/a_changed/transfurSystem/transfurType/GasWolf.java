package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.zaharenko424.a_changed.client.model.GasWolfModel;
import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import org.jetbrains.annotations.NotNull;

public class GasWolf extends LatexWolf {

    public GasWolf(@NotNull Properties<LatexBeast> properties){
        super(properties);
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, GasWolfModel::new);
    }
}