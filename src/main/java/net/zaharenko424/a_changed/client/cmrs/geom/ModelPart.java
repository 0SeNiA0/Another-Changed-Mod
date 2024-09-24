package net.zaharenko424.a_changed.client.cmrs.geom;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class ModelPart {

    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public float xScale = 1.0F;
    public float yScale = 1.0F;
    public float zScale = 1.0F;
    public boolean visible = true;
    public boolean draw = true;
    private final List<Cube> cubes;
    private final List<Mesh> meshes;
    private final Map<String, ModelPart> children;
    private final Map<String, ModelPart> allParts;
    private final boolean armor;
    private final boolean glowing;
    private PartPose initialPose = PartPose.ZERO;
    private Object2FloatArrayMap<VertexData> animatedVertices;

    public ModelPart(List<Cube> cubes, List<Mesh> meshes, boolean armor, boolean glowing, Map<String, ModelPart> children, Map<String, ModelPart> allParts) {
        this.cubes = cubes;
        this.meshes = meshes;
        this.armor = armor;
        this.glowing = glowing;
        this.children = Map.copyOf(children);
        this.allParts = allParts;
    }

    public boolean isArmor(){
        return armor;
    }

    public boolean isGlowing(){
        return glowing;
    }

    private void addAnimatedVertices(List<VertexData> data, float[] vertices){
        if(animatedVertices == null) animatedVertices = new Object2FloatArrayMap<>(vertices.length / 2);
        for(int i = 0; i < vertices.length;){
            animatedVertices.put(data.get((int) vertices[i++]), vertices[i++]);
        }
    }

    public PartPose storePose() {
        return PartPose.offsetAndRotation(x, y, z, xRot, yRot, zRot);
    }

    public PartPose getInitialPose() {
        return initialPose;
    }

    public void setInitialPose(PartPose pose) {
        initialPose = pose;
    }

    public void resetPose() {
        loadPose(initialPose);
    }

    public void loadPose(PartPose pose) {
        x = pose.x;
        y = pose.y;
        z = pose.z;
        xRot = pose.xRot;
        yRot = pose.yRot;
        zRot = pose.zRot;
        xScale = 1.0F;
        yScale = 1.0F;
        zScale = 1.0F;
    }

    public void copyFrom(ModelPart from) {
        xScale = from.xScale;
        yScale = from.yScale;
        zScale = from.zScale;
        xRot = from.xRot;
        yRot = from.yRot;
        zRot = from.zRot;
        x = from.x;
        y = from.y;
        z = from.z;
    }

    public void copyFrom(net.minecraft.client.model.geom.ModelPart part) {
        xScale = part.xScale;
        yScale = part.yScale;
        zScale = part.zScale;
        xRot = part.xRot;
        yRot = part.yRot;
        zRot = part.zRot;
        x = part.x;
        y = part.y;
        z = part.z;
    }

    public boolean hasChild(String name) {
        return this.children.containsKey(name);
    }

    public boolean hasChildren(){
        return !children.isEmpty();
    }

    public ModelPart getChild(String name) {
        ModelPart modelPart = children.get(name);
        if (modelPart == null) {
            throw new NoSuchElementException("Can't find part " + name);
        } else {
            return modelPart;
        }
    }

    public void setPos(float newX, float newY, float newZ) {
        x = newX;
        y = newY;
        z = newZ;
    }

    public void setRotation(float newXRot, float newYRot, float newZRot) {
        xRot = newXRot;
        yRot = newYRot;
        zRot = newZRot;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        render(poseStack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color){
        render(poseStack, consumer, light, overlay, FastColor.ARGB32.red(color) / 255f, FastColor.ARGB32.green(color) / 255f, FastColor.ARGB32.blue(color) / 255f, FastColor.ARGB32.alpha(color) / 255f);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
        if(!visible || (isEmpty() && children.isEmpty())) return;
        poseStack.pushPose();
        translateAndRotate(poseStack);
        for (Cube cube : cubes) cube.resetTransform();
        for (Mesh mesh : meshes) mesh.resetTransform();


        for (ModelPart modelpart : children.values()) {
            modelpart.render(poseStack, consumer, light, overlay, r, g, b, alpha);
        }

        PoseStack.Pose pose = poseStack.last();
        if(animatedVertices != null) {
            Matrix4f posTransform = pose.pose();
            Matrix3f normalTransform = pose.normal();
            for (Object2FloatMap.Entry<VertexData> entry : animatedVertices.object2FloatEntrySet()) {
                entry.getKey().transform(posTransform, normalTransform, entry.getFloatValue());
            }
        }

        if(draw) {
            for (Cube cube : this.cubes) {
                cube.compile(pose, consumer, light, overlay, r, g, b, alpha);
            }
            for (Mesh mesh : meshes){
                mesh.compile(pose, consumer, light, overlay, r, g, b, alpha);
            }
        }

        poseStack.popPose();
    }

    public void translateAndRotate(PoseStack poseStack) {
        poseStack.translate(x / 16.0F, y / 16.0F, z / 16.0F);
        if (xRot != 0.0F || yRot != 0.0F || zRot != 0.0F) {
            poseStack.mulPose(new Quaternionf().rotationZYX(zRot, yRot, xRot));
        }

        if (xScale != 1.0F || yScale != 1.0F || zScale != 1.0F) {
            poseStack.scale(xScale, yScale, zScale);
        }
    }

    public boolean hasCubes(){
        return !cubes.isEmpty();
    }

    public boolean hasMeshes(){
        return !meshes.isEmpty();
    }

    /**
     * @return Random cube if any or null.
     */
    public Cube getRandomCube(RandomSource random) {
        return hasCubes() ? cubes.get(random.nextInt(cubes.size())) : null;
    }

    public boolean isEmpty() {
        return cubes.isEmpty() && meshes.isEmpty();
    }

    public void offsetPos(Vector3f pos) {
        x += pos.x();
        y += pos.y();
        z += pos.z();
    }

    public void offsetRotation(Vector3f rotation) {
        xRot += rotation.x();
        yRot += rotation.y();
        zRot += rotation.z();
    }

    public void offsetScale(Vector3f scale) {
        xScale += scale.x();
        yScale += scale.y();
        zScale += scale.z();
    }

    public ModelPart partByName(String name){
        return allParts.get(name);
    }

    /**
     * @return Unmodifiable map
     */
    public Map<String, ModelPart> getChildren(){
        return children;
    }

    public Stream<ModelPart> getAllParts() {
        return allParts.values().stream();
    }

    public static class Cube extends Mesh {

        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;

        public Cube(float x, float y, float z, float sizeX, float sizeY, float sizeZ,
                    float inflateX, float inflateY, float inflateZ, CubeUV uv, float textureWidth, float textureHeight) {
            super(buildVertices(x - inflateX, y - inflateY, z - inflateZ,
                            x + sizeX + inflateX, y + sizeY + inflateY, z + sizeZ + inflateZ),
                    new Quad[uv.uv.size()]);
            minX = x;
            minY = y;
            minZ = z;
            maxX = x + sizeX;
            maxY = y + sizeY;
            maxZ = z + sizeZ;

            int i = 0;
            Direction direction;
            for(Map.Entry<Direction, UVData> entry : uv.uv.entrySet()){
                direction = entry.getKey();
                quads[i++] = new Quad(
                        switch(direction){
                            case DOWN ->  quadVertices(4, 3, 7, 0);
                            case UP ->    quadVertices(1, 2, 6, 5);
                            case WEST ->  quadVertices(7, 3, 6, 2);
                            case NORTH -> quadVertices(0, 7, 2, 1);
                            case EAST ->  quadVertices(4, 0, 1, 5);
                            case SOUTH -> quadVertices(3, 4, 5, 6);
                        }, entry.getValue(), textureWidth, textureHeight, direction
                );
            }
        }

        private static @NotNull ImmutableList<VertexData> buildVertices(float x1, float y1, float z1, float x2, float y2, float z2){
            ImmutableList.Builder<VertexData> builder = ImmutableList.builder();
            Quad[] empty = new Quad[0];
            builder.add(new VertexData(new Vector3f(x2, y1, z1), empty));
            builder.add(new VertexData(new Vector3f(x2, y2, z1), empty));
            builder.add(new VertexData(new Vector3f(x1, y2, z1), empty));
            builder.add(new VertexData(new Vector3f(x1, y1, z2), empty));
            builder.add(new VertexData(new Vector3f(x2, y1, z2), empty));
            builder.add(new VertexData(new Vector3f(x2, y2, z2), empty));
            builder.add(new VertexData(new Vector3f(x1, y2, z2), empty));
            builder.add(new VertexData(new Vector3f(x1, y1, z1), empty));
            return builder.build();
        }

        private Vertex @NotNull [] quadVertices(int v0, int v1, int v2, int v3){
            return new Vertex[]{new Vertex(vertexData.get(v0)), new Vertex(vertexData.get(v1)),
                    new Vertex(vertexData.get(v2)), new Vertex(vertexData.get(v3))};
        }
    }

    public static class Mesh {

        protected final ImmutableList<VertexData> vertexData;
        protected final Quad[] quads;

        public Mesh(ImmutableList<VertexData> vertexData, Quad[] quads){
            this.vertexData = vertexData;
            this.quads = quads;
        }

        public Mesh(float[] vertices, float[] quads, float textureWidth, float textureHeight){
            int size = quads.length / 12;
            this.quads = new Quad[size];
            // key = index of vertices, IntList = quad indices
            Int2ObjectOpenHashMap<IntList> map = new Int2ObjectOpenHashMap<>(vertices.length / 3);

            int q = 0, i;
            while (q < size) {
                Vertex[] ar = new Vertex[4];
                for(i = 0; i < 4; i++){
                    ar[i] = vertex(vertices, quads, q, q * 12 + i * 3, textureWidth, textureHeight, map);
                }
                this.quads[q++] = new Quad(ar);
            }

            ImmutableList.Builder<VertexData> builder = ImmutableList.builder();
            for(int vrt = 0; vrt < vertices.length / 3; vrt++){
                IntList qIds = map.get(vrt);
                int a = vrt * 3;

                if(qIds == null) {
                    AChanged.LOGGER.warn("Unused vertex found!");
                    builder.add(createData(readVec(vertices, a), new Quad[0]));
                    continue;
                }

                Quad[] arr = new Quad[qIds.size()];
                for(i = 0; i < qIds.size(); i++){
                    arr[i] = this.quads[qIds.getInt(i)];
                }

                builder.add(createData(readVec(vertices, a), arr));
            }

            vertexData = builder.build();
        }

        protected @NotNull VertexData createData(Vector3f pos, Quad[] quads){
            return new VertexData(pos, quads);
        }

        protected Vector3f readVec(float[] array, int start){
            return new Vector3f(array[start], array[start + 1], array[start + 2]);
        }

        private @NotNull Vertex vertex(float[] vertices, float[] quads, int quadIndex, int vertexIndex, float textureWidth, float textureHeight, Int2ObjectOpenHashMap<IntList> map){
            int vertexDataIndex = (int) quads[vertexIndex];//index of vector in vertices[]

            map.computeIfAbsent(vertexDataIndex, key -> new IntArrayList(4)).add(quadIndex);

            int vertexDataIndexM = vertexDataIndex * 3;     //position of vector in vertices[]
            return new Vertex(readVec(vertices, vertexDataIndexM), Suppliers.memoize(() -> vertexData.get(vertexDataIndex)),
                    quads[vertexIndex + 1] / textureWidth, quads[vertexIndex + 2] / textureHeight);
        }

        Mesh addAnimatedVertices(String[] groups, float[][] vertexInfluence, Map<String, ModelPart> allParts){
            for(int i = 0; i < groups.length; i++){
                if(!allParts.containsKey(groups[i])) continue;
                allParts.get(groups[i]).addAnimatedVertices(vertexData, vertexInfluence[i]);
            }
            return this;
        }

        public void resetTransform(){
            for(VertexData data : vertexData) data.resetTransform();
            for(Quad quad : quads) quad.resetTransform();
        }

        public void compile(PoseStack.Pose pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
            Matrix4f poseM = pose.pose();
            Matrix3f normal = pose.normal();
            for(Quad quad : this.quads) {
                if(quad.transformedNormal.x == Float.NEGATIVE_INFINITY) quad.transformAndUpdateNormal(poseM);
                quad.compile(poseM, normal, consumer, light, overlay, r, g, b, alpha);
            }
        }
    }

    public static class SmoothMesh extends Mesh {

        protected SmoothMesh(ImmutableList<VertexData> vertices, Quad[] quads) {
            super(vertices, quads);
        }

        public SmoothMesh(float[] vertices, float[] quads, float textureWidth, float textureHeight) {
            super(vertices, quads, textureWidth, textureHeight);

            Vector3f[] buffer = {new Vector3f()};
            for(VertexData data : vertexData){
                buffer[0].set(0);
                for(Quad quad : data.quads) buffer[0].add(quad.normal);
                buffer[0].div(data.quads.length, data.normal);
                data.transformedNormal.set(Float.POSITIVE_INFINITY);
            }
        }

        @Override
        protected @NotNull VertexData createData(Vector3f pos, Quad[] quads) {
            return new VertexData(pos, new Vector3f(), quads);
        }

        public void compile(PoseStack.Pose pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
            Matrix4f poseM = pose.pose();
            Matrix3f normalM = pose.normal();
            Vector3f pos;
            Vector3f normal;
            VertexData data;
            for(Quad quad : this.quads) {
                for(Vertex vertex : quad.vertices) {
                    data = vertex.data();
                    pos = data.transformOrGet(poseM);
                    normal = data.transformOrGetNormal(normalM);
                    consumer.addVertex(pos.x(), pos.y(), pos.z(),
                            FastColor.ARGB32.colorFromFloat(alpha, r, g, b),
                            vertex.u, vertex.v,
                            overlay, light,
                            normal.x(), normal.y(), normal.z()
                    );
                }
            }
        }
    }

    public static class Quad {

        public final Vertex[] vertices;
        public final Vector3f normal;
        public final Vector3f transformedNormal;

        public Quad(Vertex[] vertices){
            this.vertices = vertices;

            normal = new Vector3f();
            for (int i = 0; i < 4; i++) {
                normal.add(vertices[i].pos.cross(vertices[(i + 1) % 4].pos, new Vector3f()));
            }
            normal.normalize();
            transformedNormal = new Vector3f(Float.POSITIVE_INFINITY);
        }

        /**
         * Put uv directly in vertices or use UVData here.
         */
        public Quad(Vertex[] vertices, @NotNull UVData uv, float textureWidth, float textureHeight){
            this(vertices);
            remapUV(uv, textureWidth, textureHeight);
        }

        public Quad(Vertex[] vertices, UVData uv, float textureWidth, float textureHeight, Direction direction) {
            this.vertices = vertices;
            vertices[0] = vertices[0].remap(uv.u2() / textureWidth, uv.v1() / textureHeight);
            vertices[1] = vertices[1].remap(uv.u1() / textureWidth, uv.v1() / textureHeight);
            vertices[2] = vertices[2].remap(uv.u1() / textureWidth, uv.v2() / textureHeight);
            vertices[3] = vertices[3].remap(uv.u2() / textureWidth, uv.v2() / textureHeight);

            normal = direction.step();
            transformedNormal = new Vector3f(Float.POSITIVE_INFINITY);
        }

        /**
         * Not for cube quads!
         */
        public void remapUV(UVData uv, float textureWidth, float textureHeight){
            vertices[0] = vertices[0].remap(uv.u1() / textureWidth, uv.v1() / textureHeight);//bb vertex 1
            vertices[1] = vertices[1].remap(uv.u2() / textureWidth, uv.v1() / textureHeight);//bb vertex 3?
            vertices[2] = vertices[2].remap(uv.u2() / textureWidth, uv.v2() / textureHeight);//bb vertex 4
            vertices[3] = vertices[3].remap(uv.u1() / textureWidth, uv.v2() / textureHeight);//bb vertex 2
        }

        public void resetTransform(){
            transformedNormal.set(Float.POSITIVE_INFINITY);
        }

        void transformAndUpdateNormal(Matrix4f pose){
            transformedNormal.set(0);
            Vector3f tmp = Reusable.VEC3F.get();
            for (int i = 0; i < 4; i++) {
                transformedNormal.add(vertices[i].data().transformOrGet(pose).cross(vertices[(i + 1) % 4].data().transformOrGet(pose), tmp));
            }
            transformedNormal.normalize();
        }

        Vector3f transformOrGetNormal(Matrix3f pose){
            if(transformedNormal.isFinite()) return transformedNormal;
            return transformedNormal.set(normal).mul(pose);
        }

        public void compile(Matrix4f posTransform, Matrix3f normalTransform, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha){
            Vector3f vector3f = transformOrGetNormal(normalTransform);
            Vector3f pos;
            Vector3f normal;
            VertexData data;
            for(Vertex vertex : vertices) {
                data = vertex.data();
                pos = data.transformOrGet(posTransform);
                normal = data.normal != null ? data.transformOrGetNormal(normalTransform) : vector3f;
                consumer.addVertex(pos.x(), pos.y(), pos.z(),
                        FastColor.ARGB32.colorFromFloat(alpha, r, g, b),
                        vertex.u, vertex.v,
                        overlay, light,
                        normal.x(), normal.y(), normal.z()
                );
            }
        }
    }

    /**
     * normal !null -> transformedNormal !null
     */
    public record VertexData(Vector3f pos, Vector3f transformedPos, @Nullable Vector3f normal, @Nullable Vector3f transformedNormal, Quad[] quads){

        public VertexData(Vector3f pos, Quad[] quads){
            this(pos, new Vector3f(Float.POSITIVE_INFINITY), null, null, quads);
        }

        public VertexData(Vector3f pos, Vector3f normal, Quad[] quads){
            this(pos, new Vector3f(Float.POSITIVE_INFINITY), normal, new Vector3f(Float.POSITIVE_INFINITY), quads);
        }

        public void resetTransform(){
            transformedPos.set(Float.POSITIVE_INFINITY);
            if(normal != null) transformedNormal.set(Float.POSITIVE_INFINITY);
        }

        public void transform(Matrix4f posTransform, Matrix3f normalTransform, float factor){
            boolean changed = false;
            if(!transformedPos.isFinite()) {
                transformedPos.set(pos.x / 16, pos.y / 16, pos.z / 16)
                        .lerp(Reusable.VEC3F.get().set(transformedPos).mulPosition(posTransform), factor);
                changed = true;
            }

            if(normal != null && !transformedNormal.isFinite()) {
                transformedNormal.set(normal).lerp(Reusable.VEC3F.get().set(normal).mul(normalTransform), factor);
                changed = true;
            }

            if(!changed) return;
            for(Quad quad : quads){
                quad.transformedNormal.set(Float.NEGATIVE_INFINITY);
            }
        }

        public Vector3f transformOrGet(Matrix4f pose){
            if(transformedPos.isFinite()) return transformedPos;
            return transformedPos.set(pos.x / 16, pos.y / 16, pos.z / 16).mulPosition(pose);
        }

        public Vector3f transformOrGetNormal(Matrix3f pose){
            if(normal == null || transformedNormal.isFinite()) return transformedNormal;
            return pose.transform(normal, transformedNormal);
        }
    }

    public record Vertex(Vector3f pos, Supplier<VertexData> dataSupplier, float u, float v) {

        public Vertex(VertexData data){
            this(data.pos, () -> data, 0, 0);
        }

        public VertexData data(){
            return dataSupplier.get();
        }

        @Contract("_, _ -> new")
        public @NotNull Vertex remap(float u, float v) {
            return new Vertex(pos, dataSupplier, u, v);
        }
    }
}