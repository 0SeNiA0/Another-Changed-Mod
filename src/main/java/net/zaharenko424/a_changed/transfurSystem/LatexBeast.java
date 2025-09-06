package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.ability.api.AbilityHolder;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import org.jetbrains.annotations.NotNull;

import java.util.SequencedSet;

public interface LatexBeast extends AbilityHolder {

    @NotNull TransfurType<?> transfurType();

    @Override
    default @NotNull SequencedSet<? extends Ability> getAbilities(){
        return transfurType().abilities;
    }

    void copyEquipment(@NotNull LivingEntity copyFrom);
}
