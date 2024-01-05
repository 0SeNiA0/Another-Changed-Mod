package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.datagen.worldgen.BiomeModifierProvider;
import net.zaharenko424.a_changed.datagen.worldgen.ConfiguredFeatureProvider;
import net.zaharenko424.a_changed.datagen.worldgen.PlacedFeatureProvider;
import net.zaharenko424.a_changed.transfurSystem.TransfurDamageSource;
import net.zaharenko424.a_changed.worldgen.Biomes;
import net.zaharenko424.a_changed.datagen.worldgen.biome.DarkLatexBiome;
import net.zaharenko424.a_changed.datagen.worldgen.biome.WhiteLatexBiome;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class DatapackEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, DatapackEntriesProvider::biome)
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureProvider::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatureProvider::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifierProvider::bootstrap)
            .add(Registries.DAMAGE_TYPE, DatapackEntriesProvider::damageType);

    public DatapackEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(AChanged.MODID));
    }

    private static void damageType(BootstapContext<DamageType> context){
        context.register(TransfurDamageSource.solvent,new DamageType("solvent", DamageScaling.ALWAYS,0));
        context.register(TransfurDamageSource.transfur,new DamageType("transfur",DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,0.1f));
    }

    private static void biome(BootstapContext<Biome> context){
        context.register(Biomes.DARK_LATEX_BIOME, DarkLatexBiome.biome(context));
        context.register(Biomes.WHITE_LATEX_BIOME, WhiteLatexBiome.biome(context));
    }
}