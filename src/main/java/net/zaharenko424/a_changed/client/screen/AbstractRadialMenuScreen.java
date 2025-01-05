package net.zaharenko424.a_changed.client.screen;

import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

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

        HashSet<IntIntPair> data = new HashSet<>();

        raycast(innerRadius, radius, minRad, centerX, centerY, data);
        raycast(innerRadius, radius, maxRad, centerX, centerY, data);
        arc(minRad, maxRad, radius, Mth.DEG_TO_RAD / 2, centerX, centerY, data);
        arc(minRad, maxRad, innerRadius, Mth.DEG_TO_RAD / 2, centerX, centerY, data);

        if(maxRad > Mth.TWO_PI) maxRad -= Mth.TWO_PI;

        return new RadialButton(minRad, maxRad, data);
    }

    protected void arc(float minRad, float maxRad, int radius, float precision, int centerX, int centerY, HashSet<IntIntPair> data){
        for (float rad = minRad; rad <= maxRad; rad += precision) {
            data.add(IntIntPair.of(Math.round((Mth.cos(rad) * radius) + centerX), Math.round((Mth.sin(rad) * radius) + centerY)));
        }
    }

    protected void raycast(int minLength, int maxLength, float rayRad, int originX, int originY, HashSet<IntIntPair> data){
        for (int i = minLength; i < maxLength; i++) {
            data.add(IntIntPair.of(Math.round((Mth.cos(rayRad) * i) + originX), (int) Math.round((Math.sin(rayRad) * i) + originY)));
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
        RenderType gui = RenderType.gui();
        MultiBufferSource.BufferSource source = guiGraphics.bufferSource();

        for (int i = 0; i < buttons.size(); i++) {
            button = buttons.get(i);
            color = buttonColor(i);
            size = i == selectedButton ? 2 : 1;
            for(IntIntPair pos : button.pixels){
                x = pos.firstInt();
                y = pos.secondInt();
                fill(guiGraphics, gui, x - size, y - size, x + size, y + size, 0, color);
            }
            source.endBatch(gui);

            minRad = button.radMin();
            maxRad = button.radMax();
            if(minRad > maxRad) maxRad += Mth.TWO_PI;//TMP temporary fix. save clamped & non clamped rad values in buttons?
            radCenter = (maxRad - minRad) / 2 + minRad;//Nothing is more permanent than a temporary solution...

            renderIcon(guiGraphics, (int) (Mth.cos(radCenter) * radius + halfWidth) - 16, (int) (Mth.sin(radCenter) * radius + halfHeight) - 16, pPartialTick, i);
        }
    }

    public static void fill(GuiGraphics graphics, RenderType renderType, int minX, int minY, int maxX, int maxY, int z, int color) {
        Matrix4f matrix4f = graphics.pose().last().pose();
        if (minX < maxX) {
            int i = minX;
            minX = maxX;
            maxX = i;
        }

        if (minY < maxY) {
            int j = minY;
            minY = maxY;
            maxY = j;
        }

        VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(renderType);
        vertexconsumer.addVertex(matrix4f, minX, minY, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, minX, maxY, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, maxX, maxY, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, maxX, minY, z).setColor(color);
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
        if(mouseX > halfWidth && mouseY < halfHeight) mouseRad += Mth.TWO_PI;

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

    protected record RadialButton(float radMin, float radMax, HashSet<IntIntPair> pixels){}
}