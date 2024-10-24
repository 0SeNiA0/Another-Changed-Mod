package net.zaharenko424.a_changed.datagen.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.zaharenko424.a_changed.recipe.LatexPurifierRecipe;
import net.zaharenko424.a_changed.recipe.SingleInputRecipe;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class LatexPurifierRecipeBuilder implements RecipeBuilder {

    private final LatexPurifierRecipe recipe;
    private final Item result;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public LatexPurifierRecipeBuilder(@NotNull Ingredient ingredient, @NotNull ItemStack result){
        SingleInputRecipe.Serializer<?> serializer = RecipeRegistry.LATEX_PURIFIER_RECIPE_SERIALIZER.get();
        recipe = new LatexPurifierRecipe("", ingredient, result, serializer.defaultEnergyConsumption, serializer.defaultProcessingTime);
        this.result = result.getItem();
    }

    public LatexPurifierRecipeBuilder(String group, @NotNull Ingredient ingredient, @NotNull ItemStack result, int energyConsumption, int processingTime){
        recipe = new LatexPurifierRecipe(group, ingredient, result, energyConsumption, processingTime);
        this.result = result.getItem();
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String pName, @NotNull Criterion<?> pCriterion) {
        criteria.put(pName, pCriterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return result;
    }

    @Override
    public void save(@NotNull RecipeOutput output, @NotNull ResourceLocation pId) {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
        Advancement.Builder advancement$builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(advancement$builder::addCriterion);
        output.accept(pId, recipe, advancement$builder.build(pId.withPrefix("recipes/latex_purifier/")));
    }

}