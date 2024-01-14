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
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.block.blocks.Crystal;
import net.zaharenko424.a_changed.block.blocks.TallCrystal;
import net.zaharenko424.a_changed.block.doors.AbstractTwoByTwoDoor;
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
        dropSelf(BLUE_LAB_TILE.get());
        dropSelf(BOLTED_BLUE_LAB_TILE.get());
        dropSelf(BOLTED_LAB_TILE.get());
        dropSelf(BROWN_LAB_BLOCK.get());
        dropSelf(CARDBOARD_BOX.get());
        dropSelf(CARPET_BLOCK.get());
        dropSelf(CHAIR.get());
        dropSelf(COMPUTER.get());
        dropSelf(CONNECTED_BLUE_LAB_TILE.get());
        dropSelf(CONNECTED_LAB_TILE.get());
        dropSelf(DANGER_SIGN.get());
        dropSelf(DARK_LATEX_BLOCK.get());
        crystalDrops(DARK_LATEX_CRYSTAL.get(), ItemRegistry.DARK_LATEX_CRYSTAL_SHARD);
        dropWhenSilkTouch(DARK_LATEX_CRYSTAL_ICE.get());
        doublePartCrystal(GREEN_CRYSTAL.get(), ItemRegistry.GREEN_CRYSTAL_SHARD);
        dropSelf(HAZARD_BLOCK.get());
        dropSelf(HAZARD_LAB_BLOCK.get());
        dropSelf(KEYPAD.get());
        dropSelf(LAB_BLOCK.get());
        twoByTwoDoorDrops(LAB_DOOR.get());
        dropSelf(LAB_TILE.get());
        dropSelf(LASER_EMITTER.get());
        doublePartBlockDrops(LATEX_CONTAINER.get());
        twoByTwoDoorDrops(LIBRARY_DOOR.get());
        twoByTwoDoorDrops(MAINTENANCE_DOOR.get());
        doublePartBlockDrops(METAL_BOX.get());
        dropOther(NOTE.get(), Items.PAPER);
        dropSelf(NOTEPAD.get());
        dropSelf(ORANGE_LAB_BLOCK.get());
        createOrangeLeavesDrops();
        dropSelf(ORANGE_SAPLING.get());
        dropSelf(ORANGE_TREE_LOG.get());
        dropSelf(PIPE.get());
        dropSelf(SCANNER.get());
        dropSelf(SMART_SEWAGE_SYSTEM.get());
        dropSelf(STRIPED_ORANGE_LAB_BLOCK.get());
        dropSelf(TABLE.get());
        doublePartBlockDrops(TALL_CARDBOARD_BOX.get());
        dropSelf(TRAFFIC_CONE.get());
        dropSelf(VENT.get());
        dropSelf(VENT_WALL.get());
        dropSelf(WHITE_LATEX_BLOCK.get());
        dropSelf(WHITE_LATEX_PUDDLE_F.get());
        dropSelf(WHITE_LATEX_PUDDLE_M.get());
        dropSelf(YELLOW_LAB_BLOCK.get());
    }

    private void createOrangeLeavesDrops(){
        Block leaves=ORANGE_LEAVES.get();
        add(leaves, createLeavesDrops(leaves,ORANGE_SAPLING.get(),NORMAL_LEAVES_SAPLING_CHANCES)
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

    private void doublePartBlockDrops(Block block){
        add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StateProperties.PART2,0)))
                .add(
                        applyExplosionCondition(block,LootItem.lootTableItem(block))
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

    private void twoByTwoDoorDrops(AbstractTwoByTwoDoor block){
        add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractTwoByTwoDoor.PART,0)))
                .add(
                        applyExplosionCondition(block,LootItem.lootTableItem(block))
                )));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
    }
}