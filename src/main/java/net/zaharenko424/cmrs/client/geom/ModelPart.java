package net.zaharenko424.cmrs.client.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.util.RandomSource;
import net.zaharenko424.cmrs.api.ISimpleVertexConsumer;
import net.zaharenko424.cmrs.api.MatrixStack;
import net.zaharenko424.cmrs.client.model.RenderStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
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
    public float xScale = 1;
    public float yScale = 1;
    public float zScale = 1;
    public boolean visible = true;
    public boolean draw = true;
    private final List<Cube> cubes;
    private final List<Mesh> meshes;
    private final Map<String, ModelPart> children;
    private final Map<String, ModelPart> allChildren;
    private final Map<String, ModelPart> allParts;
    private PartPose initialPose = PartPose.ZERO;
    private Object2FloatArrayMap<VertexData> animatedVertices;

    public ModelPart(List<Cube> cubes, List<Mesh> meshes, Map<String, ModelPart> children, Map<String, ModelPart> allParts) {
        this.cubes = cubes;
        this.meshes = meshes;
        this.children = children;

        allChildren = new HashMap<>();
        this.children.forEach((name, part) -> allChildren.putAll(part.allChildren));
        allChildren.putAll(this.children);

        this.allParts = allParts;
    }

    void addAnimatedVertices(List<VertexData> data, float[] vertices){
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

    public ModelPart getDirectChild(String name) {
        ModelPart modelPart = children.get(name);
        if (modelPart == null) {
            throw new NoSuchElementException("Can't find part " + name);
        } else {
            return modelPart;
        }
    }

    public @Nullable ModelPart getPart(String name) {
        return allParts.get(name);
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
        render(poseStack, consumer, light, overlay, -1);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int argb){
        SimpleVertexConsumer simple = Reusable.SIMPLE_CONSUMER.get();
        render(poseStack, simple.wrap(consumer, overlay, light, argb), new ObjectArrayList<>());
        simple.reset();
    }

    private void render(PoseStack matrixStack, ISimpleVertexConsumer consumer, List<Mesh> animated) {
        if(!visible || (isEmpty() && (animatedVertices == null || animatedVertices.isEmpty()))) return;
        PoseStack.Pose last = matrixStack.last();
        MatrixStack.push(matrixStack);
        translateAndRotate(matrixStack);

        for (Mesh anim : animated) {
            anim.offset.add(initialPose.x, initialPose.y, initialPose.z);
        }

        if(draw) {
            for (Cube cube : cubes) cube.resetTransform();
            for (Mesh mesh : meshes) {
                mesh.resetTransform();
                if (mesh.animated) animated.add(mesh);
            }
        }

        for (ModelPart modelpart : children.values()) {
            modelpart.render(matrixStack, consumer, animated);
        }

        PoseStack.Pose pose = matrixStack.last();
        if(animatedVertices != null) {
            VertexData data;
            Vector3f offset;
            for (Object2FloatMap.Entry<VertexData> entry : animatedVertices.object2FloatEntrySet()) {
                data = entry.getKey();
                offset = data.mesh().offset;
                matrixStack.translate(-offset.x / 16, -offset.y / 16, -offset.z / 16);
                data.transform(pose, last, entry.getFloatValue());
                matrixStack.translate(offset.x / 16, offset.y / 16, offset.z / 16);
            }
        }

        if(draw) {
            for (Cube cube : this.cubes) {
                cube.compile(pose, consumer);
            }
            for (Mesh mesh : meshes) {
                mesh.compile(pose, consumer);
                animated.remove(mesh);
            }
        }

        for(Mesh anim : animated) {
            anim.offset.sub(initialPose.x, initialPose.y, initialPose.z);
        }
        MatrixStack.pop(matrixStack);
    }

    public void render(PoseStack poseStack, RenderStack stack, int light, int overlay, int color){
        render(poseStack, stack, light, overlay, color, new ObjectArrayList<>());
    }

    private void render(PoseStack matrixStack, RenderStack stack, int light, int overlay, int color, List<Mesh> animated) {
        if(!visible || (isEmpty() && (animatedVertices == null || animatedVertices.isEmpty()))) return;
        PoseStack.Pose last = matrixStack.last();
        MatrixStack.push(matrixStack);
        translateAndRotate(matrixStack);

        for (Mesh anim : animated) {
            anim.offset.add(initialPose.x, initialPose.y, initialPose.z);
        }

        if(draw) {
            for (Cube cube : cubes) cube.resetTransform();
            for (Mesh mesh : meshes) {
                mesh.resetTransform();
                if (mesh.animated) animated.add(mesh);
            }
        }

        for (ModelPart modelpart : children.values()) {
            modelpart.render(matrixStack, stack, light, overlay, color, animated);
        }

        PoseStack.Pose pose = matrixStack.last();
        if(animatedVertices != null) {
            VertexData data;
            Vector3f offset;
            for (Object2FloatMap.Entry<VertexData> entry : animatedVertices.object2FloatEntrySet()) {
                data = entry.getKey();
                offset = data.mesh().offset;
                matrixStack.translate(-offset.x / 16, -offset.y / 16, -offset.z / 16);
                data.transform(pose, last, entry.getFloatValue());
                matrixStack.translate(offset.x / 16, offset.y / 16, offset.z / 16);
            }
        }

        if(draw) {
            for (Cube cube : this.cubes) {
                stack.renderMesh(cube, pose, light, overlay, color);
            }
            for (Mesh mesh : meshes) {
                stack.renderMesh(mesh, pose, light, overlay, color);
                animated.remove(mesh);
            }
        }

        for(Mesh anim : animated) {
            anim.offset.sub(initialPose.x, initialPose.y, initialPose.z);
        }
        MatrixStack.pop(matrixStack);
    }

    public void translateAndRotate(PoseStack poseStack) {
        if(x != 0 || y != 0 || z != 0) poseStack.translate(x / 16, y / 16, z / 16);

        if (xRot != 0 || yRot != 0 || zRot != 0) {
            poseStack.mulPose(Reusable.QUATERNION.get().identity().rotationZYX(zRot, yRot, xRot));
        }

        if (xScale != 1 || yScale != 1 || zScale != 1) {
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
        return cubes.isEmpty() && meshes.isEmpty() && children.isEmpty();
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
        return allParts.values().stream();
    }

    public Stream<ModelPart> getAllChildParts(){
        return allChildren.values().stream();
    }
}