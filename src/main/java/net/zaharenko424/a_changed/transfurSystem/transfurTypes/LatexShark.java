package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.model.CustomEntityModel;
import net.zaharenko424.a_changed.client.model.LatexSharkFemaleModel;
import net.zaharenko424.a_changed.client.model.LatexSharkMaleModel;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

public class LatexShark extends AbstractWaterLatex {

    public LatexShark(@NotNull Properties properties) {
        super(properties.maxHealthModifier(8));
    }

    @Override
    public <E extends LivingEntity> CustomEntityModel<E> getModel(int modelVariant) {
        return gender == Gender.FEMALE ? new LatexSharkFemaleModel<>() : new LatexSharkMaleModel<>();
    }
}