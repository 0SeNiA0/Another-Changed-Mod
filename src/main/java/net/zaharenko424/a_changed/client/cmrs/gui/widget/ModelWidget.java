package net.zaharenko424.a_changed.client.cmrs.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.zaharenko424.a_changed.client.cmrs.api.MatrixStack;
import net.zaharenko424.a_changed.client.cmrs.geom.Reusable;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class ModelWidget extends Widget {

    protected float width = 100, height = 150;

    protected Vector2f rotation = new Vector2f();
    protected Vector2f accumulatedRotation = new Vector2f();
    protected float zoom = 50;

    public ModelWidget(){
        setInteractable(false);
    }

    public ModelWidget setSize(float width, float height){
        this.width = width;
        this.height = height;
        return this;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public ModelWidget setInteractable(boolean interactable){
        super.setInteractable(interactable);
        return this;
    }

    public ModelWidget setZoom(float zoom){
        this.zoom = zoom;
        return this;
    }

    public ModelWidget setRotation(float radX, float radY){
        rotation.sub(radX, radY, accumulatedRotation);
        accumulatedRotation.negate();
        return this;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(!isVisible()) return;

        rotation.add(accumulatedRotation.x * .5f, accumulatedRotation.y * .5f);
        accumulatedRotation.sub(accumulatedRotation.x * .5f, accumulatedRotation.y * .5f);

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);

        //transform rect to screen coordinates to apply scissors
        Vector3f v = new Vector3f(- width / 2f, - height / 2f, 0).mulPosition(stack.last().pose());
        Vector3f v1 = new Vector3f(+ width / 2f, + height / 2f, 0).mulPosition(stack.last().pose());
        guiGraphics.enableScissor((int) v.x, (int) v.y, (int) v1.x, (int) v1.y);

        Matrix4f mat1 = Reusable.MAT4F.get().identity();
        mat1.rotateX(rotation.x);
        mat1.rotateY(rotation.y);
        stack.mulPose(mat1);

        stack.translate(0, zoom, 0);
        stack.scale(zoom, -zoom, zoom);

        renderModel(guiGraphics.pose());

        guiGraphics.disableScissor();
        MatrixStack.pop(stack);
    }

    protected abstract void renderModel(PoseStack stack);

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(!isInteractable()) return false;
        accumulatedRotation.add((float) -dragY * Mth.DEG_TO_RAD, (float) dragX * Mth.DEG_TO_RAD);
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if(!isInteractable()) return false;
        zoom += (float) scrollY * 2;
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return isHovering() && isInteractable();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(isClickThrough()) return false;
        float halfWidth = width / 2 * scale.x;
        float halfHeight = height / 2 * scale.y;

        return mouseX >= origin.x - halfWidth && mouseX <= origin.x + halfWidth
                && mouseY >= origin.y - halfHeight && mouseY <= origin.y + halfHeight;
    }
}
