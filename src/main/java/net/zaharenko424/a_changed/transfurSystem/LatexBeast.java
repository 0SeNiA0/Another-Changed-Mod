package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.ability.AbilityHolder;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface LatexBeast extends AbilityHolder {

    @NotNull TransfurType<?> transfurType();

    @Override
    default @NotNull List<? extends Ability> getAllowedAbilities(){
        return transfurType().abilities;
    }

    void copyEquipment(@NotNull LivingEntity copyFrom);
}
