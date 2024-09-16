package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

public class CatProperties extends TransfurType.Properties {

    protected CatProperties(ResourceLocation resourceLocation, Latex latex) {
        super(resourceLocation, latex);
        airReductionModifier(1);
        swimSpeedModifier(-.5f);
        addModifier(Attributes.MOVEMENT_SPEED, "cat_speed_buff", .2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAbility(AbilityRegistry.CAT_PASSIVE);
    }

    public static @NotNull CatProperties of(ResourceLocation resourceLocation) {
        return new CatProperties(resourceLocation, Latex.WHITE);
    }

    public static @NotNull CatProperties of(ResourceLocation resourceLocation, Latex latex) {
        return new CatProperties(resourceLocation, latex);
    }
}