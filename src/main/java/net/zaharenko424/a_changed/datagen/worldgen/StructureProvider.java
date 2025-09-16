package net.zaharenko424.a_changed.datagen.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.datagen.worldgen.template_pool.LabPools;
import net.zaharenko424.a_changed.datagen.worldgen.template_pool.RuinedLabPools;
import net.zaharenko424.a_changed.worldgen.Structures;

import java.util.List;
import java.util.Optional;

public class StructureProvider {

    public static void bootstrap(BootstrapContext<Structure> context){
        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);

        context.register(Structures.LAB, new JigsawStructure(
                new Structure.StructureSettings.Builder(
                        HolderSet.direct(
                                biomeGetter.getOrThrow(Biomes.PLAINS), biomeGetter.getOrThrow(Biomes.SNOWY_PLAINS),
                                biomeGetter.getOrThrow(Biomes.MEADOW), biomeGetter.getOrThrow(Biomes.SAVANNA),
                                biomeGetter.getOrThrow(Biomes.SUNFLOWER_PLAINS), biomeGetter.getOrThrow(Biomes.DESERT),
                                biomeGetter.getOrThrow(Biomes.BADLANDS)
                        )
                ).terrainAdapation(TerrainAdjustment.BEARD_THIN).build(),
                poolGetter.getOrThrow(LabPools.START),
                Optional.of(AChanged.resourceLoc("start")),
                4,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                false,
                Optional.of(Heightmap.Types.WORLD_SURFACE),
                64,
                List.of(),
                JigsawStructure.DEFAULT_DIMENSION_PADDING,
                LiquidSettings.IGNORE_WATERLOGGING
        ));

        context.register(Structures.RUINED_LAB, new JigsawStructure(
                new Structure.StructureSettings.Builder(
                        HolderSet.direct(
                                biomeGetter.getOrThrow(Biomes.PLAINS), biomeGetter.getOrThrow(Biomes.SNOWY_PLAINS),
                                biomeGetter.getOrThrow(Biomes.MEADOW), biomeGetter.getOrThrow(Biomes.SAVANNA),
                                biomeGetter.getOrThrow(Biomes.SUNFLOWER_PLAINS), biomeGetter.getOrThrow(Biomes.DESERT),
                                biomeGetter.getOrThrow(Biomes.BADLANDS)
                        )
                ).terrainAdapation(TerrainAdjustment.BEARD_THIN).build(),
                poolGetter.getOrThrow(RuinedLabPools.START),
                Optional.of(AChanged.resourceLoc("start")),
                2,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                false,
                Optional.of(Heightmap.Types.WORLD_SURFACE),
                64,
                List.of(),
                JigsawStructure.DEFAULT_DIMENSION_PADDING,
                LiquidSettings.IGNORE_WATERLOGGING
        ));
    }

    public static void structureSet(BootstrapContext<StructureSet> context){
        HolderGetter<Structure> structureGetter = context.lookup(Registries.STRUCTURE);

        context.register(Structures.LAB_SET, new StructureSet(
                List.of(
                        StructureSet.entry(structureGetter.getOrThrow(Structures.LAB)),
                        StructureSet.entry(structureGetter.getOrThrow(Structures.RUINED_LAB))
                ),
                new RandomSpreadStructurePlacement(24, 16, RandomSpreadType.LINEAR, 1212121)));
    }
}