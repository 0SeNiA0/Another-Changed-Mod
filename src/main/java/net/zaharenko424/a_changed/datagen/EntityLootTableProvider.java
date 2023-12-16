package net.zaharenko424.a_changed.datagen;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static net.zaharenko424.a_changed.registry.EntityRegistry.*;

public class EntityLootTableProvider extends EntityLootSubProvider {
    public EntityLootTableProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        add(DARK_LATEX_WOLF_FEMALE.get(), new LootTable.Builder()
                .withPool(LootPool.lootPool()
                        .add(latex(false))));//TODO finish loot tables
        add(DARK_LATEX_WOLF_MALE.get(), new LootTable.Builder()
                .withPool(LootPool.lootPool()
                        .add(latex(false))));

        add(GAS_WOLF.get(), new LootTable.Builder()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(ItemRegistry.ORANGE_ITEM)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))));

        add(WHITE_LATEX_WOLF_FEMALE.get(), new LootTable.Builder()
                .withPool(LootPool.lootPool()
                        .add(latex(true))));
        add(WHITE_LATEX_WOLF_MALE.get(), new LootTable.Builder()
                .withPool(LootPool.lootPool()
                        .add(latex(true))));
    }

    private LootPoolEntryContainer.@NotNull Builder<?> latex(boolean white){
        return LootItem.lootTableItem(white?ItemRegistry.WHITE_LATEX_ITEM:ItemRegistry.DARK_LATEX_ITEM)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2)))
                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0,1)));
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return ENTITIES.getEntries().stream().map(DeferredHolder::get);
    }
}