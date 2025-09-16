package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.client.model.YufengDragonModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;

public class YufengDragon extends TransfurType<LatexBeast> {

    public YufengDragon(@NotNull ResourceLocation loc) {
        super(FlyingProperties.of(loc, EntityRegistry.YUFENG_DRAGON)
                .maxHealthModifier(6).colors(-13686230,-14408668)
                .addAbility(AbilityRegistry.GRAB_ABILITY));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, YufengDragonModel::new);
    }
}