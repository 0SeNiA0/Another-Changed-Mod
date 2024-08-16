package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

public class WaterLatexProperties extends TransfurType.Properties {

    protected WaterLatexProperties(ResourceLocation resourceLocation, Latex latex) {
        super(resourceLocation, latex);
        airReductionModifier(-1);
        swimSpeedModifier(2);
        addAbility(AbilityRegistry.FISH_PASSIVE);
    }

    public static @NotNull WaterLatexProperties of(ResourceLocation resourceLocation) {
        return new WaterLatexProperties(resourceLocation, Latex.WHITE);
    }

    public static @NotNull WaterLatexProperties of(ResourceLocation resourceLocation, Latex latex) {
        return new WaterLatexProperties(resourceLocation, latex);
    }
}
