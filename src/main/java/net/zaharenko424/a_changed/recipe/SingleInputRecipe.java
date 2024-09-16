package net.zaharenko424.a_changed.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class SingleInputRecipe implements Recipe<SingleInputRecipeWrapper> {

    protected final String group;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final int energyConsumption;
    protected final int processingTime;

    public SingleInputRecipe(String group, Ingredient ingredient, ItemStack result, int energyConsumption, int processingTime){
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.energyConsumption = energyConsumption;
        this.processingTime = processingTime;
    }

    @Override
    public boolean matches(@NotNull SingleInputRecipeWrapper input, @NotNull Level level) {
        return ingredient.test(input.item());
    }

    @Override
    public @NotNull ItemStack assemble(SingleInputRecipeWrapper input, HolderLookup.@NotNull Provider registries) {
        input.shrink(ingredient.getItems()[0].getCount());
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, ingredient);
    }

    @Override
    public @NotNull String getGroup() {
        return group;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public interface Factory<T extends SingleInputRecipe> {
        T create(String group, Ingredient ingredient, ItemStack result, int energyConsumption, int processingTime);
    }

    public static class Serializer <T extends SingleInputRecipe> implements RecipeSerializer<T> {

        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        public final int defaultEnergyConsumption;
        public final int defaultProcessingTime;

        public Serializer(Factory<T> factory, int defaultEnergyConsumption, int defaultProcessingTime){
            codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_300832_ -> p_300832_.group),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(p_300833_ -> p_300833_.ingredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(p_300827_ -> p_300827_.result),
                    Codec.INT.fieldOf("energyConsumption").orElse(defaultEnergyConsumption).forGetter(p_300834_ -> p_300834_.energyConsumption),
                    Codec.INT.fieldOf("processingTime").orElse(defaultProcessingTime).forGetter(p_300834_ -> p_300834_.processingTime)
            ).apply(builder, factory::create));

            streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    SingleInputRecipe::getGroup,
                    Ingredient.CONTENTS_STREAM_CODEC,
                    recipe -> recipe.ingredient,
                    ItemStack.STREAM_CODEC,
                    recipe -> recipe.result,
                    ByteBufCodecs.VAR_INT,
                    recipe -> recipe.energyConsumption,
                    ByteBufCodecs.VAR_INT,
                    recipe -> recipe.processingTime,
                    factory::create
            );
            
            this.defaultEnergyConsumption = defaultEnergyConsumption;
            this.defaultProcessingTime = defaultProcessingTime;
        }

        @Override
        public @NotNull MapCodec<T> codec() {
            return codec;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return streamCodec;
        }
    }
}