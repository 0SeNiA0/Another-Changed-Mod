package net.zaharenko424.a_changed.compat;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.screen.machines.CompressorScreen;
import net.zaharenko424.a_changed.recipe.LatexPurifierRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexPurifierRecipeCategory implements IRecipeCategory<LatexPurifierRecipe> {

    public static final RecipeType<LatexPurifierRecipe> TYPE = RecipeType.create(AChanged.MODID, "latex_purifier", LatexPurifierRecipe.class);

    @Override
    public @NotNull RecipeType<LatexPurifierRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("container.a_changed.latex_purifier");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return 82;
            }

            @Override
            public int getHeight() {
                return 54;
            }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                guiGraphics.blit(CompressorScreen.TEXTURE, xOffset, yOffset, 82, 54, 55, 16, 82, 54, 256, 166);
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
                guiGraphics.renderItem(ItemRegistry.LATEX_PURIFIER_ITEM.toStack(), xOffset, yOffset);
            }
        };
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull LatexPurifierRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(recipe.getResultItem());
    }
}