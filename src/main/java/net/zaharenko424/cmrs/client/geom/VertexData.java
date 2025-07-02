package net.zaharenko424.cmrs.client.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * normal !null -> transformedNormal !null
 */
public record VertexData(Vector3f pos, Vector3f transformedPos, @Nullable Vector3f normal,
                         @Nullable Vector3f transformedNormal, Quad[] quads, Mesh mesh) {

    public VertexData(Vector3f pos, Quad[] quads, Mesh mesh) {
        this(pos, new Vector3f(Float.POSITIVE_INFINITY), null, null, quads, mesh);
    }

    public VertexData(Vector3f pos, Vector3f normal, Quad[] quads, Mesh mesh) {
        this(pos, new Vector3f(Float.POSITIVE_INFINITY), normal, new Vector3f(Float.POSITIVE_INFINITY), quads, mesh);
    }

    public VertexData(Vector3f pos, Quad[] quads) {
        this(pos, new Vector3f(Float.POSITIVE_INFINITY), null, null, quads, null);
    }

    public void resetTransform() {
        transformedPos.set(Float.POSITIVE_INFINITY);
        if (normal != null) transformedNormal.set(Float.POSITIVE_INFINITY);
    }

    public void transform(PoseStack.Pose transform, PoseStack.Pose last, float factor) {
        boolean changed = false;
        if (!transformedPos.isFinite()) {
            transformedPos.set(pos.x / 16, pos.y / 16, pos.z / 16);
            if (factor == 0) {
                transformedPos.mulPosition(last.pose());
            } else if (factor == 1) {
                transformedPos.mulPosition(transform.pose());
            } else {
                transformedPos.mulPosition(last.pose()).lerp(Reusable.VEC3F.get().set(pos.x / 16, pos.y / 16, pos.z / 16).mulPosition(transform.pose()), factor);
            }
            changed = true;
        }

        if (normal != null && !transformedNormal.isFinite()) {
            if (factor == 0) {
                transformedNormal.set(normal).mul(last.normal());
            } else if (factor == 1) {
                transformedNormal.set(normal).mul(transform.normal());
            } else {
                transformedNormal.set(normal).mul(last.normal()).lerp(Reusable.VEC3F.get().set(normal).mul(transform.normal()), factor);
            }
            changed = true;
        }

        if (!changed) return;
        for (Quad quad : quads) {
            quad.transformedNormal.set(Float.NEGATIVE_INFINITY);
        }
    }

    public Vector3f transformOrGet(Matrix4f pose) {
        if (transformedPos.isFinite()) return transformedPos;
        return transformedPos.set(pos.x / 16, pos.y / 16, pos.z / 16).mulPosition(pose);
    }

    public Vector3f transformOrGetNormal(Matrix3f pose) {
        if (normal == null || transformedNormal.isFinite()) return transformedNormal;
        return pose.transform(normal, transformedNormal);
    }
}
