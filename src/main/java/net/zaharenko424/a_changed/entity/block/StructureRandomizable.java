package net.zaharenko424.a_changed.entity.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nullable;

public interface StructureRandomizable {

    String LOOT_TABLE_TAG = RandomizableContainer.LOOT_TABLE_TAG;
    String LOOT_TABLE_SEED_TAG = RandomizableContainer.LOOT_TABLE_SEED_TAG;

    @Nullable
    ResourceKey<LootTable> getLootTable();

    void setLootTable(@Nullable ResourceKey<LootTable> lootTable);

    long getLootTableSeed();

    void setLootTableSeed(long seed);

    void unpackLootTable();

    default boolean tryLoadLootTable(CompoundTag tag) {
        if(!tag.contains(LOOT_TABLE_TAG, 8)) return false;

        setLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(tag.getString(LOOT_TABLE_TAG))));
        if (tag.contains(LOOT_TABLE_SEED_TAG, 4)) {
            setLootTableSeed(tag.getLong(LOOT_TABLE_SEED_TAG));
        } else {
            setLootTableSeed(0L);
        }

        return true;
    }

    default boolean trySaveLootTable(CompoundTag tag) {
        ResourceKey<LootTable> resourcekey = getLootTable();
        if (resourcekey == null) return false;

        tag.putString(LOOT_TABLE_TAG, resourcekey.location().toString());
        long i = getLootTableSeed();
        if (i != 0L) {
            tag.putLong(LOOT_TABLE_SEED_TAG, i);
        }

        return true;
    }
}
