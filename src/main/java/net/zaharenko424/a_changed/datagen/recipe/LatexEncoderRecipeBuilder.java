package net.zaharenko424.a_changed.datagen.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.item.DNASample;
import net.zaharenko424.a_changed.item.LatexSyringeItem;
import net.zaharenko424.a_changed.item.SyringeItem;
import net.zaharenko424.a_changed.recipe.LatexEncoderRecipe;
import net.zaharenko424.a_changed.recipe.PartialNBTIngredientFix;
import net.zaharenko424.a_changed.DNAType;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class LatexEncoderRecipeBuilder implements RecipeBuilder {

    private final NonNullList<PartialNBTIngredientFix> ingredients = NonNullList.withSize(7, PartialNBTIngredientFix.EMPTY);
    private Gender gender;
    private final ItemStack result;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public LatexEncoderRecipeBuilder(@NotNull ItemStack result, @NotNull Gender gender) {
        ResourceLocation tf = LatexSyringeItem.decodeTransfur(result);
        if(tf == null || TransfurManager.getTransfurType(tf) == null)
            throw new IllegalArgumentException("Invalid latex syringe encoding! ("+tf+")");
        this.result = result;
        this.gender = gender;
        ingredients.set(0, PartialNBTIngredientFix.of(ItemRegistry.SYRINGE_ITEM.get(), null));
    }

    public static @NotNull LatexEncoderRecipeBuilder of(@NotNull AbstractTransfurType transfurType){
        return new LatexEncoderRecipeBuilder(LatexSyringeItem.encodeTransfur(transfurType), transfurType.getGender());
    }

    /**
     * Syringe ingredient can be overridden. Defaults to ItemRegistry.SYRINGE_ITEM
     * @param syringe must be an instance of SyringeItem
     */
    public @NotNull LatexEncoderRecipeBuilder setSyringeIngredient(@NotNull ItemStack syringe){
        if(!(syringe.getItem() instanceof SyringeItem))
            throw new IllegalArgumentException("Syringe ingredient must be an instance of SyringeItem!");
        ingredients.set(0, PartialNBTIngredientFix.of(syringe.getItem(), syringe.getTag()));
        return this;
    }

    /**
     * Mandatory!
     */
    public @NotNull LatexEncoderRecipeBuilder setLatexBaseIngredient(@NotNull ItemStack latexBase){
        ingredients.set(1, PartialNBTIngredientFix.of(latexBase.getItem(), latexBase.getTag()));
        return this;
    }

    /**
     * Adds DNASample ingredient to this recipe if there is space left (max 3)
     */
    public @NotNull LatexEncoderRecipeBuilder addDNASampleIngredient(@NotNull DeferredHolder<DNAType, DNAType> dnaType){
        return addDNASampleIngredient(DNASample.encodeDNA(dnaType.get()));
    }

    /**
     * Adds DNASample ingredient to this recipe if there is space left (max 3)
     * @param dnaSample must be an instance of DNASample
     */
    public @NotNull LatexEncoderRecipeBuilder addDNASampleIngredient(@NotNull ItemStack dnaSample){
        if(!(dnaSample.getItem() instanceof DNASample))
            throw new IllegalArgumentException("DNA sample ingredient must be an instance of DNASample!");
        for(int i = 2; i < 5; i++){
            if(!ingredients.get(i).isEmpty()) continue;
            ingredients.set(i, PartialNBTIngredientFix.of(dnaSample.getItem(), dnaSample.getTag()));
            break;
        }
        return this;
    }

    /**
     * Adds any ingredient to this recipe if there is space left (max 2)
     */
    public @NotNull LatexEncoderRecipeBuilder addMiscIngredient(@NotNull ItemStack stack){
        for(int i = 5; i < 7; i++){
            if(!ingredients.get(i).isEmpty()) continue;
            ingredients.set(i, PartialNBTIngredientFix.of(stack.getItem(), stack.getTag()));
            return this;
        }
        return this;
    }

    public @NotNull LatexEncoderRecipeBuilder setGender(@NotNull Gender gender){
        this.gender = gender;
        return this;
    }

    /**
     * Mandatory!
     */
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
        return result.getItem();
    }

    @Override
    public void save(@NotNull RecipeOutput pRecipeOutput) {
        save(pRecipeOutput, Objects.requireNonNull(LatexSyringeItem.decodeTransfur(result)));
    }

    @Override
    public void save(@NotNull RecipeOutput output, @NotNull ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
        if (ingredients.get(1).isEmpty()) {
            throw new IllegalStateException("No latexBase ingredient specified in recipe " + pId);
        }
        Advancement.Builder advancement$builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        output.accept(pId, new LatexEncoderRecipe(ingredients, gender == null ? Gender.NONE : gender, result),
                advancement$builder.build(pId.withPrefix("recipes/latex_encoder/")));
    }
}