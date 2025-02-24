package net.zaharenko424.a_changed.compat.jei;

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
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.client.screen.machines.LatexPurifierScreen;
import net.zaharenko424.a_changed.recipe.LatexPurifierRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexPurifierRecipeCategory implements IRecipeCategory<LatexPurifierRecipe> {

    public static final RecipeType<LatexPurifierRecipe> TYPE = new RecipeType<>(RecipeRegistry.LATEX_PURIFIER_RECIPE.getId(), LatexPurifierRecipe.class);

    private final IGuiHelper guiHelper;
    private final IDrawable icon;

    public LatexPurifierRecipeCategory(IGuiHelper guiHelper){
        this.guiHelper = guiHelper;
        icon = new IDrawable() {
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
    public @NotNull RecipeType<LatexPurifierRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("container.a_changed.latex_purifier");
    }

    @Override
    public int getWidth() {
        return 162;
    }

    @Override
    public int getHeight() {
        return 76;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(@NotNull LatexPurifierRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(LatexPurifierScreen.TEXTURE, 0, 0, 162, 76, 7, 5, 162, 76, 256, 166);

        JeiPlugin.drawEnergyConsumption(recipe.getEnergyConsumption(), guiGraphics, getWidth() - 120, 64);
        JeiPlugin.drawProcessingTime(recipe.getProcessingTime(), guiGraphics, getWidth() - 100, 64);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull LatexPurifierRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 30).addIngredients(recipe.getIngredients().getFirst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 30).addItemStack(RecipeUtil.getResultItem(recipe));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull LatexPurifierRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addAnimatedRecipeArrow(recipe.getProcessingTime()).setPosition(69, 30);
    }
}