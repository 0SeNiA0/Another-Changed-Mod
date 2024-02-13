package net.zaharenko424.a_changed.datagen.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.item.DNASample;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.registry.DNAType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DNAExtractorRecipeBuilder implements RecipeBuilder {

    private final DNAExtractorRecipe recipe;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public DNAExtractorRecipeBuilder(Ingredient ingredient, ItemStack result){
        this.recipe = new DNAExtractorRecipe(ingredient, result);
    }

    public static @NotNull DNAExtractorRecipeBuilder of(@NotNull DeferredHolder<DNAType, DNAType> dnaType){
        DNAType type = dnaType.get();
        return new DNAExtractorRecipeBuilder(Ingredient.of(type.getMaterial()), DNASample.encodeDNA(type));
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
                                        Optional.empty(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,
                                        MinMaxBounds.Ints.ANY, List.of(ItemPredicate.Builder.item().of(ingredient).build())
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
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
        Advancement.Builder advancement$builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        output.accept(new Finished(recipe, pId,
                advancement$builder.build(pId.withPrefix("recipes/dna_extractor/"))));
    }

    public static class Finished implements FinishedRecipe {

        private final DNAExtractorRecipe recipe;
        private final ResourceLocation recipeId;
        private final AdvancementHolder holder;

        public Finished(DNAExtractorRecipe recipe, ResourceLocation recipeId, AdvancementHolder holder){
            this.recipe = recipe;
            this.recipeId = recipeId;
            this.holder = holder;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {}

        @Override
        public @NotNull JsonObject serializeRecipe() {
            JsonObject obj = DNAExtractorRecipe.Serializer.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow(false, null).getAsJsonObject();
            obj.addProperty("type", AChanged.DNA_EXTRACTOR_RECIPE_SERIALIZER.getId().toString());
            return obj;
        }

        @Override
        public @NotNull ResourceLocation id() {
            return recipeId;
        }

        @Override
        public @NotNull RecipeSerializer<?> type() {
            return AChanged.DNA_EXTRACTOR_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public AdvancementHolder advancement() {
            return holder;
        }
    }
}