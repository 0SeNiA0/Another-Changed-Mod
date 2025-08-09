package net.zaharenko424.a_changed.datagen.worldgen.template_pool;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.datagen.DatapackEntriesProvider;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RuinedLabPools {

    public static final ResourceKey<StructureTemplatePool> START = key("ruined_lab/entrance");
    private static final ResourceKey<StructureTemplatePool> STAIRCASE = key("ruined_lab/staircase");
    private static final ResourceKey<StructureTemplatePool> STAIRCASE_UP = key("ruined_lab/staircase_up");
    private static final ResourceKey<StructureTemplatePool> STAIRCASE_DOWN = key("ruined_lab/staircase_down");
    private static final ResourceKey<StructureTemplatePool> ROOMS_UNDERGROUND = key("ruined_lab/underground");

    public static void bootstrap(@NotNull BootstrapContext<StructureTemplatePool> context){
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<StructureProcessorList> processorGetter = context.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureTemplatePool> empty = poolGetter.getOrThrow(Pools.EMPTY);
        Holder<StructureProcessorList> rot = processorGetter.getOrThrow(DatapackEntriesProvider.LAB_ROT);

        context.register(START, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/entrance_0"), rot), 1),
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/entrance_1"), rot), 1),
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/entrance_2"), rot), 1),
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/entrance_3"), rot), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        //pool for stairs(up / up&down / empty -> fallback)
        context.register(STAIRCASE, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/staircase/up"), rot), 1),
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/staircase/updown"), rot), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        //pool for room on top of stairs(more stairs / end stairs)
        context.register(STAIRCASE_UP, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/staircase/down"), rot), 1),
                        Pair.of(StructurePoolElement.empty(), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        //pool for room after stairs down
        Holder<StructureTemplatePool> h = context.register(ROOMS_UNDERGROUND, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(StructurePoolElement.empty(), 1),
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/underground/storage"), rot), 1),
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/underground/generator_room"), rot), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        //pool for room below stairs(more stairs / end stairs)
        context.register(STAIRCASE_DOWN, new StructureTemplatePool(
                h,
                List.of(
                        Pair.of(StructurePoolElement.single(loc("ruined_lab/staircase/up"), rot), 3)
                ),
                StructureTemplatePool.Projection.RIGID
        ));
    }

    private static @NotNull ResourceKey<StructureTemplatePool> key(String str){
        return Utils.resourceKey(Registries.TEMPLATE_POOL, str);
    }

    @Contract(pure = true)
    private static @NotNull String loc(String str){
        return AChanged.MODID + ":" + str;
    }
}
