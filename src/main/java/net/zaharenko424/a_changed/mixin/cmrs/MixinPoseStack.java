package net.zaharenko424.a_changed.mixin.cmrs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.zaharenko424.cmrs.api.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Mixin(PoseStack.class)
public abstract class MixinPoseStack implements MatrixStack {

    @Shadow @Final private Deque<PoseStack.Pose> poseStack;

    @Shadow public abstract PoseStack.Pose last();

    @Shadow public abstract void pushPose();

    @Unique
    private static final ThreadLocal<List<PoseStack.Pose>> cmrs$posePool = ThreadLocal.withInitial(ArrayList::new);

    @Override
    public void cmrs$push() {
        if(cmrs$posePool.get().isEmpty()) {
            pushPose();
            return;
        }
        PoseStack.Pose matrix = cmrs$posePool.get().removeLast();
        PoseStack.Pose last = last();

        matrix.pose().set(last.pose());
        matrix.normal().set(last.normal());
        matrix.trustedNormals = last.trustedNormals;

        poseStack.add(matrix);
    }

    @Override
    public void cmrs$pop() {
        cmrs$posePool.get().add(poseStack.removeLast());
    }
}
