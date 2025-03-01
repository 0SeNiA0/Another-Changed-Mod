package net.zaharenko424.a_changed.client.cmrs.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.MatrixUtil;
import net.minecraft.Util;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class MatrixStack {

    private final List<Matrix> stack = Util.make(new ArrayList<>(), list -> list.add(new Matrix()));
    private final List<Matrix> matrixPool = new ArrayList<>();
    private PoseStack vanilla;

    public MatrixStack copyPoseStack(PoseStack poseStack){
        while(!isEmpty()) matrixPool.add(stack.removeLast());
        last().set(poseStack.last());
        return this;
    }

    public PoseStack asVanilla(){
        if(vanilla == null) vanilla = new PoseStack();

        while(!vanilla.clear()) vanilla.popPose();

        PoseStack.Pose last = vanilla.last();
        Matrix lastM = last();
        last.pose().set(lastM.transform());
        last.normal().set(lastM.normal());
        last.trustedNormals = lastM.trustedNormals;

        return vanilla;
    }

    public void translate(double x, double y, double z) {
        translate((float)x, (float)y, (float)z);
    }

    public void translate(float x, float y, float z) {
        last().transform.translate(x, y, z);
    }

    public void translate(Vector3f translation){
        last().transform.translate(translation);
    }

    public void scale(float x, float y, float z) {
        Matrix matrix = last();
        matrix.transform.scale(x, y, z);

        if (Math.abs(x) == Math.abs(y) && Math.abs(y) == Math.abs(z)) {
            if (x < 0 || y < 0 || z < 0) {
                matrix.normal.scale(Math.signum(x), Math.signum(y), Math.signum(z));
            }
        } else {
            matrix.normal.scale(1 / x, 1 / y, 1 / z);
            matrix.trustedNormals = false;
        }
    }

    public void mulPose(Quaternionf quaternion) {
        Matrix matrix = last();
        matrix.transform.rotate(quaternion);
        matrix.normal.rotate(quaternion);
    }

    public void rotateAround(Quaternionf quaternion, float x, float y, float z) {
        Matrix matrix = last();
        matrix.transform.rotateAround(quaternion, x, y, z);
        matrix.normal.rotate(quaternion);
    }

    public void push() {
        Matrix matrix = matrixPool.isEmpty() ? new Matrix() : matrixPool.removeLast();
        stack.add(matrix.set(last()));
    }

    public void pop() {
        if(isEmpty()) return;
        matrixPool.add(stack.removeLast());
    }

    public Matrix last() {
        return stack.getLast();
    }

    public boolean isEmpty() {
        return stack.size() == 1;
    }

    public void setIdentity() {
        Matrix matrix = last();
        matrix.transform.identity();
        matrix.normal.identity();
        matrix.trustedNormals = true;
    }

    public void mulPose(Matrix4f transform) {
        Matrix matrix = last();
        matrix.transform.mul(transform);
        if(MatrixUtil.isPureTranslation(transform)) return;

        if(MatrixUtil.isOrthonormal(transform)){
            matrix.normal.mul(Reusable.MAT3F.get().set(transform));
        } else matrix.computeNormalMatrix();
    }

    public static class Matrix {

        final Matrix4f transform = new Matrix4f();
        final Matrix3f normal = new Matrix3f();
        private boolean trustedNormals = true;

        Matrix set(Matrix matrix){
            transform.set(matrix.transform);
            normal.set(matrix.normal);
            trustedNormals = matrix.trustedNormals;
            return this;
        }

        void set(PoseStack.Pose pose){
            transform.set(pose.pose());
            normal.set(pose.normal());
            trustedNormals = pose.trustedNormals;
        }

        void computeNormalMatrix() {
            normal.set(transform).invert().transpose();
            trustedNormals = false;
        }

        public Matrix4f transform() {
            return transform;
        }

        public Matrix3f normal() {
            return normal;
        }

        public Vector3f transformNormal(Vector3f vector, Vector3f destination) {
            return transformNormal(vector.x, vector.y, vector.z, destination);
        }

        public Vector3f transformNormal(float x, float y, float z, Vector3f destination) {
            Vector3f vector3f = normal.transform(x, y, z, destination);
            return trustedNormals ? vector3f : vector3f.normalize();
        }
    }
}
