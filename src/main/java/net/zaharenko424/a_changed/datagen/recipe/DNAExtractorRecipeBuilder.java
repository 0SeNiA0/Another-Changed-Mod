package net.zaharenko424.a_changed.datagen.recipe;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.item.DNASample;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.recipe.PartialNBTIngredientFix;
import net.zaharenko424.a_changed.registry.DNAType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DNAExtractorRecipeBuilder implements RecipeBuilder {

    private final DNAExtractorRecipe recipe;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public DNAExtractorRecipeBuilder(@NotNull PartialNBTIngredientFix ingredient, @NotNull ItemStack result){
        this.recipe = new DNAExtractorRecipe(ingredient, result);
    }

    public static @NotNull DNAExtractorRecipeBuilder of(@NotNull DeferredHolder<DNAType, DNAType> dnaType){
        DNAType type = dnaType.get();
        ItemStack item = type.getMaterial();
        return new DNAExtractorRecipeBuilder(PartialNBTIngredientFix.of(item.getItem(), item.getTag()), DNASample.encodeDNA(type));
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String pName, @NotNull Criterion<?> pCriterion) {
        criteria.put(pName, pCriterion);
        return this;
    }

    public RecipeBuilder unlockedByMaterial(){
        Item ingredient = recipe.getIngredient().getItems()[0].getItem();
        criteria.put("has_" + BuiltInRegistries.ITEM.getKey(ingredient).getPath(),
                CriteriaTriggers.INVENTORY_CHANGED
                        .createCriterion(
                                new InventoryChangeTrigger.TriggerInstance(
                                        Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                                        List.of(ItemPredicate.Builder.item().of(ingredient).build())
                                )
                        ));
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return recipe.getResultItem().getItem();
    }

    @Override
    public void save(@NotNull RecipeOutput pRecipeOutput) {
        save(pRecipeOutput, Objects.requireNonNull(DNASample.decodeDNA(recipe.getResultItem())));
    }

    @Override
    public void save(@NotNull RecipeOutput output, @NotNull ResourceLocation pId) {
        if(criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
        Advancement.Builder advancement$builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(advancement$builder::addCriterion);
        output.accept(pId, recipe, advancement$builder.build(pId.withPrefix("recipes/dna_extractor/")));
    }
}