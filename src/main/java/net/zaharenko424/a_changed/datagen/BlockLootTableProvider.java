package net.zaharenko424.a_changed.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.block.AbstractMultiBlock;
import net.zaharenko424.a_changed.block.GrowingFruitBlock;
import net.zaharenko424.a_changed.block.blocks.Crystal;
import net.zaharenko424.a_changed.block.blocks.TallCrystal;
import net.zaharenko424.a_changed.block.machines.AbstractDerelictMachine;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

import static net.zaharenko424.a_changed.registry.BlockRegistry.*;

public class BlockLootTableProvider extends BlockLootSubProvider {
    public BlockLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        doublePartBlockDrops(AIR_CONDITIONER.get());
        ninePartMultiBlockDrops(BIG_LAB_DOOR.get());
        doublePartBlockDrops(BIG_LAB_LAMP.get());
        ninePartMultiBlockDrops(BIG_LIBRARY_DOOR.get());
        ninePartMultiBlockDrops(BIG_MAINTENANCE_DOOR.get());
        dropSelf(BLUE_LAB_TILE.get());
        dropSelf(BLUE_LAB_TILE_SLAB.get());
        dropSelf(BLUE_LAB_TILE_STAIRS.get());
        dropSelf(BOLTED_BLUE_LAB_TILE.get());
        dropSelf(BOLTED_LAB_TILE.get());
        dropSelf(BROKEN_CUP.get());
        dropSelf(BROKEN_FLASK.get());
        dropSelf(BROWN_LAB_BLOCK.get());
        dropSelf(CAPACITOR.get());
        dropSelf(CARDBOARD_BOX.get());
        dropSelf(CARPET_BLOCK.get());
        dropSelf(CHAIR.get());
        dropSelf(COMPRESSOR.get());
        dropSelf(COMPUTER.get());
        dropSelf(CONNECTED_BLUE_LAB_TILE.get());
        dropSelf(CONNECTED_LAB_TILE.get());
        dropSelf(COPPER_WIRE.get());
        twelvePartMultiBlockDrops(CRYO_CHAMBER.get());
        dropOther(CUP.get(), BROKEN_CUP);
        dropSelf(DANGER_SIGN.get());
        dropSelf(DARK_LATEX_BLOCK.get());
        crystalDrops(DARK_LATEX_CRYSTAL.get(), ItemRegistry.DARK_LATEX_CRYSTAL_SHARD);
        dropWhenSilkTouch(DARK_LATEX_CRYSTAL_ICE.get());
        dropSelf(DARK_LATEX_PUDDLE_F.get());
        dropSelf(DARK_LATEX_PUDDLE_M.get());
        derelictMachineDrops(DERELICT_LATEX_ENCODER.get(), 1.5f);
        derelictMachineDrops(DERELICT_LATEX_PURIFIER.get(), 1);
        dropSelf(DNA_EXTRACTOR.get());
        dropSelf(EXPOSED_PIPES.get());
        dropOther(FLASK.get(), BROKEN_FLASK);
        dropSelf(GENERATOR.get());
        doublePartCrystal(GREEN_CRYSTAL.get(), ItemRegistry.GREEN_CRYSTAL_SHARD);
        dropSelf(HAZARD_BLOCK.get());
        dropSelf(HAZARD_SLAB.get());
        dropSelf(HAZARD_STAIRS.get());
        dropSelf(HAZARD_LAB_BLOCK.get());
        doublePartBlockDrops(IV_RACK.get());
        dropSelf(KEYPAD.get());
        dropSelf(LAB_BLOCK.get());
        dropSelf(LAB_SLAB.get());
        dropSelf(LAB_STAIRS.get());
        fourPartMultiBlockDrops(LAB_DOOR.get());
        dropSelf(LAB_LAMP.get());
        dropSelf(LAB_TILE.get());
        dropSelf(LAB_TILE_SLAB.get());
        dropSelf(LAB_TILE_STAIRS.get());
        dropSelf(LASER_EMITTER.get());
        doublePartBlockDrops(LATEX_CONTAINER.get());
        dropSelf(LATEX_ENCODER.get());
        dropSelf(LATEX_PURIFIER.get());
        dropSelf(LATEX_RESISTANT_BLOCK.get());
        dropSelf(LATEX_RESISTANT_GLASS.get());
        dropSelf(LATEX_RESISTANT_GLASS_PANE.get());
        fourPartMultiBlockDrops(LIBRARY_DOOR.get());
        fourPartMultiBlockDrops(MAINTENANCE_DOOR.get());
        doublePartBlockDrops(METAL_BOX.get());
        dropSelf(METAL_CAN.get());
        dropOther(NOTE.get(), Items.PAPER);
        dropSelf(NOTEPAD.get());
        fruitDrops(ORANGE.get());
        dropOther(ORANGE.get(), ItemRegistry.ORANGE_ITEM);
        dropSelf(ORANGE_BUTTON.get());
        add(ORANGE_DOOR.get(), createDoorTable(ORANGE_DOOR.get()));
        dropSelf(ORANGE_FENCE.get());
        dropSelf(ORANGE_FENCE_GATE.get());
        dropSelf(ORANGE_HANGING_SIGN.get());
        dropOther(ORANGE_WALL_HANGING_SIGN.get(), ORANGE_HANGING_SIGN);
        dropSelf(ORANGE_LAB_BLOCK.get());
        dropSelf(ORANGE_LAB_SLAB.get());
        dropSelf(ORANGE_LAB_STAIRS.get());
        createOrangeLeavesDrops();
        dropSelf(ORANGE_PLANKS.get());
        dropSelf(ORANGE_PRESSURE_PLATE.get());
        dropSelf(ORANGE_SIGN.get());
        dropSelf(ORANGE_SAPLING.get());
        dropSelf(ORANGE_SLAB.get());
        dropSelf(ORANGE_STAIRS.get());
        dropSelf(ORANGE_TRAPDOOR.get());
        dropSelf(ORANGE_TREE_LOG.get());
        dropOther(ORANGE_WALL_SIGN.get(), ORANGE_SIGN);
        dropSelf(ORANGE_WOOD.get());
        dropSelf(PIPE.get());
        add(POTTED_ORANGE_SAPLING.get(), createPotFlowerItemTable(ORANGE_SAPLING));
        dropSelf(ROTATING_CHAIR.get());
        dropSelf(SCANNER.get());
        dropSelf(SMART_SEWAGE_SYSTEM.get());
        dropSelf(STRIPED_ORANGE_LAB_BLOCK.get());
        dropSelf(STRIPPED_ORANGE_LOG.get());
        dropSelf(STRIPPED_ORANGE_WOOD.get());
        dropSelf(TABLE.get());
        doublePartBlockDrops(TALL_CARDBOARD_BOX.get());
        dropSelf(TEST_TUBES.get());
        dropSelf(TRAFFIC_CONE.get());
        dropSelf(TV_SCREEN.get());
        dropSelf(VENT_DUCT.get());
        dropSelf(VENT_HATCH.get());
        dropSelf(VENT_WALL.get());
        dropSelf(WHITE_LATEX_BLOCK.get());
        dropSelf(WHITE_LATEX_PUDDLE_F.get());
        dropSelf(WHITE_LATEX_PUDDLE_M.get());
        dropSelf(YELLOW_LAB_BLOCK.get());
        dropSelf(YELLOW_LAB_SLAB.get());
        dropSelf(YELLOW_LAB_STAIRS.get());
    }

    private void createOrangeLeavesDrops(){
        Block leaves=ORANGE_LEAVES.get();
        add(leaves, createLeavesDrops(leaves,ORANGE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES)
        .withPool(
            LootPool.lootPool().when(HAS_SHEARS.invert().and(HAS_NO_SILK_TOUCH))
                .add(
                    applyExplosionCondition(leaves, LootItem.lootTableItem(ItemRegistry.ORANGE_ITEM))
                    .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))
                )
        ));
    }

    private void crystalDrops(Crystal crystal, ItemLike shard){
        add(crystal, createSilkTouchDispatchTable(crystal, LootItem.lootTableItem(shard)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    private void derelictMachineDrops(AbstractDerelictMachine derelictMachine, float dropMultiplier){
        add(derelictMachine, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(
                        applyExplosionCondition(derelictMachine, LootItem.lootTableItem(ItemRegistry.IRON_PLATE)
                                .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, .5f * dropMultiplier))))
                )
        ).withPool(LootPool.lootPool()
                .add(
                        applyExplosionCondition(derelictMachine, LootItem.lootTableItem(ItemRegistry.GOLDEN_PLATE)
                                .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, .25f * dropMultiplier))))
                )
        ).withPool(LootPool.lootPool()
                .add(
                        applyExplosionCondition(derelictMachine, LootItem.lootTableItem(Items.DIAMOND)
                                .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, .1f * dropMultiplier))))
                )
        ));
    }

    private void doublePartBlockDrops(Block block){
        add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StateProperties.PART2,0)))
                .add(
                        applyExplosionCondition(block, LootItem.lootTableItem(block))
                )));
    }

    private void doublePartCrystal(TallCrystal crystal, ItemLike shard){
        add(crystal, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(crystal)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StateProperties.PART2,0)))
                        .add(applyExplosionCondition(crystal, LootItem.lootTableItem(crystal).when(HAS_SILK_TOUCH)
                                .otherwise(LootItem.lootTableItem(shard)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2)))
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))))
                ));
    }

    private void twelvePartMultiBlockDrops(AbstractMultiBlock block){
        add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StateProperties.PART12, 0)))
                .add(
                        applyExplosionCondition(block, LootItem.lootTableItem(block))
                )));
    }

    private void ninePartMultiBlockDrops(AbstractMultiBlock block){
        add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StateProperties.PART9,0)))
                .add(
                        applyExplosionCondition(block, LootItem.lootTableItem(block))
                )));
    }

    private void fruitDrops(GrowingFruitBlock fruitBlock){
        add(fruitBlock, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(fruitBlock)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(fruitBlock.ageProperty(), fruitBlock.maxAge())))
                .add(
                        applyExplosionCondition(fruitBlock, LootItem.lootTableItem(fruitBlock.getFruitItem()))
                )));
    }

    private void fourPartMultiBlockDrops(AbstractMultiBlock block){
        add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StateProperties.PART4,0)))
                .add(
                        applyExplosionCondition(block, LootItem.lootTableItem(block))
                )));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
    }
}