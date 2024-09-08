package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CatProperties extends TransfurType.Properties {

    private static final UUID speedBuff = UUID.fromString("1c73d915-bf9c-45e4-8afe-6738244e9191");

    protected CatProperties(ResourceLocation resourceLocation, Latex latex) {
        super(resourceLocation, latex);
        eyeHeight(1.75f, 1.5f);
        airReductionModifier(1);
        swimSpeedModifier(-.5f);
        addModifier(Attributes.MOVEMENT_SPEED, speedBuff, "cat_speed_buff", .2, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAbility(AbilityRegistry.CAT_PASSIVE);
    }

    public static @NotNull CatProperties of(ResourceLocation resourceLocation) {
        return new CatProperties(resourceLocation, Latex.WHITE);
    }

    public static @NotNull CatProperties of(ResourceLocation resourceLocation, Latex latex) {
        return new CatProperties(resourceLocation, latex);
    }
}