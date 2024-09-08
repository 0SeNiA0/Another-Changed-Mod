package net.zaharenko424.a_changed.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractRadialMenuScreen extends Screen {

    protected int halfWidth = width / 2;
    protected int halfHeight = height / 2;
    protected List<RadialButton> buttons = new ArrayList<>();
    protected int selectedButton = -1;
    protected int currentlyActive;
    protected int radius;
    protected int innerRadius;

    protected AbstractRadialMenuScreen(Component pTitle, int radius, int innerRadius) {
        super(pTitle);
        this.radius = radius;
        this.innerRadius = innerRadius;
    }

    @Override
    protected void init() {
        super.init();
        halfWidth = width / 2;
        halfHeight = height / 2;
    }

    protected int buttonOffsetDeg(){
        return 4;
    }

    protected int buttonColor(int button){
        if(button == selectedButton && button == currentlyActive) return -16711808;
        if(button == selectedButton) return Color.GRAY.getRGB();
        if(button == currentlyActive) return Color.GREEN.getRGB();

        return Color.BLACK.getRGB();
    }

    protected void addRadialButton(float minDeg, float maxDeg, int centerX, int centerY){
        buttons.add(makeButton(minDeg, maxDeg, radius, innerRadius, centerX, centerY, buttonOffsetDeg()));
    }

    protected RadialButton makeButton(float minDeg, float maxDeg, int radius, int innerRadius, int centerX, int centerY, float buttonOffsetDeg){
        float minRad = Mth.DEG_TO_RAD * (minDeg + buttonOffsetDeg);
        float maxRad = Mth.DEG_TO_RAD * (maxDeg - buttonOffsetDeg);

        HashSet<Pair<Integer, Integer>> data = new HashSet<>();

        raycast(innerRadius, radius, minRad, centerX, centerY, data);
        raycast(innerRadius, radius, maxRad, centerX, centerY, data);
        arc(minRad, maxRad, radius, Mth.DEG_TO_RAD / 2, centerX, centerY, data);
        arc(minRad, maxRad, innerRadius, Mth.DEG_TO_RAD / 2, centerX, centerY, data);

        if(maxRad > 2 * Mth.PI) maxRad -= 2 * Mth.PI;

        return new RadialButton(minRad, maxRad, data);
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

    /**
     * x, y -> coordinates of top left corner.
     * @param button index of button.
     */
    protected abstract void renderIcon(GuiGraphics guiGraphics, int x, int y, float partialTick, int button);

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        RadialButton button;
        final int radius = this.radius - (this.radius - innerRadius) / 2;
        float minRad, maxRad;
        float radCenter;
        int color, size, x, y;
        for (int i = 0; i < buttons.size(); i++) {
            button = buttons.get(i);
            color = buttonColor(i);
            size = i == selectedButton ? 2 : 1;
            for(Pair<Integer, Integer> pos : button.pixels){
                x = pos.getLeft();
                y = pos.getRight();
                guiGraphics.fill(x - size, y - size, x + size, y + size, color);
            }

            minRad = button.radMin();
            maxRad = button.radMax();
            if(minRad > maxRad) maxRad += 2 * Mth.PI;//TMP temporary fix. save clamped & non clamped rad values in buttons?
            radCenter = (maxRad - minRad) / 2 + minRad;

            renderIcon(guiGraphics, (int) (Mth.cos(radCenter) * radius + halfWidth) - 16, (int) (Mth.sin(radCenter) * radius + halfHeight) - 16, pPartialTick, i);
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

    protected record RadialButton(float radMin, float radMax, HashSet<Pair<Integer, Integer>> pixels){}
}