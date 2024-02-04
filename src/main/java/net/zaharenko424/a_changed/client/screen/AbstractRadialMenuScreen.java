package net.zaharenko424.a_changed.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractRadialMenuScreen extends Screen {

    protected List<RadialButton> buttons = new ArrayList<>();
    protected int selectedButton = -1;
    protected int currentlyActive;

    protected AbstractRadialMenuScreen(Component pTitle) {
        super(pTitle);
    }

    protected abstract int buttonOffsetDeg();

    protected void addRadialButton(float minDeg, float maxDeg, int radius, int innerRadius, int centerX, int centerY){
        float minRad = Mth.DEG_TO_RAD * (minDeg + buttonOffsetDeg());
        float maxRad = Mth.DEG_TO_RAD * (maxDeg - buttonOffsetDeg());

        HashSet<Pair<Integer, Integer>> data = new HashSet<>();

        raycast(innerRadius, radius, minRad, centerX, centerY, data);
        raycast(innerRadius, radius, maxRad, centerX, centerY, data);
        arc(minRad, maxRad, radius, Mth.DEG_TO_RAD / 2, centerX, centerY, data);
        arc(minRad, maxRad, innerRadius, Mth.DEG_TO_RAD / 2, centerX, centerY, data);

        if(maxRad > 2 * Mth.PI) maxRad -= 2 * Mth.PI;

        buttons.add(new RadialButton(minRad, maxRad, data, null));
    }

    protected void arc(float minRad, float maxRad, int radius, float precision, int centerX, int centerY, HashSet<Pair<Integer, Integer>> data){
        for (float rad = minRad; rad <= maxRad; rad += precision) {
            data.add(Pair.of((int)(Mth.cos(rad) * radius) + centerX, (int)(Mth.sin(rad) * radius) + centerY));
        }
    }

    protected void raycast(int minLength, int maxLength, float rayRad, int originX, int originY, HashSet<Pair<Integer, Integer>> data){
        for (int i = minLength; i < maxLength; i++) {
            data.add(Pair.of((int) (Math.cos(rayRad) * i) + originX, (int) (Math.sin(rayRad) * i) + originY));
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        int color, size, x, y;
        for (int i = 0; i < buttons.size(); i++) {
            if(i == selectedButton && i == currentlyActive) color = -16711808;
            else if(i == selectedButton) color = Color.GRAY.getRGB();
            else if(i == currentlyActive) color = Color.GREEN.getRGB();
            else color = Color.BLACK.getRGB();
            size = i == selectedButton ? 2 : 1;
            for(Pair<Integer, Integer> pos : buttons.get(i).pixels){
                x = pos.getLeft();
                y = pos.getRight();
                guiGraphics.fill(x - size, y - size, x + size, y + size, color);
            }
        }
    }

    @Override
    public void renderTransparentBackground(@NotNull GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(0, 0, width, height, -2146430960, -2012213232);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        float mouseRad = (float) Math.atan((mouseY - halfHeight) / (mouseX - halfWidth));

        if(mouseX < halfWidth) mouseRad += Mth.PI;
        if(mouseX > halfWidth && mouseY < halfHeight) mouseRad += 2 * Mth.PI;

        RadialButton button;
        for(int i = 0; i < buttons.size(); i++){
            button = buttons.get(i);
            if((mouseRad >= button.radMin && mouseRad <= button.radMax)
                    || button.radMin > button.radMax && (mouseRad >= button.radMin || mouseRad <= button.radMax)) {
                selectedButton = i;
                return;
            }
        }
        selectedButton = -1;
    }

    protected record RadialButton(float radMin, float radMax, HashSet<Pair<Integer, Integer>> pixels, @Nullable Runnable onClick){}
}