package net.zaharenko424.a_changed.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.zaharenko424.a_changed.AChanged;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(MultiNoiseBiomeSourceParameterList.class)
public class MixinMultiNoiseBiomeSourceParameterList {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset$SourceProvider;apply(Ljava/util/function/Function;)Lnet/minecraft/world/level/biome/Climate$ParameterList;"),
            method = "<init>")
    private <T> Function<ResourceKey<Biome>, T> checkGetterType(Function<ResourceKey<Biome>, T> valueGetter, @Local(argsOnly = true) HolderGetter<Biome> biomes){

        AChanged.isSafeToAddBiomes = !biomes.getClass().getName().equals("net.minecraft.core.RegistrySetBuilder$UniversalLookup");

        return valueGetter;
    }
}
