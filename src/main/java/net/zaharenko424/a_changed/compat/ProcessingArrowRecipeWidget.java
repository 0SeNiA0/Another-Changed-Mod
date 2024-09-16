package net.zaharenko424.a_changed.compat;

import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.common.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import org.jetbrains.annotations.NotNull;

public class ProcessingArrowRecipeWidget implements IRecipeWidget {

    private final IDrawableAnimated arrow;
    private final ScreenPosition position;

    public ProcessingArrowRecipeWidget(IGuiHelper guiHelper, int processingTime, ScreenPosition position) {
        this.arrow = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17)
                .buildAnimated(processingTime, IDrawableAnimated.StartDirection.LEFT, false);
        this.position = position;
    }

    @Override
    public @NotNull ScreenPosition getPosition() {
        return position;
    }

    @Override
    public void draw(@NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics);
    }
}