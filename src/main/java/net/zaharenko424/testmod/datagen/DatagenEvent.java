package net.zaharenko424.testmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.zaharenko424.testmod.TestMod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TestMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DatagenEvent {
    @SubscribeEvent
    public static void onDataGather(@NotNull GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        PackOutput out = generator.getPackOutput();

        generator.addProvider(event.includeClient(), new BlockStateProvider(out, helper));
        generator.addProvider(event.includeClient(), new ItemModelProvider(out, helper));
        generator.addProvider(event.includeClient(), new SoundDefinitionProvider(out, helper));

        CompletableFuture<HolderLookup.Provider> lookup0=
                generator.addProvider(event.includeServer(), new DatapackEntriesProvider(out,lookup)).getRegistryProvider();
        generator.addProvider(event.includeServer(), new DamageTypeTagProvider(out,lookup0,helper));

        generator.addProvider(event.includeServer(), new RecipeProvider(out,lookup));
        generator.addProvider(event.includeServer(), new LootTableProvider(out, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLootTableProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(EntityLootTableProvider::new, LootContextParamSets.ENTITY)
        )));

        BlockTagProvider tagProvider = generator.addProvider(event.includeServer(),new BlockTagProvider(out,lookup,helper));
        generator.addProvider(event.includeServer(), new ItemTagProvider(out,lookup,tagProvider.contentsGetter(),helper));
        generator.addProvider(event.includeServer(), new EntityTypeTagProvider(out,lookup,helper));
    }
}