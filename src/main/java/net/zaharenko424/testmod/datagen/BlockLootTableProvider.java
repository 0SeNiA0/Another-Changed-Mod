package net.zaharenko424.testmod.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.testmod.block.Box;
import net.zaharenko424.testmod.block.AbstractTwoByTwoDoor;
import net.zaharenko424.testmod.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

import static net.zaharenko424.testmod.registry.BlockRegistry.*;

public class BlockLootTableProvider extends BlockLootSubProvider {
    public BlockLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(BOLTED_LAB_TILE.get());
        doublePartBlockDrops(CARDBOARD_BOX.get());
        dropSelf(BROWN_LAB_BLOCK.get());
        dropSelf(CARPET_BLOCK.get());
        dropSelf(CHAIR.get());
        dropSelf(CONNECTED_LAB_TILE.get());
        dropSelf(DARK_LATEX_BLOCK.get());
        dropSelf(HAZARD_BLOCK.get());
        dropSelf(LAB_BLOCK.get());
        twoByTwoDoorDrops(LAB_DOOR.get());
        dropSelf(LAB_TILE.get());
        doublePartBlockDrops(LATEX_CONTAINER.get());
        twoByTwoDoorDrops(LIBRARY_DOOR.get());
        twoByTwoDoorDrops(MAINTENANCE_DOOR.get());
        doublePartBlockDrops(METAL_BOX.get());
        dropOther(NOTE.get(), Items.PAPER);
        dropSelf(NOTEPAD.get());
        createOrangeLeavesDrops();
        dropSelf(ORANGE_SAPLING.get());
        dropSelf(ORANGE_TREE_LOG.get());
        dropSelf(SCANNER.get());
        dropSelf(TABLE.get());
        dropSelf(VENT.get());
        dropSelf(WHITE_LATEX_BLOCK.get());
        dropSelf(YELLOW_LAB_BLOCK.get());
    }

    private void createOrangeLeavesDrops(){
        Block leaves=ORANGE_LEAVES.get();
        add(leaves,createLeavesDrops(leaves,ORANGE_SAPLING.get(),NORMAL_LEAVES_SAPLING_CHANCES)
        .withPool(
            LootPool.lootPool().when(HAS_SHEARS.invert().and(HAS_NO_SILK_TOUCH))
                .add(
                    applyExplosionCondition(leaves, LootItem.lootTableItem(ItemRegistry.ORANGE_ITEM))
                    .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))
                )
        ));
    }

    private void doublePartBlockDrops(Block block){
        add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(Box.PART,"lower")))
                .add(
                        applyExplosionCondition(block,LootItem.lootTableItem(block))
                )));
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