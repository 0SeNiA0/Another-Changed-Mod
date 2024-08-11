package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

public class LatexShark extends AbstractWaterLatex {

    public LatexShark(@NotNull ResourceLocation loc, @NotNull Gender gender) {
        super(WaterLatexProperties.of(loc).gender(gender).maxHealthModifier(8).colors(-6908266, -1644826)
                .addAbility(AbilityRegistry.GRAB_ABILITY),
                FMLLoader.getDist().isClient() ? ClientOnly.latexSharkModel(gender) : null);
    }
}