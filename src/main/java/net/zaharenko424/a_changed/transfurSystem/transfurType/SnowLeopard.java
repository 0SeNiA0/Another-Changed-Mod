package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.entity.LatexBeast;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.client.model.SnowLeopardFemaleModel;
import net.zaharenko424.a_changed.client.model.SnowLeopardMaleModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

public class SnowLeopard extends TransfurType<LatexBeast> {

    public SnowLeopard(@NotNull ResourceLocation loc, @NotNull Gender gender) {
        super(CatProperties.of(loc, gender == Gender.FEMALE ? EntityRegistry.SNOW_LEOPARD_FEMALE : EntityRegistry.SNOW_LEOPARD_MALE)
                .gender(gender).maxHealthModifier(4).colors(-6513508, -263173)
                .addAbility(AbilityRegistry.GRAB_ABILITY));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, gender == Gender.FEMALE ? SnowLeopardFemaleModel::new : SnowLeopardMaleModel::new);
    }
}