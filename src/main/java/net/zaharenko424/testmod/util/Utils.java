package net.zaharenko424.testmod.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.zaharenko424.testmod.TestMod;
import org.jetbrains.annotations.NotNull;

public class Utils {

    public static @NotNull ResourceKey<ConfiguredFeature<?,?>> featureKey(@NotNull String str){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,new ResourceLocation(TestMod.MODID,str));
    }
}