package net.zaharenko424.cmrs.client.geom;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.api.ISimpleVertexConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Map;

public class Mesh {

    protected final ImmutableList<VertexData> vertexData;
    protected final Quad[] quads;
    protected final Vector3f offset = new Vector3f();
    protected boolean animated;
    @ApiStatus.Internal
    public int renderId;

    public Mesh(ImmutableList<VertexData> vertexData, Quad[] quads, int renderId) {
        this.vertexData = vertexData;
        this.quads = quads;
        this.renderId = renderId;
    }

    public Mesh(float[] vertices, float[] quads, float textureWidth, float textureHeight, int renderId) {
        this.renderId = renderId;
        int size = quads.length / 12;
        this.quads = new Quad[size];
        // key = index of vertices, IntList = quad indices
        Int2ObjectOpenHashMap<IntList> map = new Int2ObjectOpenHashMap<>(vertices.length / 3);

        int q = 0, i;
        while (q < size) {
            Vertex[] ar = new Vertex[4];
            for (i = 0; i < 4; i++) {
                ar[i] = vertex(vertices, quads, q, q * 12 + i * 3, textureWidth, textureHeight, map);
            }
            this.quads[q++] = new Quad(ar);
        }

        ImmutableList.Builder<VertexData> builder = ImmutableList.builder();
        for (int vrt = 0; vrt < vertices.length / 3; vrt++) {
            IntList qIds = map.get(vrt);
            int a = vrt * 3;

            if (qIds == null) {
                CMRS.LOGGER.warn("Unused vertex found!");
                builder.add(createData(readVec(vertices, a), new Quad[0]));
                continue;
            }

            Quad[] arr = new Quad[qIds.size()];
            for (i = 0; i < qIds.size(); i++) {
                arr[i] = this.quads[qIds.getInt(i)];
            }

            builder.add(createData(readVec(vertices, a), arr));
        }

        vertexData = builder.build();
    }

    protected @NotNull VertexData createData(Vector3f pos, Quad[] quads) {
        return new VertexData(pos, quads, this);
    }

    protected Vector3f readVec(float[] array, int start) {
        return new Vector3f(array[start], array[start + 1], array[start + 2]);
    }

    private @NotNull Vertex vertex(float[] vertices, float[] quads, int quadIndex, int vertexIndex, float textureWidth, float textureHeight, Int2ObjectOpenHashMap<IntList> map) {
        int vertexDataIndex = (int) quads[vertexIndex];//index of vector in vertices[]

        map.computeIfAbsent(vertexDataIndex, key -> new IntArrayList(4)).add(quadIndex);

        int vertexDataIndexM = vertexDataIndex * 3;     //position of vector in vertices[]
        return new Vertex(readVec(vertices, vertexDataIndexM), Suppliers.memoize(() -> vertexData.get(vertexDataIndex)),
                quads[vertexIndex + 1] / textureWidth, quads[vertexIndex + 2] / textureHeight);
    }

    //TODO put Map<String(modelPart name), List<IntFloatPair(vert index, influence)>> in each animated mesh ? -> all the data is contained inside the mesh. To check the names would need BiMap(allParts) or use modelPart as key here
    @ApiStatus.Internal
    public Mesh addAnimatedVertices(String[] groups, float[][] vertexInfluence, Map<String, ModelPart> partLookup) {
        animated = true;
        for (int i = 0; i < groups.length; i++) {
            if (!partLookup.containsKey(groups[i])) continue;
            partLookup.get(groups[i]).addAnimatedVertices(vertexData, vertexInfluence[i]);
        }
        return this;
    }

    public void resetTransform() {
        for (VertexData data : vertexData) data.resetTransform();
        for (Quad quad : quads) quad.resetTransform();
        offset.set(0);
    }

    public void compile(PoseStack.Pose matrix, ISimpleVertexConsumer consumer) {
        Matrix4f poseM = matrix.pose();
        Matrix3f normal = matrix.normal();
        for (Quad quad : this.quads) {
            if (quad.transformedNormal.x == Float.NEGATIVE_INFINITY) quad.transformAndUpdateNormal(poseM);
            quad.compile(poseM, normal, consumer);
        }
    }
}
