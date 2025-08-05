package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.Latex;
import net.zaharenko424.a_changed.transfurSystem.LatexBeast;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class WaterLatexProperties <T extends LivingEntity & LatexBeast> extends TransfurType.Properties<T> {

    protected WaterLatexProperties(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType, Latex latex) {
        super(resourceLocation, entityType, latex);
        airReductionModifier(-1);
        swimSpeedModifier(2);
        addAbility(AbilityRegistry.FISH_PASSIVE);
    }

    public static <T extends LivingEntity & LatexBeast> @NotNull WaterLatexProperties<T> of(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType) {
        return new WaterLatexProperties<>(resourceLocation, entityType, Latex.WHITE);
    }

    public static <T extends LivingEntity & LatexBeast> @NotNull WaterLatexProperties<T> of(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType, Latex latex) {
        return new WaterLatexProperties<>(resourceLocation, entityType, latex);
    }
}
