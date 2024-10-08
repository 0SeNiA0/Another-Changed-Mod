package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

public class SnowLeopard extends TransfurType {

    public SnowLeopard(@NotNull ResourceLocation loc, @NotNull Gender gender) {
        super(CatProperties.of(loc).gender(gender).maxHealthModifier(4).colors(-6513508, -263173)
                .addAbility(AbilityRegistry.GRAB_ABILITY),
                FMLLoader.getDist().isClient() ? ClientOnly.snowLeopardModel(gender) : null);
    }
}