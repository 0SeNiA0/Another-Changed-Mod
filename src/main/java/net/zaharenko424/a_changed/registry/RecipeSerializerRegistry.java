package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.recipe.CompressorRecipe;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.recipe.LatexEncoderRecipe;
import net.zaharenko424.a_changed.recipe.LatexPurifierRecipe;

public class RecipeSerializerRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, AChanged.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CompressorRecipe>> COMPRESSOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("compressor", ()-> CompressorRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DNAExtractorRecipe>> DNA_EXTRACTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("dna_extractor", ()-> DNAExtractorRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<LatexEncoderRecipe>> LATEX_ENCODER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("latex_encoder", ()-> LatexEncoderRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<LatexPurifierRecipe>> LATEX_PURIFIER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("latex_purifier", ()-> LatexPurifierRecipe.Serializer.INSTANCE);
}