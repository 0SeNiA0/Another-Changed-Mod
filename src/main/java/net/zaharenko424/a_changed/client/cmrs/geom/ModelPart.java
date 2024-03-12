package net.zaharenko424.a_changed.client.cmrs.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

    public boolean hasChild(String p_233563_) {
        return this.children.containsKey(p_233563_);
    }

    public boolean hasChildren(){
        return !children.isEmpty();
    }

    public ModelPart getChild(String p_171325_) {
        ModelPart modelpart = children.get(p_171325_);
        if (modelpart == null) {
            throw new NoSuchElementException("Can't find part " + p_171325_);
        } else {
            return modelpart;
        }
    }

    public void setPos(float p_104228_, float p_104229_, float p_104230_) {
        x = p_104228_;
        y = p_104229_;
        z = p_104230_;
    }

    public void setRotation(float p_171328_, float p_171329_, float p_171330_) {
        xRot = p_171328_;
        yRot = p_171329_;
        zRot = p_171330_;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        render(poseStack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
        if(!visible || (cubes.isEmpty() && meshes.isEmpty() && children.isEmpty())) return;
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

    public ModelPart.Cube getRandomCube(RandomSource random) {
        return this.cubes.get(random.nextInt(cubes.size()));
    }

    public boolean isEmpty() {
        return this.cubes.isEmpty();
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
     * @return immutable map
     */
    public Map<String, ModelPart> getChildren(){
        return children;
    }

    public Stream<ModelPart> getAllParts() {
        return Stream.concat(Stream.of(this), children.values().stream().flatMap(ModelPart::getAllParts));
    }

    public static class Cube {

        private final Quad[] quads;
        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;

        public Cube(float x1, float y1, float z1, float sizeX, float sizeY, float sizeZ,
                    float inflateX, float inflateY, float inflateZ, boolean mirror,
                    CubeUV uv, float scaleU, float scaleV) {
            minX = x1;
            minY = y1;
            minZ = z1;
            maxX = x1 + sizeX;
            maxY = y1 + sizeY;
            maxZ = z1 + sizeZ;
            quads = new Quad[uv.uv.size()];
            float x2 = x1 + sizeX;
            float y2 = y1 + sizeY;
            float z2 = z1 + sizeZ;
            x1 -= inflateX;
            y1 -= inflateY;
            z1 -= inflateZ;
            x2 += inflateX;
            y2 += inflateY;
            z2 += inflateZ;

            if (mirror) {//Swap X start with X end
                float f3 = x2;
                x2 = x1;
                x1 = f3;
            }

            Vertex vertex0 = new Vertex(x2, y1, z1);
            Vertex vertex1 = new Vertex(x2, y2, z1);
            Vertex vertex2 = new Vertex(x1, y2, z1);
            Vertex vertex3 = new Vertex(x1, y1, z2);
            Vertex vertex4 = new Vertex(x2, y1, z2);
            Vertex vertex5 = new Vertex(x2, y2, z2);
            Vertex vertex6 = new Vertex(x1, y2, z2);
            Vertex vertex7 = new Vertex(x1, y1, z1);
            int i = 0;
            Direction direction;
            for(Map.Entry<Direction,UVData> entry : uv.uv.entrySet()){
                direction = entry.getKey();
                quads[i] = new Quad(
                        switch(direction){
                            case DOWN ->  new Vertex[]{vertex4, vertex3, vertex7, vertex0};
                            case UP ->    new Vertex[]{vertex1, vertex2, vertex6, vertex5};
                            case WEST ->  new Vertex[]{vertex7, vertex3, vertex6, vertex2};
                            case NORTH -> new Vertex[]{vertex0, vertex7, vertex2, vertex1};
                            case EAST ->  new Vertex[]{vertex4, vertex0, vertex1, vertex5};
                            case SOUTH -> new Vertex[]{vertex3, vertex4, vertex5, vertex6};
                        }, entry.getValue(), scaleU, scaleV, mirror, direction
                );
                i++;
            }
        }

        public void compile(PoseStack.Pose pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
            Matrix4f poseM = pose.pose();
            Matrix3f normal = pose.normal();
            for(Quad quad : this.quads) {
                quad.compile(poseM, normal, consumer, light, overlay, r, g, b, alpha);
            }
        }
    }

    public static class Mesh {

        private final Quad[] quads;

        public Mesh(float[] vertices, float[] quads, float textureWidth, float textureHeight){
            int size = quads.length / 8;
            this.quads = new Quad[size];

            int q = 0, i = 0;
            while (q < size) {
                this.quads[q++] = new Quad(new Vertex[]{vertex(vertices, i++), vertex(vertices, i++), vertex(vertices, i++), vertex(vertices, i++)},
                        new UVData(quads[i++], quads[i++], quads[i++], quads[i++]), textureWidth, textureHeight);
            }
        }

        @Contract("_, _ -> new")
        private @NotNull Vertex vertex(float[] vertices, int i){
            return new Vertex(vertices[i++], vertices[i++], vertices[i]);
        }

        public void compile(PoseStack.Pose pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
            Matrix4f poseM = pose.pose();
            Matrix3f normal = pose.normal();
            for(Quad quad : this.quads) {
                quad.compile(poseM, normal, consumer, light, overlay, r, g, b, alpha);
            }
        }
    }

    public static class Quad {
        public final Vertex[] vertices;
        public final Vector3f normal;

        public Quad(Vertex[] vertices, UVData uv, float textureWidth, float textureHeight){
            this.vertices = vertices;
            remapUV(uv, textureWidth, textureHeight);

            normal = new Vector3f();
            for (int i = 0; i < 4; i++) {
                normal.add(vertices[i].pos.cross(vertices[(i + 1) % 4].pos, new Vector3f()));
            }
            normal.normalize();
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

    public static class Vertex {
        public final Vector3f pos;
        public final float u;
        public final float v;

        public Vertex(float x, float y, float z) {
            this(new Vector3f(x, y, z), 0, 0);
        }

        public Vertex remap(float u, float v) {
            return new Vertex(this.pos, u, v);
        }

        public Vertex(Vector3f pos, float u, float v) {
            this.pos = pos;
            this.u = u;
            this.v = v;
        }
    }
}