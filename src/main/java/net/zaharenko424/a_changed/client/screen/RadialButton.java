package net.zaharenko424.a_changed.client.screen;

import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class RadialButton implements Renderable, GuiEventListener {

    protected int x;
    protected int y;
    protected boolean extendClickAreaOutsideRadius;
    protected boolean extendClickAreaInsideRadius;
    //Need rebuilding on update \/
    protected float minRad;
    protected float maxRad;
    protected float radius;
    protected float innerRadius;
    protected float outlineThickness;
    protected float scale;
    protected FloatFloatPair[] pixels;

    public RadialButton(int x, int y, float minRad, float maxRad, float radius, float innerRadius, float outlineThickness, float scale){
        set(x, y, minRad, maxRad, radius, innerRadius, outlineThickness, scale);
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setClickAreaProperties(boolean extendClickAreaOutsideRadius, boolean extendClickAreaInsideRadius){
        this.extendClickAreaOutsideRadius = extendClickAreaOutsideRadius;
        this.extendClickAreaInsideRadius = extendClickAreaInsideRadius;
    }

    public void setRad(float minRad, float maxRad){
        this.minRad = minRad;
        this.maxRad = maxRad;
        rebuildPixels();
    }

    public void setRadius(float radius, float innerRadius){
        this.radius = radius;
        this.innerRadius = innerRadius;
        rebuildPixels();
    }

    public void setThickness(float outlineThickness){
        this.outlineThickness = outlineThickness;
        rebuildPixels();
    }

    public void setScale(float scale){
        this.scale = scale;
        rebuildPixels();
    }

    public void set(int x, int y, float minRad, float maxRad, float radius, float innerRadius, float outlineThickness, float scale){
        this.x = x;
        this.y = y;
        this.minRad = minRad;
        this.maxRad = maxRad;
        this.radius = radius;
        this.innerRadius = innerRadius;
        this.outlineThickness = outlineThickness;
        this.scale = scale;
        rebuildPixels();
    }

    public void rebuildPixels(){
        List<FloatFloatPair> list = new ArrayList<>();

        //do the building

        pixels = list.toArray(new FloatFloatPair[0]);
    }

    protected int color(){
        return Color.BLACK.getRGB();
    }

    protected abstract void renderIcon(@NotNull GuiGraphics graphics, float x, float y, float partialTick);

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        float thickness = outlineThickness, x, y;
        int color = color();
        for(FloatFloatPair pair : pixels){
            x = pair.firstFloat();
            y = pair.secondFloat();
            fillGui(guiGraphics, x - thickness, y - thickness, x + thickness, y + thickness, color);
        }
        flushGui(guiGraphics);

        if(minRad > maxRad) maxRad += Mth.TWO_PI;//TMP temporary fix. save clamped & non clamped rad values in buttons?
        float radCenter = (maxRad - minRad) / 2 + minRad;//Nothing is more permanent than a temporary solution...
        renderIcon(guiGraphics, (Mth.cos(radCenter) * radius + this.x) - 16, (Mth.sin(radCenter) * radius + this.y) - 16, partialTick);
    }




    public static void fillGui(GuiGraphics graphics, float minX, float minY, float maxX, float maxY, int color){
        fill(graphics, RenderType.gui(), minX, minY, maxX, maxY, 0, color);
    }

    public static void fill(GuiGraphics graphics, RenderType renderType, float minX, float minY, float maxX, float maxY, float z, int color) {
        Matrix4f matrix4f = graphics.pose().last().pose();
        if (minX < maxX) {
            float i = minX;
            minX = maxX;
            maxX = i;
        }

        if (minY < maxY) {
            float j = minY;
            minY = maxY;
            maxY = j;
        }

        VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(renderType);
        vertexconsumer.addVertex(matrix4f, minX, minY, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, minX, maxY, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, maxX, maxY, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, maxX, minY, z).setColor(color);
    }

    public static void flushGui(GuiGraphics graphics){
        flush(graphics, RenderType.gui());
    }

    public static void flush(GuiGraphics graphics, RenderType renderType){
        graphics.bufferSource().endBatch(renderType);
    }



    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        float mouseRad = (float) Math.atan((mouseY - y) / (mouseX - x));

        if(mouseX < x) mouseRad += Mth.PI;
        if(mouseX > x && mouseY < y) mouseRad += Mth.TWO_PI;

        return (mouseRad >= minRad && mouseRad <= maxRad && checkDistance(mouseX, mouseY))
                || (minRad > maxRad && (mouseRad >= minRad || mouseRad <= maxRad) && checkDistance(mouseX, mouseY));
    }

    protected boolean checkDistance(double mouseX, double mouseY){
        if(extendClickAreaOutsideRadius && extendClickAreaInsideRadius) return true;

        float mouseDistSqr = Vector2f.distanceSquared(x, y, (float) mouseX, (float) mouseY);
        float sqrRadius = radius * radius, sqrInnerRadius = innerRadius * innerRadius;

        if(extendClickAreaOutsideRadius && mouseDistSqr >= sqrInnerRadius) return true;
        if(extendClickAreaInsideRadius && mouseDistSqr <= sqrRadius) return true;
        return mouseDistSqr <= sqrRadius && mouseDistSqr >= sqrInnerRadius;
    }

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return true;
    }
}
