package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.client.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.client.model.BeiFengModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.Latex;
import org.jetbrains.annotations.NotNull;

public class BeiFeng extends TransfurType {

    public BeiFeng(@NotNull ResourceLocation loc) {
        super(Properties.of(loc, Latex.WHITE)
                .maxHealthModifier(4).colors(-11442787, -14013910)
                .addAbility(AbilityRegistry.GRAB_ABILITY));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, BeiFengModel::new);
    }
}