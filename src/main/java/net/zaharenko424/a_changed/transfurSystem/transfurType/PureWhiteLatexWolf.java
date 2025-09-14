package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.zaharenko424.a_changed.client.model.PureWhiteLatexWolfModel;
import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import org.jetbrains.annotations.NotNull;

public class PureWhiteLatexWolf extends LatexWolf {

    public PureWhiteLatexWolf(@NotNull Properties<LatexBeast> properties){
        super(properties);
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, PureWhiteLatexWolfModel::new);
    }
}