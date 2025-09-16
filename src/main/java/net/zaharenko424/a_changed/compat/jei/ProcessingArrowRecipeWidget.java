package net.zaharenko424.a_changed.compat.jei;

import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import org.jetbrains.annotations.NotNull;

public class ProcessingArrowRecipeWidget implements IRecipeWidget {

    private final IDrawableAnimated arrow;
    private final ScreenPosition position;

    public ProcessingArrowRecipeWidget(int processingTime, ScreenPosition position, IDrawableBuilder arrow) {
        this.arrow = arrow.buildAnimated(processingTime, IDrawableAnimated.StartDirection.LEFT, false);
        this.position = position;
    }

    @Override
    public @NotNull ScreenPosition getPosition() {
        return position;
    }

    @Override
    public void drawWidget(@NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics);
    }
}