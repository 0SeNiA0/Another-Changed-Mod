package net.zaharenko424.testmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;

public class LatexBeast extends AbstractLatexBeast {

    public LatexBeast(@NotNull EntityType<? extends Monster> p_21368_, @NotNull Level p_21369_, @NotNull AbstractTransfurType transfurType) {
        super(p_21368_, p_21369_,transfurType);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes(){
        return baseAttributes();
    }
}