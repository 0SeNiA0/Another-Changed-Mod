package net.zaharenko424.a_changed.compat;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.client.screen.machines.LatexPurifierScreen;
import net.zaharenko424.a_changed.recipe.LatexPurifierRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexPurifierRecipeCategory implements IRecipeCategory<LatexPurifierRecipe> {

    public static final RecipeType<LatexPurifierRecipe> TYPE = new RecipeType<>(RecipeRegistry.LATEX_PURIFIER_RECIPE.getId(), LatexPurifierRecipe.class);

    protected final IGuiHelper guiHelper;

    public LatexPurifierRecipeCategory(IGuiHelper guiHelper){
        this.guiHelper = guiHelper;
    }

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
                guiGraphics.blit(LatexPurifierScreen.TEXTURE, xOffset, yOffset, 82, 54, 55, 16, 82, 54, 256, 166);
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
    public void draw(@NotNull LatexPurifierRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        JeiPlugin.drawEnergyConsumption(recipe.getEnergyConsumption(), guiGraphics, getWidth() - 20, 45);
        JeiPlugin.drawProcessingTime(recipe.getProcessingTime(), guiGraphics, getWidth(), 45);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull LatexPurifierRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().getFirst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(RecipeUtil.getResultItem(recipe));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull LatexPurifierRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addWidget(new ProcessingArrowRecipeWidget(guiHelper, recipe.getProcessingTime(), new ScreenPosition(24, 18)));
    }
}