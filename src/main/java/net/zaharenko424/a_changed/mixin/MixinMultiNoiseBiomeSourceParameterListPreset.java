package net.zaharenko424.a_changed.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.worldgen.Biomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(MultiNoiseBiomeSourceParameterList.Preset.class)
public class MixinMultiNoiseBiomeSourceParameterListPreset {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addBiomes(Ljava/util/function/Consumer;)V"),
            method = "generateOverworldBiomes")
    private static <T> void modifyOverworldBiomes(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> key, Operation<Void> original, @Local ImmutableList.Builder<Pair<Climate.ParameterPoint, T>> builder, @Local(argsOnly = true) Function<ResourceKey<Biome>, T> valueGetter){

        if(AChanged.isSafeToAddBiomes) {
            builder.add(new Pair<>(new Climate.ParameterPoint(
                    Climate.Parameter.span(0, 0.5f),
                    Climate.Parameter.span(-.4f, 0.4f),
                    Climate.Parameter.span(.2f, 1),
                    Climate.Parameter.span(0, 1),
                    Climate.Parameter.point(0),
                    Climate.Parameter.span(-1, -.1f),
                    0
            ), valueGetter.apply(Biomes.DARK_LATEX_BIOME)));

            builder.add(new Pair<>(new Climate.ParameterPoint(
                    Climate.Parameter.span(-.5f, 0),
                    Climate.Parameter.span(-.4f, 0.4f),
                    Climate.Parameter.span(.2f, 1),
                    Climate.Parameter.span(0, 1),
                    Climate.Parameter.point(0),
                    Climate.Parameter.span(-1, -.1f),
                    0
            ), valueGetter.apply(Biomes.WHITE_LATEX_BIOME)));
        }

        original.call(instance, key);
    }
}
