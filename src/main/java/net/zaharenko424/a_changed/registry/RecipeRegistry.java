package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.recipe.*;

public class RecipeRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, AChanged.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, AChanged.MODID);

    //Serializers
    public static final DeferredHolder<RecipeSerializer<?>, SingleInputRecipe.Serializer<CompressorRecipe>> COMPRESSOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS
            .register("compressor", ()-> new SingleInputRecipe.Serializer<>(CompressorRecipe::new, 32, 120));

    public static final DeferredHolder<RecipeSerializer<?>, DNAExtractorRecipe.Serializer> DNA_EXTRACTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS
            .register("dna_extractor", DNAExtractorRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, LatexEncoderRecipe.Serializer> LATEX_ENCODER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS
            .register("latex_encoder", ()-> new LatexEncoderRecipe.Serializer(96, 300));

    public static final DeferredHolder<RecipeSerializer<?>, SingleInputRecipe.Serializer<LatexPurifierRecipe>> LATEX_PURIFIER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS
            .register("latex_purifier", ()-> new SingleInputRecipe.Serializer<>(LatexPurifierRecipe::new, 48, 160));

    //Types
    public static final DeferredHolder<RecipeType<?>, RecipeType<CompressorRecipe>> COMPRESSOR_RECIPE = registerType("compressor");
    public static final DeferredHolder<RecipeType<?>, RecipeType<DNAExtractorRecipe>> DNA_EXTRACTOR_RECIPE = registerType("dna_extractor");
    public static final DeferredHolder<RecipeType<?>, RecipeType<LatexEncoderRecipe>> LATEX_ENCODER_RECIPE = registerType("latex_encoder");
    public static final DeferredHolder<RecipeType<?>, RecipeType<LatexPurifierRecipe>> LATEX_PURIFIER_RECIPE = registerType("latex_purifier");

    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> registerType(String name){
        return RECIPE_TYPES.register(name, () -> RecipeType.simple(AChanged.resourceLoc(name)));
    }
}