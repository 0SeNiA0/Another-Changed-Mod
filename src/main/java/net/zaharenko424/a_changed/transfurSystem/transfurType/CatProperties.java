package net.zaharenko424.a_changed.transfurSystem.transfurType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.Latex;
import net.zaharenko424.a_changed.transfurSystem.LatexBeast;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CatProperties <T extends LivingEntity & LatexBeast> extends TransfurType.Properties<T> {

    protected CatProperties(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType, Latex latex) {
        super(resourceLocation, entityType, latex);
        airReductionModifier(1);
        swimSpeedModifier(-.5f);
        addModifier(Attributes.MOVEMENT_SPEED, "cat_speed_buff", .2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAbility(AbilityRegistry.CAT_PASSIVE);
    }

    public static <T extends LivingEntity & LatexBeast> @NotNull CatProperties<T> of(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType) {
        return new CatProperties<>(resourceLocation, entityType, Latex.WHITE);
    }

    public static <T extends LivingEntity & LatexBeast> @NotNull CatProperties<T> of(ResourceLocation resourceLocation, Supplier<EntityType<T>> entityType, Latex latex) {
        return new CatProperties<>(resourceLocation, entityType, latex);
    }
}