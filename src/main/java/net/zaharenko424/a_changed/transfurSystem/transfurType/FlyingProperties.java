package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.Latex;
import net.zaharenko424.a_changed.transfurSystem.LatexBeast;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FlyingProperties <T extends LivingEntity & LatexBeast> extends TransfurType.Properties<T> {

    protected FlyingProperties(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType, Latex latex) {
        super(resourceLocation, entityType, latex);
        airReductionModifier(.5f);
        swimSpeedModifier(-.25f);
        addAbility(AbilityRegistry.FALL_FLYING_PASSIVE);
    }

    public static <T extends LivingEntity & LatexBeast> @NotNull FlyingProperties<T> of(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType) {
        return new FlyingProperties<>(resourceLocation, entityType, Latex.WHITE);
    }

    public static <T extends LivingEntity & LatexBeast> @NotNull FlyingProperties<T> of(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType, Latex latex) {
        return new FlyingProperties<>(resourceLocation, entityType, latex);
    }
}