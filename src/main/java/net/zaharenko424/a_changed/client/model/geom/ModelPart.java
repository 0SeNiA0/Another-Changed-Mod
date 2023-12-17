package net.zaharenko424.a_changed.client.model.geom;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
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
    public boolean skipDraw;
    private final List<ModelPart.Cube> cubes;
    private final Map<String, ModelPart> children;
    private PartPose initialPose = PartPose.ZERO;

    public ModelPart(List<ModelPart.Cube> p_171306_, Map<String, ModelPart> p_171307_) {
        this.cubes = p_171306_;
        this.children = p_171307_;
    }

    public PartPose storePose() {
        return PartPose.offsetAndRotation(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot);
    }

    public PartPose getInitialPose() {
        return this.initialPose;
    }

    public void setInitialPose(PartPose p_233561_) {
        this.initialPose = p_233561_;
    }

    public void resetPose() {
        this.loadPose(this.initialPose);
    }

    public void loadPose(PartPose p_171323_) {
        this.x = p_171323_.x;
        this.y = p_171323_.y;
        this.z = p_171323_.z;
        this.xRot = p_171323_.xRot;
        this.yRot = p_171323_.yRot;
        this.zRot = p_171323_.zRot;
        this.xScale = 1.0F;
        this.yScale = 1.0F;
        this.zScale = 1.0F;
    }

    public void copyFrom(ModelPart p_104316_) {
        this.xScale = p_104316_.xScale;
        this.yScale = p_104316_.yScale;
        this.zScale = p_104316_.zScale;
        this.xRot = p_104316_.xRot;
        this.yRot = p_104316_.yRot;
        this.zRot = p_104316_.zRot;
        this.x = p_104316_.x;
        this.y = p_104316_.y;
        this.z = p_104316_.z;
    }

    public boolean hasChild(String p_233563_) {
        return this.children.containsKey(p_233563_);
    }

    public ModelPart getChild(String p_171325_) {
        ModelPart modelpart = this.children.get(p_171325_);
        if (modelpart == null) {
            throw new NoSuchElementException("Can't find part " + p_171325_);
        } else {
            return modelpart;
        }
    }

    public void setPos(float p_104228_, float p_104229_, float p_104230_) {
        this.x = p_104228_;
        this.y = p_104229_;
        this.z = p_104230_;
    }

    public void setRotation(float p_171328_, float p_171329_, float p_171330_) {
        this.xRot = p_171328_;
        this.yRot = p_171329_;
        this.zRot = p_171330_;
    }

    public void render(PoseStack p_104302_, VertexConsumer p_104303_, int light, int overlay) {
        this.render(p_104302_, p_104303_, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack p_104307_, VertexConsumer p_104308_, int light, int overlay, float r, float g, float b, float alpha) {
        if (this.visible) {
            if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
                p_104307_.pushPose();
                this.translateAndRotate(p_104307_);
                if (!this.skipDraw) {
                    this.compile(p_104307_.last(), p_104308_, light, overlay, r, g, b, alpha);
                }

                for(ModelPart modelpart : this.children.values()) {
                    modelpart.render(p_104307_, p_104308_, light, overlay, r, g, b, alpha);
                }

                p_104307_.popPose();
            }
        }
    }

    public void translateAndRotate(PoseStack p_104300_) {
        p_104300_.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        if (this.xRot != 0.0F || this.yRot != 0.0F || this.zRot != 0.0F) {
            p_104300_.mulPose(new Quaternionf().rotationZYX(this.zRot, this.yRot, this.xRot));
        }

        if (this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F) {
            p_104300_.scale(this.xScale, this.yScale, this.zScale);
        }
    }

    private void compile(
            PoseStack.Pose p_104291_, VertexConsumer p_104292_, int light, int overlay, float p_104295_, float p_104296_, float p_104297_, float p_104298_
    ) {
        for(ModelPart.Cube modelpart$cube : this.cubes) {
            modelpart$cube.compile(p_104291_, p_104292_, light, overlay, p_104295_, p_104296_, p_104297_, p_104298_);
        }
    }

    public ModelPart.Cube getRandomCube(RandomSource p_233559_) {
        return this.cubes.get(p_233559_.nextInt(this.cubes.size()));
    }

    public boolean isEmpty() {
        return this.cubes.isEmpty();
    }

    public void offsetPos(Vector3f p_253873_) {
        this.x += p_253873_.x();
        this.y += p_253873_.y();
        this.z += p_253873_.z();
    }

    public void offsetRotation(Vector3f p_253983_) {
        this.xRot += p_253983_.x();
        this.yRot += p_253983_.y();
        this.zRot += p_253983_.z();
    }

    public void offsetScale(Vector3f p_253957_) {
        this.xScale += p_253957_.x();
        this.yScale += p_253957_.y();
        this.zScale += p_253957_.z();
    }

    public Stream<ModelPart> getAllParts() {
        return Stream.concat(Stream.of(this), this.children.values().stream().flatMap(ModelPart::getAllParts));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Cube {
        private final Polygon[] polygons;
        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;

        public Cube(float x1, float y1, float z1, float x2, float y2, float z2,
                float inflateX, float inflateY, float inflateZ, boolean mirror,
                ImmutableMap<Direction,UVData> uv, float scaleU, float scaleV) {
            this.minX = x1;
            this.minY = y1;
            this.minZ = z1;
            this.maxX = x1 + x2;
            this.maxY = y1 + y2;
            this.maxZ = z1 + z2;
            this.polygons = new Polygon[uv.size()];
            float f = x1 + x2;
            float f1 = y1 + y2;
            float f2 = z1 + z2;
            x1 -= inflateX;
            y1 -= inflateY;
            z1 -= inflateZ;
            f += inflateX;
            f1 += inflateY;
            f2 += inflateZ;
            if (mirror) {
                float f3 = f;
                f = x1;
                x1 = f3;
            }

            Vertex modelpart$vertex7 = new Vertex(x1, y1, z1, 0.0F, 0.0F);
            Vertex modelpart$vertex = new Vertex(f, y1, z1, 0.0F, 8.0F);
            Vertex modelpart$vertex1 = new Vertex(f, f1, z1, 8.0F, 8.0F);
            Vertex modelpart$vertex2 = new Vertex(x1, f1, z1, 8.0F, 0.0F);
            Vertex modelpart$vertex3 = new Vertex(x1, y1, f2, 0.0F, 0.0F);
            Vertex modelpart$vertex4 = new Vertex(f, y1, f2, 0.0F, 8.0F);
            Vertex modelpart$vertex5 = new Vertex(f, f1, f2, 8.0F, 8.0F);
            Vertex modelpart$vertex6 = new Vertex(x1, f1, f2, 8.0F, 0.0F);
            int i = 0;
            Direction direction;
            for(Map.Entry<Direction,UVData> entry: uv.entrySet()){
                direction=entry.getKey();
                polygons[i]=new Polygon(
                        switch(direction){
                            case DOWN -> new Vertex[]{modelpart$vertex4, modelpart$vertex3, modelpart$vertex7, modelpart$vertex};
                            case UP -> new Vertex[]{modelpart$vertex1, modelpart$vertex2, modelpart$vertex6, modelpart$vertex5};
                            case WEST -> new Vertex[]{modelpart$vertex7, modelpart$vertex3, modelpart$vertex6, modelpart$vertex2};
                            case NORTH -> new Vertex[]{modelpart$vertex, modelpart$vertex7, modelpart$vertex2, modelpart$vertex1};
                            case EAST -> new Vertex[]{modelpart$vertex4, modelpart$vertex, modelpart$vertex1, modelpart$vertex5};
                            case SOUTH -> new Vertex[]{modelpart$vertex3, modelpart$vertex4, modelpart$vertex5, modelpart$vertex6};
                        },entry.getValue(),scaleU,scaleV,mirror,direction
                );
                i++;
            }
            /*if (uv.containsKey(Direction.DOWN)) {
                this.polygons[i++] = new Polygon(
                        new Vertex[]{modelpart$vertex4, modelpart$vertex3, modelpart$vertex7, modelpart$vertex},
                        uv.get(Direction.DOWN),
                        scaleU,
                        scaleV,
                        mirror,
                        Direction.DOWN
                );
            }

            if (uv.containsKey(Direction.UP)) {
                this.polygons[i++] = new Polygon(
                        new Vertex[]{modelpart$vertex1, modelpart$vertex2, modelpart$vertex6, modelpart$vertex5},
                        uv.get(Direction.UP),
                        scaleU,
                        scaleV,
                        mirror,
                        Direction.UP
                );
            }

            if (uv.containsKey(Direction.WEST)) {
                this.polygons[i++] = new Polygon(
                        new Vertex[]{modelpart$vertex7, modelpart$vertex3, modelpart$vertex6, modelpart$vertex2},
                        uv.get(Direction.WEST),
                        scaleU,
                        scaleV,
                        mirror,
                        Direction.WEST
                );
            }

            if (uv.containsKey(Direction.NORTH)) {
                this.polygons[i++] = new Polygon(
                        new Vertex[]{modelpart$vertex, modelpart$vertex7, modelpart$vertex2, modelpart$vertex1},
                        f5,
                        f11,
                        f6,
                        f12,
                        scaleU,
                        scaleV,
                        mirror,
                        Direction.NORTH
                );
            }

            if (uv.containsKey(Direction.EAST)) {
                this.polygons[i++] = new Polygon(
                        new Vertex[]{modelpart$vertex4, modelpart$vertex, modelpart$vertex1, modelpart$vertex5},
                        f6,
                        f11,
                        f8,
                        f12,
                        scaleU,
                        scaleV,
                        mirror,
                        Direction.EAST
                );
            }

            if (uv.containsKey(Direction.SOUTH)) {
                this.polygons[i] = new Polygon(
                        new Vertex[]{modelpart$vertex3, modelpart$vertex4, modelpart$vertex5, modelpart$vertex6},
                        f8,
                        f11,
                        f9,
                        f12,
                        scaleU,
                        scaleV,
                        mirror,
                        Direction.SOUTH
                );
            }*/
        }

        public void compile(PoseStack.Pose pose, VertexConsumer vertexConsumer, int light, int overlay, float r, float g, float b, float alpha) {
            Matrix4f matrix4f = pose.pose();
            Matrix3f matrix3f = pose.normal();

            for(Polygon modelpart$polygon : this.polygons) {
                Vector3f vector3f = matrix3f.transform(new Vector3f(modelpart$polygon.normal));
                float f = vector3f.x();
                float f1 = vector3f.y();
                float f2 = vector3f.z();

                for(Vertex modelpart$vertex : modelpart$polygon.vertices) {
                    float f3 = modelpart$vertex.pos.x() / 16.0F;
                    float f4 = modelpart$vertex.pos.y() / 16.0F;
                    float f5 = modelpart$vertex.pos.z() / 16.0F;
                    Vector4f vector4f = matrix4f.transform(new Vector4f(f3, f4, f5, 1.0F));
                    vertexConsumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(),
                            r, g, b, alpha,
                            modelpart$vertex.u, modelpart$vertex.v,
                            overlay, light,
                            f, f1, f2
                    );
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class Polygon {
        public final Vertex[] vertices;
        public final Vector3f normal;

        public Polygon(Vertex[] vertices, UVData uv, float textureWidth, float textureHeight, boolean mirror, Direction direction) {
            this.vertices = vertices;
            float f = 0.0F / textureWidth;
            float f1 = 0.0F / textureHeight;
            vertices[0] = vertices[0].remap(uv.u2() / textureWidth - f, uv.v1() / textureHeight + f1);
            vertices[1] = vertices[1].remap(uv.u1() / textureWidth + f, uv.v1() / textureHeight + f1);
            vertices[2] = vertices[2].remap(uv.u1() / textureWidth + f, uv.v2() / textureHeight - f1);
            vertices[3] = vertices[3].remap(uv.u2() / textureWidth - f, uv.v2() / textureHeight - f1);
            if (mirror) {
                int i = vertices.length;

                for(int j = 0; j < i / 2; ++j) {
                    Vertex modelpart$vertex = vertices[j];
                    vertices[j] = vertices[i - 1 - j];
                    vertices[i - 1 - j] = modelpart$vertex;
                }
            }

            this.normal = direction.step();
            if (mirror) {
                this.normal.mul(-1.0F, 1.0F, 1.0F);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class Vertex {
        public final Vector3f pos;
        public final float u;
        public final float v;

        public Vertex(float p_104375_, float p_104376_, float p_104377_, float p_104378_, float p_104379_) {
            this(new Vector3f(p_104375_, p_104376_, p_104377_), p_104378_, p_104379_);
        }

        public Vertex remap(float p_104385_, float p_104386_) {
            return new Vertex(this.pos, p_104385_, p_104386_);
        }

        public Vertex(Vector3f p_253667_, float p_253662_, float p_254308_) {
            this.pos = p_253667_;
            this.u = p_253662_;
            this.v = p_254308_;
        }
    }
}