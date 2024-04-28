package net.zaharenko424.a_changed.client.cmrs.geom;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
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
    private final boolean armor;
    private final boolean glowing;
    private PartPose initialPose = PartPose.ZERO;

    public ModelPart(List<ModelPart.Cube> cubes, List<ModelPart.Mesh> meshes, boolean armor, boolean glowing, Map<String, ModelPart> children) {
        this.cubes = cubes;
        this.meshes = meshes;
        this.armor = armor;
        this.glowing = glowing;
        this.children = Map.copyOf(children);
    }

    public boolean isArmor(){
        return armor;
    }

    public boolean isGlowing(){
        return glowing;
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
        ModelPart modelpart = children.get(name);
        if (modelpart == null) {
            throw new NoSuchElementException("Can't find part " + name);
        } else {
            return modelpart;
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

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
        if(!visible || (isEmpty() && children.isEmpty())) return;
        poseStack.pushPose();
        translateAndRotate(poseStack);

        if (draw) {
            PoseStack.Pose pose = poseStack.last();
            for (Cube cube : this.cubes) {
                cube.compile(pose, consumer, light, overlay, r, g, b, alpha);
            }
            for (Mesh mesh : meshes){
                mesh.compile(pose, consumer, light, overlay, r, g, b, alpha);
            }
        }

        for (ModelPart modelpart : children.values()) {
            modelpart.render(poseStack, consumer, light, overlay, r, g, b, alpha);
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
    public ModelPart.Cube getRandomCube(RandomSource random) {
        return hasCubes() ? cubes.get(random.nextInt(cubes.size())) : null;
    }

    /**
     * @return First mesh if any or null.
     */
    public ModelPart.Mesh getMesh(){
        return hasMeshes() ? meshes.get(0) : null;
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

    /**
     * @return Unmodifiable map
     */
    public Map<String, ModelPart> getChildren(){
        return children;
    }

    public Stream<ModelPart> getAllParts() {
        return Stream.concat(Stream.of(this), children.values().stream().flatMap(ModelPart::getAllParts));
    }

    public static class Cube extends Mesh {

        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;

        public Cube(float x1, float y1, float z1, float sizeX, float sizeY, float sizeZ,
                    float inflateX, float inflateY, float inflateZ, boolean mirror,
                    CubeUV uv, float textureWidth, float textureHeight) {
            super(buildVertices(x1 - inflateX, y1 - inflateY, z1 - inflateZ,
                    x1 + sizeX + inflateX, y1 + sizeY + inflateY, z1 + sizeZ + inflateZ, mirror),
                    new Quad[uv.uv.size()]);
            minX = x1;
            minY = y1;
            minZ = z1;
            maxX = x1 + sizeX;
            maxY = y1 + sizeY;
            maxZ = z1 + sizeZ;

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
                        }, entry.getValue(), textureWidth, textureHeight, mirror, direction
                );
            }
        }

        private static @NotNull ImmutableList<Vector3f> buildVertices(float x1, float y1, float z1, float x2, float y2, float z2, boolean mirror){
            if (mirror) {//Swap X start with X end
                float f3 = x2;
                x2 = x1;
                x1 = f3;
            }
            ImmutableList.Builder<Vector3f> builder = new ImmutableList.Builder<>();
            builder.add(new Vector3f(x2, y1, z1));
            builder.add(new Vector3f(x2, y2, z1));
            builder.add(new Vector3f(x1, y2, z1));
            builder.add(new Vector3f(x1, y1, z2));
            builder.add(new Vector3f(x2, y1, z2));
            builder.add(new Vector3f(x2, y2, z2));
            builder.add(new Vector3f(x1, y2, z2));
            builder.add(new Vector3f(x1, y1, z1));
            return builder.build();
        }

        private Vertex @NotNull [] quadVertices(int v0, int v1, int v2, int v3){
            return new Vertex[]{new Vertex(vertices.get(v0)), new Vertex(vertices.get(v1)),
                    new Vertex(vertices.get(v2)), new Vertex(vertices.get(v3))};
        }
    }

    public static class Mesh {

        protected final ImmutableList<Vector3f> vertices;
        protected final Quad[] quads;

        protected Mesh(ImmutableList<Vector3f> vertices, Quad[] quads){
            this.vertices = vertices;
            this.quads = quads;
        }

        public Mesh(float[] vertices, float[] quads, float textureWidth, float textureHeight){
            ImmutableList.Builder<Vector3f> builder = ImmutableList.builder();
            for(int i = 0; i < vertices.length;){
                builder.add(new Vector3f(vertices[i++], vertices[i++], vertices[i++]));
            }
            this.vertices = builder.build();

            int size = quads.length / 12;
            this.quads = new Quad[size];

            int q = 0, i = 0;
            while (q < size) {
                this.quads[q++] = new Quad(new Vertex[]{vertex(quads, i++, textureWidth, textureHeight),
                        vertex(quads, i++, textureWidth, textureHeight),
                        vertex(quads, i++, textureWidth, textureHeight),
                        vertex(quads, i++, textureWidth, textureHeight)});
            }
        }

        private @NotNull Vertex vertex(float[] quads, int quadIndex, float textureWidth, float textureHeight){
            quadIndex *= 3;
            return new Vertex(vertices.get((int) quads[quadIndex]), quads[quadIndex + 1] / textureWidth, quads[quadIndex + 2] / textureHeight);
        }

        public void animateRawGeometry(Consumer<ImmutableList<Vector3f>> vertexConsumer){
            vertexConsumer.accept(vertices);
        }

        public void compile(PoseStack.Pose pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
            Matrix4f poseM = pose.pose();
            Matrix3f normal = pose.normal();
            for(Quad quad : this.quads) {
                quad.compile(poseM, normal, consumer, light, overlay, r, g, b, alpha);
            }
        }
    }

    public static class Triangle {

        public final Vertex[] vertices;
        public final Vector3f normal;

        /**
         * Put uv directly in vertices or use UVData here.
         */
        public Triangle(Vertex[] vertices, @Nullable UVData uv, float textureWidth, float textureHeight){
            this.vertices = vertices;

            if(uv != null) remapUV(uv, textureWidth, textureHeight);

            normal = new Vector3f();
            for (int i = 0; i < 3; i++) {
                normal.add(vertices[i].pos.cross(vertices[(i + 1) % 3].pos, new Vector3f()));
            }
            normal.normalize();
        }

        public void remapUV(UVData uv, float textureWidth, float textureHeight){
            vertices[0] = vertices[0].remap(uv.u1() / textureWidth, uv.v1() / textureHeight);
            vertices[1] = vertices[1].remap(uv.u2() / textureWidth, uv.v1() / textureHeight);
            vertices[2] = vertices[2].remap(uv.u1() / textureWidth, uv.v2() / textureHeight);
        }

        public void compile(Matrix4f pose, Matrix3f normal, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha){
            Vector3f vector3f = normal.transform(new Vector3f(this.normal));
            for(Vertex vertex : vertices) {
                Vector4f vector4f = pose.transform(new Vector4f(vertex.pos.x() / 16.0F, vertex.pos.y() / 16.0F, vertex.pos.z() / 16.0F, 1.0F));
                consumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(),
                        r, g, b, alpha,
                        vertex.u, vertex.v,
                        overlay, light,
                        vector3f.x(), vector3f.y(), vector3f.z()
                );
            }
        }
    }

    public static class Quad {

        public final Vertex[] vertices;
        public final Vector3f normal;

        public Quad(Vertex[] vertices){
            this.vertices = vertices;

            normal = new Vector3f();
            for (int i = 0; i < 4; i++) {
                normal.add(vertices[i].pos.cross(vertices[(i + 1) % 4].pos, new Vector3f()));
            }
            normal.normalize();
        }

        /**
         * Put uv directly in vertices or use UVData here.
         */
        public Quad(Vertex[] vertices, @NotNull UVData uv, float textureWidth, float textureHeight){
            this(vertices);
            remapUV(uv, textureWidth, textureHeight);
        }

        public Quad(Vertex[] vertices, UVData uv, float textureWidth, float textureHeight, boolean mirror, Direction direction) {
            this.vertices = vertices;
            vertices[0] = vertices[0].remap(uv.u2() / textureWidth, uv.v1() / textureHeight);
            vertices[1] = vertices[1].remap(uv.u1() / textureWidth, uv.v1() / textureHeight);
            vertices[2] = vertices[2].remap(uv.u1() / textureWidth, uv.v2() / textureHeight);
            vertices[3] = vertices[3].remap(uv.u2() / textureWidth, uv.v2() / textureHeight);
            if (mirror) {
                int i = vertices.length;

                for(int j = 0; j < i / 2; ++j) {
                    Vertex vertex = vertices[j];
                    vertices[j] = vertices[i - 1 - j];
                    vertices[i - 1 - j] = vertex;
                }
            }

            normal = direction.step();
            if (mirror) {
                normal.mul(-1.0F, 1.0F, 1.0F);
            }
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

        public void compile(Matrix4f pose, Matrix3f normal, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha){
            Vector3f vector3f = normal.transform(new Vector3f(this.normal));
            for(Vertex vertex : vertices) {
                Vector4f vector4f = pose.transform(new Vector4f(vertex.pos.x() / 16.0F, vertex.pos.y() / 16.0F, vertex.pos.z() / 16.0F, 1.0F));
                consumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(),
                        r, g, b, alpha,
                        vertex.u, vertex.v,
                        overlay, light,
                        vector3f.x(), vector3f.y(), vector3f.z()
                );
            }
        }
    }

    public record Vertex(Vector3f pos, float u, float v) {

        public Vertex(float x, float y, float z) {
            this(new Vector3f(x, y, z), 0, 0);
        }

        public Vertex(Vector3f pos){
            this(pos, 0, 0);
        }

        @Contract("_, _ -> new")
        public @NotNull Vertex remap(float u, float v) {
            return new Vertex(pos, u, v);
        }
    }
}