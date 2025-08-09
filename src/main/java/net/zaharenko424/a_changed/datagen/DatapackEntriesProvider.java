package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.datagen.worldgen.BiomeModifierProvider;
import net.zaharenko424.a_changed.datagen.worldgen.ConfiguredFeatureProvider;
import net.zaharenko424.a_changed.datagen.worldgen.PlacedFeatureProvider;
import net.zaharenko424.a_changed.datagen.worldgen.StructureProvider;
import net.zaharenko424.a_changed.datagen.worldgen.biome.DarkLatexBiome;
import net.zaharenko424.a_changed.datagen.worldgen.biome.WhiteLatexBiome;
import net.zaharenko424.a_changed.datagen.worldgen.template_pool.LabPools;
import net.zaharenko424.a_changed.datagen.worldgen.template_pool.RuinedLabPools;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.util.Utils;
import net.zaharenko424.a_changed.worldgen.Biomes;
import net.zaharenko424.a_changed.worldgen.LabRotProcessor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class DatapackEntriesProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, DatapackEntriesProvider::biome)
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureProvider::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatureProvider::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifierProvider::bootstrap)
            .add(Registries.DAMAGE_TYPE, DatapackEntriesProvider::damageType)
            .add(Registries.PROCESSOR_LIST, DatapackEntriesProvider::processorList)
            .add(Registries.TEMPLATE_POOL, DatapackEntriesProvider::templatePools)
            .add(Registries.STRUCTURE, StructureProvider::bootstrap)
            .add(Registries.STRUCTURE_SET, StructureProvider::structureSet);

    public DatapackEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(AChanged.MODID));
    }

    private static void damageType(BootstrapContext<DamageType> context){
        context.register(DamageSources.assimilation, new DamageType("assimilation", DamageScaling.NEVER, 0));
        context.register(DamageSources.electricity, new DamageType("electricity", DamageScaling.NEVER, .1f));
        context.register(DamageSources.solvent, new DamageType("solvent", DamageScaling.ALWAYS,.1f, DamageEffects.BURNING));
        context.register(DamageSources.syringe, new DamageType("syringe", DamageScaling.ALWAYS, .1f, DamageEffects.POKING));
        context.register(DamageSources.placedSyringe, new DamageType("placed_syringe", DamageScaling.NEVER, .1f, DamageEffects.POKING));
        context.register(DamageSources.transfur, new DamageType("transfur", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,0.1f));
        context.register(DamageSources.transfurKill, new DamageType("transfur_kill", DamageScaling.NEVER, 0));
        context.register(DamageSources.untransfur, new DamageType("untransfur", DamageScaling.NEVER, 0));
        context.register(DamageSources.untransfurKill, new DamageType("untransfur_kill", DamageScaling.NEVER, 0));
    }

    private static void biome(BootstrapContext<Biome> context){
        context.register(Biomes.DARK_LATEX_BIOME, DarkLatexBiome.biome(context));
        context.register(Biomes.WHITE_LATEX_BIOME, WhiteLatexBiome.biome(context));
    }

    public static final ResourceKey<StructureProcessorList> LAB_ROT = Utils.resourceKey(Registries.PROCESSOR_LIST, "lab_rot");
    private static void processorList(BootstrapContext<StructureProcessorList> context){
        context.register(LAB_ROT, new StructureProcessorList(List.of(new LabRotProcessor(.9f, context.lookup(Registries.BLOCK).getOrThrow(BlockTagProvider.LAB_ROT_PROTECTED)))));
    }

    private static void templatePools(BootstrapContext<StructureTemplatePool> context){
        LabPools.bootstrap(context);
        RuinedLabPools.bootstrap(context);
    }
}