package net.zaharenko424.a_changed.datagen.worldgen.loot_table;

import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public record LabLoot(HolderLookup.Provider registries) implements LootTableSubProvider {

    public static final ResourceKey<LootTable> ENTRANCE_CHEST = key("lab/entrance_chest");
    public static final ResourceKey<LootTable> OFFICE_CHEST = key("lab/rooms/office_chest");//paper, books etc
    public static final ResourceKey<LootTable> DOCTORS_CHEST = key("lab/rooms/doctors_chest");//syringes etc
    public static final ResourceKey<LootTable> STORAGE_CHEST = key("lab/underground/storage/chest");//decorations...
    public static final ResourceKey<LootTable> STORAGE_LOCKED_CHEST = key("lab/underground/storage/locked_chest");//latex stuff, mb latex biome map
    public static final ResourceKey<LootTable> GENERATOR_CHEST = key("lab/underground/generator_room_chest");//tools, iron, copper

    private static @NotNull ResourceKey<LootTable> key(String str){
        return Utils.resourceKey(Registries.LOOT_TABLE, str);
    }

    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        HolderGetter<Biome> biomeGetter = registries.lookupOrThrow(Registries.BIOME);

        LootPool.Builder officePool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(2, 5))
                .add(LootItem.lootTableItem(Items.PAPER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                .add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(LootItem.lootTableItem(Items.WRITABLE_BOOK).setWeight(12))
                .add(LootItem.lootTableItem(ItemRegistry.NOTEPAD_ITEM).setWeight(12))
                .add(LootItem.lootTableItem(ItemRegistry.ORANGE_ITEM).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))))
                .add(LootItem.lootTableItem(ItemRegistry.ORANGE_JUICE_ITEM).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))));

        output.accept(ENTRANCE_CHEST, LootTable.lootTable().withPool(officePool));
        output.accept(OFFICE_CHEST, LootTable.lootTable().withPool(officePool));

        output.accept(DOCTORS_CHEST, LootTable.lootTable().withPool(officePool).withPool(
                latexPool(biomeGetter).setRolls(UniformGenerator.between(1, 3))
        ));

        output.accept(STORAGE_CHEST, LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1, 3))
                        .add(LootItem.lootTableItem(ItemRegistry.CHAIR_ITEM))
                        .add(LootItem.lootTableItem(ItemRegistry.ROTATING_CHAIR_ITEM))
                        .add(LootItem.lootTableItem(ItemRegistry.LIBRARY_DOOR_ITEM))
                        .add(LootItem.lootTableItem(ItemRegistry.BIG_LAB_LAMP_ITEM))
                        .add(LootItem.lootTableItem(ItemRegistry.LAB_LAMP_ITEM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(ItemRegistry.IV_RACK_ITEM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
        ).withPool(
                LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1, 3))
                        .add(LootItem.lootTableItem(ItemRegistry.LAB_BLOCK_ITEM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 10))))
                        .add(LootItem.lootTableItem(ItemRegistry.LAB_STAIRS_ITEM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 10))))
                        .add(LootItem.lootTableItem(ItemRegistry.METAL_CAN_ITEM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
        ));

        output.accept(STORAGE_LOCKED_CHEST, LootTable.lootTable().withPool(
                latexPool(biomeGetter).setRolls(UniformGenerator.between(2, 5))
                        .add(latexSyringe(TransfurRegistry.BEI_FENG_TF))
                        .add(latexSyringe(TransfurRegistry.PURE_WHITE_LATEX_WOLF_TF))
        ).withPool(
                LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1, 3))
                        .add(LootItem.lootTableItem(ItemRegistry.DARK_LATEX_ITEM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                        .add(LootItem.lootTableItem(ItemRegistry.WHITE_LATEX_ITEM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
        ));

        output.accept(GENERATOR_CHEST, LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .setRolls(UniformGenerator.between(2, 5))
                        .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                        .add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
                        .add(LootItem.lootTableItem(ItemRegistry.IRON_PLATE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(ItemRegistry.COPPER_PLATE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                        .add(LootItem.lootTableItem(ItemRegistry.COPPER_WRENCH).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(.15f, .8f))))
                        .add(LootItem.lootTableItem(ItemRegistry.POWER_CELL))
                        .add(LootItem.lootTableItem(ItemRegistry.EMPTY_CANISTER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                        .add(LootItem.lootTableItem(ItemRegistry.COPPER_WIRE_ITEM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
        ));
    }

    private LootPool.Builder latexPool(HolderGetter<Biome> biomeGetter){
        return LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemRegistry.SYRINGE_ITEM).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(latexSyringe(TransfurRegistry.DARK_LATEX_WOLF_F_TF))
                .add(latexSyringe(TransfurRegistry.DARK_LATEX_WOLF_M_TF))
                .add(latexSyringe(TransfurRegistry.WHITE_LATEX_WOLF_F_TF))
                .add(latexSyringe(TransfurRegistry.WHITE_LATEX_WOLF_M_TF))
                .add(latexSyringe(TransfurRegistry.SNOW_LEOPARD_F_TF).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(biomeGetter.getOrThrow(Tags.Biomes.IS_SNOWY)))))
                .add(latexSyringe(TransfurRegistry.SNOW_LEOPARD_M_TF).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(biomeGetter.getOrThrow(Tags.Biomes.IS_SNOWY)))));
    }

    private LootPoolSingletonContainer.Builder<?> latexSyringe(DeferredHolder<TransfurType, ? extends TransfurType> transfurType){
        return LootItem.lootTableItem(ItemRegistry.LATEX_SYRINGE).apply(SetComponentsFunction.setComponent(ComponentRegistry.TRANSFUR_TYPE.get(), transfurType.getId()));
    }
}