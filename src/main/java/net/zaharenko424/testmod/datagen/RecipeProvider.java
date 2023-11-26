package net.zaharenko424.testmod.datagen;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,LAB_BLOCK_ITEM,32)
                .pattern("QIQ")
                .pattern("IQI")
                .pattern("QIQ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('Q',Items.QUARTZ_BLOCK)
                .unlockedBy("hasQuartz",has(Items.QUARTZ_BLOCK))
                .save(p_301172_,LAB_BLOCK_ITEM.getId());

        stonecutting(LAB_BLOCK_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem[]{LAB_TILE_ITEM,BOLTED_LAB_TILE_ITEM},"hasLabBlock",has(LAB_BLOCK_ITEM),p_301172_);
        stonecutting(LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem[]{LAB_BLOCK_ITEM,BOLTED_LAB_TILE_ITEM},"hasLabTile",has(LAB_TILE_ITEM),p_301172_);
        stonecutting(BOLTED_LAB_TILE_ITEM,RecipeCategory.BUILDING_BLOCKS,new DeferredItem[]{LAB_BLOCK_ITEM,LAB_TILE_ITEM},"hasBoltedLabTile",has(BOLTED_LAB_TILE_ITEM),p_301172_);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD,ORANGE_JUICE_ITEM)
                .requires(ORANGE_ITEM,4)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.SUGAR)
                .unlockedBy("hasOrange",has(ORANGE_ITEM))
                .save(p_301172_,ORANGE_JUICE_ITEM.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,SYRINGE_ITEM,8)
                .pattern(" II")
                .pattern(" GI")
                .pattern("I  ")
                .define('I', Tags.Items.NUGGETS_IRON)
                .define('G', Tags.Items.GLASS_PANES)
                .unlockedBy("hasIron",has(Tags.Items.INGOTS_IRON))
                .save(p_301172_,SYRINGE_ITEM.getId());
    }

    private void stonecutting(DeferredItem<?> material, RecipeCategory category, DeferredItem<?> @NotNull [] results, String str, Criterion<?> criterion, RecipeOutput out){
        for(DeferredItem<?> result:results) {
            SingleItemRecipeBuilder.stonecutting((Ingredient.of(material)), category, result).unlockedBy(str,criterion).save(out,result.getId().withSuffix("_stonecutting_from_"+material.getId().getPath()));
        }
    }
}