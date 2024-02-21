package net.zaharenko424.a_changed.datagen.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.recipe.CompressorRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class CompressorRecipeBuilder implements RecipeBuilder {

    private final CompressorRecipe recipe;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public CompressorRecipeBuilder(@NotNull Ingredient ingredient, @NotNull ItemStack result){
        recipe = new CompressorRecipe(ingredient, result);
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
        return recipe.getResultItem().getItem();
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
        output.accept(new CompressorRecipeBuilder.Finished(recipe, pId,
                advancement$builder.build(pId.withPrefix("recipes/compressor/"))));
    }

    public static class Finished implements FinishedRecipe {

        private final CompressorRecipe recipe;
        private final ResourceLocation recipeId;
        private final AdvancementHolder holder;

        public Finished(CompressorRecipe recipe, ResourceLocation recipeId, AdvancementHolder holder){
            this.recipe = recipe;
            this.recipeId = recipeId;
            this.holder = holder;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {}

        @Override
        public @NotNull JsonObject serializeRecipe() {
            JsonObject obj = CompressorRecipe.Serializer.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow(false, null).getAsJsonObject();
            obj.addProperty("type", AChanged.COMPRESSOR_RECIPE_SERIALIZER.getId().toString());
            return obj;
        }

        @Override
        public @NotNull ResourceLocation id() {
            return recipeId;
        }

        @Override
        public @NotNull RecipeSerializer<?> type() {
            return CompressorRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public AdvancementHolder advancement() {
            return holder;
        }
    }
}