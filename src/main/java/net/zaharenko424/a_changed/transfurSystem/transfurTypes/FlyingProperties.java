package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

public class FlyingProperties extends TransfurType.Properties {

    protected FlyingProperties(ResourceLocation resourceLocation, Latex latex) {
        super(resourceLocation, latex);
        airReductionModifier(.5f);
        swimSpeedModifier(-.25f);
        addAbility(AbilityRegistry.FALL_FLYING_PASSIVE);
    }

    public static @NotNull FlyingProperties of(ResourceLocation resourceLocation) {
        return new FlyingProperties(resourceLocation, Latex.WHITE);
    }

    public static @NotNull FlyingProperties of(ResourceLocation resourceLocation, Latex latex) {
        return new FlyingProperties(resourceLocation, latex);
    }
}