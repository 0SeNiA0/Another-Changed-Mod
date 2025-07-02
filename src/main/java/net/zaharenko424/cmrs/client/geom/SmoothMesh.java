package net.zaharenko424.cmrs.client.geom;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.zaharenko424.cmrs.api.ISimpleVertexConsumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SmoothMesh extends Mesh {

    protected SmoothMesh(ImmutableList<VertexData> vertices, Quad[] quads, int renderId) {
        super(vertices, quads, renderId);
    }

    public SmoothMesh(float[] vertices, float[] quads, float textureWidth, float textureHeight, int renderId) {
        super(vertices, quads, textureWidth, textureHeight, renderId);

        Vector3f buffer = Reusable.VEC3F.get();
        for (VertexData data : vertexData) {
            buffer.set(0);
            for (Quad quad : data.quads()) buffer.add(quad.normal);
            buffer.div(data.quads().length, data.normal());
            data.transformedNormal().set(Float.POSITIVE_INFINITY);
        }
    }

    @Override
    protected @NotNull VertexData createData(Vector3f pos, Quad[] quads) {
        return new VertexData(pos, new Vector3f(), quads, this);
    }

    @Override
    public void compile(PoseStack.Pose pose, ISimpleVertexConsumer consumer) {
        Matrix4f poseM = pose.pose();
        Matrix3f normalM = pose.normal();
        VertexData data;
        for (Quad quad : this.quads) {
            for (Vertex vertex : quad.vertices) {
                data = vertex.data();
                consumer.addVertex(data.transformOrGet(poseM),
                        data.transformOrGetNormal(normalM), vertex.u(), vertex.v());
            }
        }
    }
}
