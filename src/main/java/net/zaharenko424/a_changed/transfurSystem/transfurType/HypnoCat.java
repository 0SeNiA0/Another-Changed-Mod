package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.client.model.HypnoCatModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;

public class HypnoCat extends TransfurType {

    public HypnoCat(@NotNull ResourceLocation loc) {
        super(CatProperties.of(loc)
                .maxHealthModifier(4).colors(-13421773, -2621626)
                .addAbility(AbilityRegistry.GRAB_ABILITY).addAbility(AbilityRegistry.HYPNOSIS_ABILITY));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, HypnoCatModel::new);
    }
}