package net.zaharenko424.a_changed.datagen.recipe;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.DNATypeRegistry;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static net.zaharenko424.a_changed.registry.ItemRegistry.*;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(p_248933_, lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput out) {
        latexBlockItem(false, out);
        latexBlockItem(true, out);
        //TODO machines
        //TODO air conditioner

        //TEST
        DNAExtractorRecipeBuilder.of(DNATypeRegistry.APPLE_DNA).unlockedByMaterial().save(out);

        LatexEncoderRecipeBuilder.of(TransfurRegistry.PURE_WHITE_LATEX_WOLF_TF.get())
                .setLatexBaseIngredient(WHITE_LATEX_BASE.get().getDefaultInstance())
                .addDNASampleIngredient(DNATypeRegistry.APPLE_DNA)
                .unlockedBy(getHasName(WHITE_LATEX_BASE), has(WHITE_LATEX_BASE))
                .save(out);
        //TEST

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BIG_LAB_DOOR_ITEM)
                .requires(LAB_DOOR_ITEM)
                .requires(LAB_DOOR_ITEM)
                .unlockedBy(getHasName(LAB_DOOR_ITEM), has(LAB_DOOR_ITEM))
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BIG_LIBRARY_DOOR_ITEM)
                .requires(LIBRARY_DOOR_ITEM)
                .requires(LIBRARY_DOOR_ITEM)
                .unlockedBy(getHasName(LIBRARY_DOOR_ITEM), has(LIBRARY_DOOR_ITEM))
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BIG_MAINTENANCE_DOOR_ITEM)
                .requires(MAINTENANCE_DOOR_ITEM)
                .requires(MAINTENANCE_DOOR_ITEM)
                .unlockedBy(getHasName(MAINTENANCE_DOOR_ITEM), has(MAINTENANCE_DOOR_ITEM))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLUE_LAB_TILE_ITEM,24)
                .pattern("CIC")
                .pattern("ICI")
                .pattern("CIC")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Items.LIGHT_BLUE_CONCRETE)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        stonecutting(BLUE_LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem<?>[]{BOLTED_LAB_TILE_ITEM,CONNECTED_BLUE_LAB_TILE_ITEM},has(BLUE_LAB_TILE_ITEM),out);
        stonecutting(BOLTED_BLUE_LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem<?>[]{BLUE_LAB_TILE_ITEM,CONNECTED_BLUE_LAB_TILE_ITEM},has(BOLTED_BLUE_LAB_TILE_ITEM),out);
        stonecutting(CONNECTED_BLUE_LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem<?>[]{BLUE_LAB_TILE_ITEM,BOLTED_BLUE_LAB_TILE_ITEM},has(CONNECTED_BLUE_LAB_TILE_ITEM),out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CARDBOARD,1)
                .pattern("P")
                .pattern("P")
                .pattern("P")
                .define('P', Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CARPET_BLOCK_ITEM,4)
                .pattern("WW")
                .pattern("WW")
                .define('W', ItemTags.WOOL)
                .unlockedBy(getHasName(Items.WHITE_WOOL), has(ItemTags.WOOL))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, CARDBOARD_BOX_ITEM)
                .pattern("CC")
                .pattern("CC")
                .define('C', CARDBOARD)
                .unlockedBy(getHasName(CARDBOARD), has(CARDBOARD))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, CHAIR_ITEM,3)
                .pattern("I ")
                .pattern("II")
                .pattern("BB")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Items.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, CUP_ITEM, 2)
                .pattern("C C")
                .pattern(" C ")
                .define('C', Items.CLAY_BALL)
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, DANGER_SIGN_ITEM,3)
                .pattern(" B ")
                .pattern("BYB")
                .define('Y', Items.YELLOW_CONCRETE)
                .define('B', Items.BLACK_CONCRETE)
                .unlockedBy(getHasName(Items.YELLOW_CONCRETE), has(Items.YELLOW_CONCRETE))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DARK_LATEX_ICE_ITEM, 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', DARK_LATEX_CRYSTAL_SHARD)
                .unlockedBy(getHasName(DARK_LATEX_CRYSTAL_SHARD), has(DARK_LATEX_CRYSTAL_SHARD))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FLASK_ITEM, 3)
                .pattern(" G ")
                .pattern("GGG")
                .define('G', Tags.Items.GLASS)
                .unlockedBy(getHasName(Items.GLASS), has(Tags.Items.GLASS))
                .save(out);

        //TODO gas tank

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HAZARD_BLOCK_ITEM,9)
                .pattern("OOB")
                .pattern("OBO")
                .pattern("BOO")
                .define('O', ORANGE_LAB_BLOCK_ITEM)
                .define('B', Items.BLACK_CONCRETE)
                .unlockedBy(getHasName(Items.ORANGE_CONCRETE), has(Items.ORANGE_CONCRETE))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HAZARD_LAB_BLOCK_ITEM,9)
                .pattern("LLL")
                .pattern("OOO")
                .pattern("LLL")
                .define('L', LAB_BLOCK_ITEM)
                .define('O', ORANGE_LAB_BLOCK_ITEM)
                .unlockedBy(getHasName(LAB_BLOCK_ITEM), has(LAB_BLOCK_ITEM))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, IV_RACK_ITEM, 3)
                .pattern("BBB")
                .pattern(" B ")
                .pattern("III")
                .define('B', Items.IRON_BARS)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        //TODO keypad, hazmat armor

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, LAB_BLOCK_ITEM,24)
                .pattern("QIQ")
                .pattern("IQI")
                .pattern("QIQ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('Q', Items.QUARTZ_BLOCK)
                .unlockedBy("hasQuartz", has(Items.QUARTZ_BLOCK))
                .save(out);

        stonecutting(BOLTED_LAB_TILE_ITEM, RecipeCategory.BUILDING_BLOCKS, new DeferredItem[]{CONNECTED_LAB_TILE_ITEM, LAB_BLOCK_ITEM, LAB_TILE_ITEM}, has(BOLTED_LAB_TILE_ITEM), out);
        stonecutting(CONNECTED_LAB_TILE_ITEM, RecipeCategory.BUILDING_BLOCKS, new DeferredItem[]{BOLTED_LAB_TILE_ITEM, LAB_BLOCK_ITEM, LAB_TILE_ITEM}, has(CONNECTED_LAB_TILE_ITEM), out);
        stonecutting(LAB_BLOCK_ITEM, RecipeCategory.BUILDING_BLOCKS, new DeferredItem[]{BOLTED_LAB_TILE_ITEM, CONNECTED_LAB_TILE_ITEM, LAB_TILE_ITEM}, has(LAB_BLOCK_ITEM), out);
        stonecutting(LAB_TILE_ITEM, RecipeCategory.BUILDING_BLOCKS, new DeferredItem[]{BOLTED_LAB_TILE_ITEM, CONNECTED_LAB_TILE_ITEM, LAB_BLOCK_ITEM}, has(LAB_TILE_ITEM), out);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, LAB_DOOR_ITEM)
                .pattern("III")
                .pattern("PIP")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Blocks.PISTON)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, LAB_DOOR_ITEM, 2)
                .requires(BIG_LAB_DOOR_ITEM)
                .unlockedBy(getHasName(BIG_LAB_DOOR_ITEM), has(BIG_LAB_DOOR_ITEM))
                .save(out, LAB_DOOR_ITEM.getId().withSuffix("_from_big"));

        //TODO latex container, replace with something like "latex resistant glass" or "latex resistant coating"
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LATEX_CONTAINER_ITEM)
                .pattern("III")
                .pattern(" G ")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.GLASS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, LIBRARY_DOOR_ITEM)
                .pattern("IGI")
                .pattern("PGP")
                .pattern("IGI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Blocks.PISTON)
                .define('G', Tags.Items.GLASS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, LIBRARY_DOOR_ITEM, 2)
                .requires(BIG_LIBRARY_DOOR_ITEM)
                .unlockedBy(getHasName(BIG_LIBRARY_DOOR_ITEM), has(BIG_LIBRARY_DOOR_ITEM))
                .save(out, LIBRARY_DOOR_ITEM.getId().withSuffix("_from_big"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, MAINTENANCE_DOOR_ITEM)
                .pattern("IPI")
                .pattern("III")
                .pattern("IPI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Blocks.PISTON)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, MAINTENANCE_DOOR_ITEM, 2)
                .requires(BIG_MAINTENANCE_DOOR_ITEM)
                .unlockedBy(getHasName(BIG_MAINTENANCE_DOOR_ITEM), has(BIG_MAINTENANCE_DOOR_ITEM))
                .save(out, MAINTENANCE_DOOR_ITEM.getId().withSuffix("_from_big"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, METAL_BOX_ITEM)
                .pattern("III")
                .pattern("I I")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, NOTEPAD_ITEM)
                .pattern("I ")
                .pattern("IP")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ORANGE_JUICE_ITEM)
                .requires(ORANGE_ITEM,4)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.SUGAR)
                .unlockedBy(getHasName(ORANGE_ITEM), has(ORANGE_ITEM))
                .save(out);

        buttonBuilder(ORANGE_BUTTON_ITEM, Ingredient.of(ORANGE_PLANKS_ITEM))
                .unlockedBy(getHasName(ORANGE_PLANKS_ITEM), has(ORANGE_PLANKS_ITEM))
                .save(out);

        doorBuilder(ORANGE_DOOR_ITEM, Ingredient.of(ORANGE_PLANKS_ITEM))
                .unlockedBy(getHasName(ORANGE_PLANKS_ITEM), has(ORANGE_PLANKS_ITEM))
                .save(out);

        fenceBuilder(ORANGE_FENCE_ITEM, Ingredient.of(ORANGE_PLANKS_ITEM))
                .unlockedBy(getHasName(ORANGE_PLANKS_ITEM), has(ORANGE_PLANKS_ITEM))
                .save(out);

        fenceGateBuilder(ORANGE_FENCE_GATE_ITEM, Ingredient.of(ORANGE_PLANKS_ITEM))
                .unlockedBy(getHasName(ORANGE_PLANKS_ITEM), has(ORANGE_PLANKS_ITEM))
                .save(out);

        hangingSign(out, ORANGE_HANGING_SIGN_ITEM, ORANGE_PLANKS_ITEM);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ORANGE_LAB_BLOCK_ITEM,24)
                .pattern("CIC")
                .pattern("ICI")
                .pattern("CIC")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Items.ORANGE_CONCRETE)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        stonecutting(ORANGE_LAB_BLOCK_ITEM, RecipeCategory.BUILDING_BLOCKS, new DeferredItem[]{STRIPED_ORANGE_LAB_BLOCK_ITEM}, has(ORANGE_LAB_BLOCK_ITEM), out);
        stonecutting(STRIPED_ORANGE_LAB_BLOCK_ITEM, RecipeCategory.BUILDING_BLOCKS, new DeferredItem[]{ORANGE_LAB_BLOCK_ITEM}, has(STRIPED_ORANGE_LAB_BLOCK_ITEM), out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ORANGE_PLANKS_ITEM, 4)
                .requires(ORANGE_TREE_LOG_ITEM)
                .unlockedBy(getHasName(ORANGE_TREE_LOG_ITEM), has(ORANGE_TREE_LOG_ITEM))
                .save(out);

        pressurePlateBuilder(RecipeCategory.REDSTONE, ORANGE_PRESSURE_PLATE_ITEM, Ingredient.of(ORANGE_PLANKS_ITEM))
                .unlockedBy(getHasName(ORANGE_PLANKS_ITEM), has(ORANGE_PLANKS_ITEM))
                .save(out);

        signBuilder(ORANGE_SIGN_ITEM, Ingredient.of(ORANGE_PLANKS_ITEM))
                .unlockedBy(getHasName(ORANGE_PLANKS_ITEM), has(ORANGE_PLANKS_ITEM))
                .save(out);

        slabBuilder(RecipeCategory.BUILDING_BLOCKS, ORANGE_SLAB_ITEM, Ingredient.of(ORANGE_PLANKS_ITEM))
                .unlockedBy(getHasName(ORANGE_PLANKS_ITEM), has(ORANGE_PLANKS_ITEM))
                .save(out);

        stairBuilder(ORANGE_STAIRS_ITEM, Ingredient.of(ORANGE_PLANKS_ITEM))
                .unlockedBy(getHasName(ORANGE_PLANKS_ITEM), has(ORANGE_PLANKS_ITEM))
                .save(out);

        trapdoorBuilder(ORANGE_TRAPDOOR_ITEM, Ingredient.of(ORANGE_PLANKS_ITEM))
                .unlockedBy(getHasName(ORANGE_PLANKS_ITEM), has(ORANGE_PLANKS_ITEM))
                .save(out);

        woodFromLogs(out, ORANGE_WOOD_ITEM, ORANGE_TREE_LOG_ITEM);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PIPE_ITEM, 8)
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        //TODO scanner

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SMALL_CARDBOARD_BOX_ITEM)
                .pattern("CCC")
                .define('C', CARDBOARD)
                .unlockedBy(getHasName(CARDBOARD), has(CARDBOARD))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SYRINGE_ITEM,8)
                .pattern(" II")
                .pattern(" GI")
                .pattern("I  ")
                .define('I', Tags.Items.NUGGETS_IRON)
                .define('G', Tags.Items.GLASS_PANES)
                .unlockedBy("hasIron", has(Tags.Items.INGOTS_IRON))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TABLE_ITEM,3)
                .pattern("III")
                .pattern("B B")
                .pattern("B B")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Items.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TALL_CARDBOARD_BOX_ITEM,1)
                .pattern("CCC")
                .pattern("C C")
                .pattern("CCC")
                .define('C',CARDBOARD)
                .unlockedBy(getHasName(CARDBOARD), has(CARDBOARD))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TRAFFIC_CONE_ITEM,3)
                .pattern(" O ")
                .pattern("BOB")
                .define('O', Items.ORANGE_CONCRETE)
                .define('B', Items.BLACK_CONCRETE)
                .unlockedBy(getHasName(Items.ORANGE_CONCRETE), has(Items.ORANGE_CONCRETE))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, VENT_ITEM,2)
                .pattern("IBI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Items.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BlockRegistry.WHITE_LATEX_PUDDLE_F,3)
                .pattern("L L")
                .pattern(" L ")
                .define('L', WHITE_LATEX_ITEM)
                .unlockedBy(getHasName(WHITE_LATEX_ITEM), has(WHITE_LATEX_ITEM))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BlockRegistry.WHITE_LATEX_PUDDLE_M,3)
                .pattern(" L ")
                .pattern("L L")
                .define('L', WHITE_LATEX_ITEM)
                .unlockedBy(getHasName(WHITE_LATEX_ITEM), has(WHITE_LATEX_ITEM))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, YELLOW_LAB_BLOCK_ITEM,24)
                .pattern("CIC")
                .pattern("ICI")
                .pattern("CIC")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Items.YELLOW_CONCRETE)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);
    }

    private void stonecutting(DeferredItem<?> material, RecipeCategory category, DeferredItem<?> @NotNull [] results, Criterion<?> criterion, RecipeOutput out){
        for(DeferredItem<?> result : results) {
            SingleItemRecipeBuilder.stonecutting((Ingredient.of(material)), category, result)
                    .unlockedBy(getHasName(material), criterion)
                    .save(out, result.getId().withSuffix("_stonecutting_from_" + material.getId().getPath()));
        }
    }

    private void latexBlockItem(boolean white, RecipeOutput out){
        DeferredItem<?> block = white ? WHITE_LATEX_BLOCK_ITEM : DARK_LATEX_BLOCK_ITEM;
        DeferredItem<?> latex = white ? WHITE_LATEX_ITEM : DARK_LATEX_ITEM;
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block,1)
                .pattern("LL")
                .pattern("LL")
                .define('L', latex)
                .unlockedBy(getHasName(latex), has(latex))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, latex,4)
                .requires(block)
                .unlockedBy(getHasName(block), has(block))
                .save(out);
    }
}