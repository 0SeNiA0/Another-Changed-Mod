package net.zaharenko424.a_changed.client.cmrs.api;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector3f;

public interface MatrixStack {

    static MatrixStack of(PoseStack poseStack){
        return (MatrixStack) poseStack;
    }

    static void push(PoseStack poseStack){
        of(poseStack).cmrs$push();
    }

    static void pop(PoseStack poseStack){
        of(poseStack).cmrs$pop();
    }

    static void translate(PoseStack poseStack, Vector3f translation){
        poseStack.last().pose().translate(translation);
    }

    static void scale(PoseStack poseStack, Vector3f scale){
        poseStack.scale(scale.x, scale.y, scale.z);
    }

    void cmrs$push();

    void cmrs$pop();
}
