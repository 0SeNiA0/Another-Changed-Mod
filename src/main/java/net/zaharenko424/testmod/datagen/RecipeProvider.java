package net.zaharenko424.testmod.datagen;

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
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static net.zaharenko424.testmod.registry.ItemRegistry.*;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(p_248933_, lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput p_301172_) {
        latexBlockItem(false,p_301172_);
        latexBlockItem(true,p_301172_);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BLUE_LAB_TILE_ITEM,24)
                .pattern("CIC")
                .pattern("ICI")
                .pattern("CIC")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Items.LIGHT_BLUE_CONCRETE)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,BLUE_LAB_TILE_ITEM.getId());

        stonecutting(BLUE_LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem<?>[]{BOLTED_LAB_TILE_ITEM,CONNECTED_BLUE_LAB_TILE_ITEM},has(BLUE_LAB_TILE_ITEM),p_301172_);
        stonecutting(BOLTED_BLUE_LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem<?>[]{BLUE_LAB_TILE_ITEM,CONNECTED_BLUE_LAB_TILE_ITEM},has(BOLTED_BLUE_LAB_TILE_ITEM),p_301172_);
        stonecutting(CONNECTED_BLUE_LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem<?>[]{BLUE_LAB_TILE_ITEM,BOLTED_BLUE_LAB_TILE_ITEM},has(CONNECTED_BLUE_LAB_TILE_ITEM),p_301172_);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,CARDBOARD,1)
                .pattern("P")
                .pattern("P")
                .pattern("P")
                .define('P',Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(p_301172_,CARDBOARD.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,CARPET_BLOCK_ITEM,4)
                .pattern("WW")
                .pattern("WW")
                .define('W', ItemTags.WOOL)
                .unlockedBy(getHasName(Items.WHITE_WOOL),has(ItemTags.WOOL))
                .save(p_301172_,CARPET_BLOCK_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS,CARDBOARD_BOX_ITEM)
                .pattern("CC")
                .pattern("CC")
                .define('C', CARDBOARD)
                .unlockedBy(getHasName(CARDBOARD),has(CARDBOARD))
                .save(p_301172_,CARDBOARD_BOX_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS,CHAIR_ITEM,3)
                .pattern("I ")
                .pattern("II")
                .pattern("BB")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B',Items.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,CHAIR_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,HAZARD_BLOCK_ITEM,8)
                .pattern("BO")
                .pattern("OB")
                .define('O', Items.ORANGE_CONCRETE)
                .define('B', Items.BLACK_CONCRETE)
                .unlockedBy(getHasName(Items.ORANGE_CONCRETE),has(Items.ORANGE_CONCRETE))
                .save(p_301172_,HAZARD_BLOCK_ITEM.getId());

        //TODO keypad, hazmat armor

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,LAB_BLOCK_ITEM,24)
                .pattern("QIQ")
                .pattern("IQI")
                .pattern("QIQ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('Q',Items.QUARTZ_BLOCK)
                .unlockedBy("hasQuartz",has(Items.QUARTZ_BLOCK))
                .save(p_301172_,LAB_BLOCK_ITEM.getId());

        stonecutting(BOLTED_LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem[]{CONNECTED_LAB_TILE_ITEM,LAB_BLOCK_ITEM,LAB_TILE_ITEM}, has(BOLTED_LAB_TILE_ITEM),p_301172_);
        stonecutting(CONNECTED_LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem[]{BOLTED_LAB_TILE_ITEM, LAB_BLOCK_ITEM, LAB_TILE_ITEM}, has(CONNECTED_LAB_TILE_ITEM),p_301172_);
        stonecutting(LAB_BLOCK_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem[]{BOLTED_LAB_TILE_ITEM,CONNECTED_LAB_TILE_ITEM,LAB_TILE_ITEM}, has(LAB_BLOCK_ITEM),p_301172_);
        stonecutting(LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem[]{BOLTED_LAB_TILE_ITEM,CONNECTED_LAB_TILE_ITEM,LAB_BLOCK_ITEM}, has(LAB_TILE_ITEM),p_301172_);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,LAB_DOOR_ITEM)
                .pattern("III")
                .pattern("PIP")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Blocks.PISTON)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,LAB_DOOR_ITEM.getId());

        //TODO latex container, replace with something like "latex resistant glass" or "latex resistant coating"
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,LATEX_CONTAINER_ITEM)
                .pattern("III")
                .pattern(" G ")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.GLASS)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,LATEX_CONTAINER_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,LIBRARY_DOOR_ITEM)
                .pattern("IGI")
                .pattern("PGP")
                .pattern("IGI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Blocks.PISTON)
                .define('G', Tags.Items.GLASS)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,LIBRARY_DOOR_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,MAINTENANCE_DOOR_ITEM)
                .pattern("IPI")
                .pattern("III")
                .pattern("IPI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Blocks.PISTON)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,MAINTENANCE_DOOR_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS,METAL_BOX_ITEM)
                .pattern("III")
                .pattern("I I")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,METAL_BOX_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS,NOTEPAD_ITEM)
                .pattern("I ")
                .pattern("IP")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(p_301172_,NOTEPAD_ITEM.getId());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD,ORANGE_JUICE_ITEM)
                .requires(ORANGE_ITEM,4)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.SUGAR)
                .unlockedBy("hasOrange",has(ORANGE_ITEM))
                .save(p_301172_,ORANGE_JUICE_ITEM.getId());

        //TODO scanner

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS,SMALL_CARDBOARD_BOX_ITEM)
                .pattern("CCC")
                .define('C',CARDBOARD)
                .unlockedBy(getHasName(CARDBOARD),has(CARDBOARD))
                .save(p_301172_,SMALL_CARDBOARD_BOX_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,SYRINGE_ITEM,8)
                .pattern(" II")
                .pattern(" GI")
                .pattern("I  ")
                .define('I', Tags.Items.NUGGETS_IRON)
                .define('G', Tags.Items.GLASS_PANES)
                .unlockedBy("hasIron",has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,SYRINGE_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS,TABLE_ITEM,3)
                .pattern("III")
                .pattern("B B")
                .pattern("B B")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B',Items.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,TABLE_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TALL_CARDBOARD_BOX_ITEM,1)
                .pattern("CCC")
                .pattern("C C")
                .pattern("CCC")
                .define('C',CARDBOARD)
                .unlockedBy(getHasName(CARDBOARD),has(CARDBOARD))
                .save(p_301172_, TALL_CARDBOARD_BOX_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS,TRAFFIC_CONE_ITEM,3)
                .pattern(" O ")
                .pattern("B B")
                .define('O', Items.ORANGE_CONCRETE)
                .define('B', Items.BLACK_CONCRETE)
                .unlockedBy(getHasName(Items.ORANGE_CONCRETE),has(Items.ORANGE_CONCRETE))
                .save(p_301172_,TRAFFIC_CONE_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,VENT_ITEM,2)
                .pattern("IBI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B',Items.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_INGOT),has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,VENT_ITEM.getId());
    }

    private void stonecutting(DeferredItem<?> material, RecipeCategory category, DeferredItem<?> @NotNull [] results, Criterion<?> criterion, RecipeOutput out){
        for(DeferredItem<?> result:results) {
            SingleItemRecipeBuilder.stonecutting((Ingredient.of(material)), category, result).unlockedBy(getHasName(material),criterion).save(out,result.getId().withSuffix("_stonecutting_from_"+material.getId().getPath()));
        }
    }

    private void latexBlockItem(boolean white, RecipeOutput out){
        DeferredItem<?> block=white?WHITE_LATEX_BLOCK_ITEM:DARK_LATEX_BLOCK_ITEM;
        DeferredItem<?> latex=white?WHITE_LATEX_ITEM:DARK_LATEX_ITEM;
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,block,1)
                .pattern("LL")
                .pattern("LL")
                .define('L',latex)
                .unlockedBy("has"+(white?"White":"Dark")+"Latex",has(latex))
                .save(out,block.getId());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,latex,4)
                .requires(block)
                .unlockedBy("has"+(white?"White":"Dark")+"LatexBlock",has(block))
                .save(out,latex.getId());
    }
}