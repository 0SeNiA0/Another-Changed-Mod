package net.zaharenko424.a_changed.compat.jei.encoder;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.zaharenko424.a_changed.client.screen.machine.LatexEncoderScreen;
import net.zaharenko424.a_changed.compat.jei.GenderIngredient;
import net.zaharenko424.a_changed.compat.jei.JeiPlugin;
import net.zaharenko424.a_changed.compat.jei.ProcessingArrowRecipeWidget;
import net.zaharenko424.a_changed.recipe.LatexEncoderRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexEncoderRecipeCategory implements IRecipeCategory<LatexEncoderRecipe> {

    public static final RecipeType<LatexEncoderRecipe> TYPE = new RecipeType<>(RecipeRegistry.LATEX_ENCODER_RECIPE.getId(), LatexEncoderRecipe.class);

    private final IGuiHelper guiHelper;
    private final IDrawable icon;

    public LatexEncoderRecipeCategory(IGuiHelper guiHelper){
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
                guiGraphics.renderItem(ItemRegistry.LATEX_ENCODER_ITEM.toStack(), xOffset, yOffset);
            }
        };
    }

    @Override
    public @NotNull RecipeType<LatexEncoderRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("container.a_changed.latex_encoder");
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
    public void draw(@NotNull LatexEncoderRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(LatexEncoderScreen.TEXTURE, 0, 0, 162, 76, 7, 5, 162, 76, 256, 166);

        JeiPlugin.drawEnergyConsumption(recipe.getEnergyConsumption(), guiGraphics, getWidth() - 5, 10);
        JeiPlugin.drawProcessingTime(recipe.getProcessingTime(), guiGraphics, getWidth() - 5, 22);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull LatexEncoderRecipe recipe, @NotNull IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        builder.addSlot(RecipeIngredientRole.INPUT, 31, 42).addIngredients(ingredients.get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 31, 20).addIngredients(ingredients.get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 53, 5).addIngredients(ingredients.get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 5).addIngredients(ingredients.get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 93, 5).addIngredients(ingredients.get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 57).addIngredients(ingredients.get(5));
        builder.addSlot(RecipeIngredientRole.INPUT, 86, 57).addIngredients(ingredients.get(6));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 115, 30).addItemStack(recipe.getResultItem());

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 116, 58).addIngredient(GenderIngredient.TYPE, recipe.getGender()).setCustomRenderer(GenderIngredient.TYPE, GenderIngredient.RENDERER);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull LatexEncoderRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addWidget(new ProcessingArrowRecipeWidget(recipe.getProcessingTime(), new ScreenPosition(60, 23),
                guiHelper.drawableBuilder(LatexEncoderScreen.TEXTURE, 176, 0, 42, 27).setTextureSize(256, 166)));
    }
}