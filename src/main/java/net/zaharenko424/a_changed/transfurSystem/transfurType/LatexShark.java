package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.cmrs.event.RegisterBuiltInModelsEvent;
import net.zaharenko424.a_changed.client.model.LatexSharkFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexSharkMaleModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

public class LatexShark extends TransfurType {

    public LatexShark(@NotNull ResourceLocation loc, @NotNull Gender gender) {
        super(WaterLatexProperties.of(loc)
                .gender(gender).maxHealthModifier(8).colors(-6908266, -1644826)
                .addAbility(AbilityRegistry.GRAB_ABILITY));
    }

    @Override
    public void registerModels(@NotNull RegisterBuiltInModelsEvent event) {
        event.registerModelSupplier(id, gender == Gender.FEMALE ? LatexSharkFemaleModel::new : LatexSharkMaleModel::new);
    }
}