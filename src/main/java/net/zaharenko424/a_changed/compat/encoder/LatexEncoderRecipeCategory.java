package net.zaharenko424.a_changed.compat.encoder;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.screen.machines.LatexEncoderScreen;
import net.zaharenko424.a_changed.compat.GenderIngredient;
import net.zaharenko424.a_changed.recipe.LatexEncoderRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexEncoderRecipeCategory implements IRecipeCategory<LatexEncoderRecipe> {

    public static final RecipeType<LatexEncoderRecipe> TYPE = RecipeType
            .create(AChanged.MODID, "latex_encoder", LatexEncoderRecipe.class);

    @Override
    public @NotNull RecipeType<LatexEncoderRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("container.a_changed.latex_encoder");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return 162;
            }

            @Override
            public int getHeight() {
                return 76;
            }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                guiGraphics.blit(LatexEncoderScreen.TEXTURE, xOffset, yOffset, 162, 76, 7, 5, 162, 76, 256, 166);
            }
        };
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return 16;
            }

            @Override
            public int getHeight() {
                return 16;
            }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                guiGraphics.renderItem(ItemRegistry.LATEX_ENCODER_ITEM.toStack(), xOffset, yOffset);
            }
        };
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull LatexEncoderRecipe recipe, @NotNull IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 42).addIngredients(ingredients.get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 20).addIngredients(ingredients.get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 67, 5).addIngredients(ingredients.get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 87, 5).addIngredients(ingredients.get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 107, 5).addIngredients(ingredients.get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 74, 57).addIngredients(ingredients.get(5));
        builder.addSlot(RecipeIngredientRole.INPUT, 100, 57).addIngredients(ingredients.get(6));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 133, 30).addItemStack(recipe.getResultItem());

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 0).addIngredient(GenderIngredient.TYPE, recipe.getGender());

        builder.moveRecipeTransferButton(160, 74);
    }
}