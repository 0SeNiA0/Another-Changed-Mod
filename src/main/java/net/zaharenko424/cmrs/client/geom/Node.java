package net.zaharenko424.cmrs.client.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.util.RandomSource;
import net.zaharenko424.cmrs.api.ISimpleVertexConsumer;
import net.zaharenko424.cmrs.api.MatrixStack;
import net.zaharenko424.cmrs.client.model.RenderStack;
import net.zaharenko424.cmrs.util.Utils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class Node implements net.zaharenko424.cmrs.api.Node {

    private final Vector3f translation = new Vector3f();
    private final Vector3f rotation = new Vector3f();
    private final Vector3f scale = new Vector3f(1);

    public float xRot;
    public boolean visible = true;
    public boolean draw = true;
    private final List<Cube> cubes;
    private final List<Mesh> meshes;
    private final Map<String, Node> children;
    private final Map<String, Node> allChildren;
    private final Map<String, Node> allNodes;
    private PartPose initialPose = PartPose.ZERO;
    private Object2FloatArrayMap<VertexData> animatedVertices;

    public Node(List<Cube> cubes, List<Mesh> meshes, Map<String, Node> children, Map<String, Node> allNodes) {
        this.cubes = cubes;
        this.meshes = meshes;
        this.children = children;

        allChildren = new HashMap<>();
        this.children.forEach((name, part) -> allChildren.putAll(part.allChildren));
        allChildren.putAll(this.children);

        this.allNodes = allNodes;
    }

    void addAnimatedVertices(List<VertexData> data, float[] vertices){
        if(animatedVertices == null) animatedVertices = new Object2FloatArrayMap<>(vertices.length / 2);
        for(int i = 0; i < vertices.length;){
            animatedVertices.put(data.get((int) vertices[i++]), vertices[i++]);
        }
    }

    @Override
    public Vector3f translation() {
        return translation;
    }

    @Override
    public Vector3f rotation() {
        return rotation;
    }

    @Override
    public Vector3f scale() {
        return scale;
    }

    @Override
    public Node getNode(String name) {
        return getDirectChild(name);
    }

    /*public PartPose storePose() {//TODO make an alternative?
        return PartPose.offsetAndRotation(x, y, z, xRot, yRot, zRot);
    }*/

    public PartPose getInitialPose() {
        return initialPose;
    }

    public void setInitialPose(PartPose pose) {
        initialPose = pose;
    }

    @Override
    public void resetPose() {
        loadPose(initialPose);
    }

    public void loadPose(PartPose pose) {
        translation.set(pose.x, pose.y, pose.z);
        rotation.set(pose.zRot, pose.yRot, pose.xRot);//TODO move to Quaternion eventually?
        //rotation.set(Reusable.QUATERNION.get().identity().rotationZYX(pose.zRot, pose.yRot, pose.xRot));
        scale.set(1);
    }

    public void copyFrom(Node from) {
        translation.set(from.translation);
        rotation.set(from.rotation);
        scale.set(from.scale);
    }

    public void copyFrom(ModelPart part) {
        translation.set(part.x, part.y, part.z);
        rotation.set(part.zRot, part.yRot, part.xRot);
        scale.set(part.xScale, part.yScale, part.zScale);
    }

    @Override
    public boolean hasNode(String name) {
        return this.children.containsKey(name);
    }

    public boolean hasChildren(){
        return !children.isEmpty();
    }

    public Node getDirectChild(String name) {
        Node node = children.get(name);
        if (node == null) {
            throw new NoSuchElementException("Can't find part " + name);
        } else {
            return node;
        }
    }

    public @Nullable Node getPart(String name) {
        return allNodes.get(name);
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

        for (Node modelpart : children.values()) {
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

    public void render(PoseStack poseStack, RenderStack stack){
        render(poseStack, stack, new ObjectArrayList<>());
    }

    private void render(PoseStack matrixStack, RenderStack stack, List<Mesh> animated) {
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

        for (Node modelpart : children.values()) {
            modelpart.render(matrixStack, stack, animated);
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
                stack.renderMesh(cube, pose);
            }
            for (Mesh mesh : meshes) {
                stack.renderMesh(mesh, pose);
                animated.remove(mesh);
            }
        }

        for(Mesh anim : animated) {
            anim.offset.sub(initialPose.x, initialPose.y, initialPose.z);
        }
        MatrixStack.pop(matrixStack);
    }

    public void translateAndRotate(PoseStack poseStack) {
        if(Utils.isNonZero(translation)) MatrixStack.translate(poseStack, translation.div(16, Reusable.VEC3F.get()));

        if(Utils.isNonZero(rotation)) poseStack.mulPose(Reusable.QUATERNION.get().identity().rotationZYX(rotation.z, rotation.y, rotation.x));

        if(Utils.isNonOne(scale)) MatrixStack.scale(poseStack, scale);
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

    /**
     * @return Unmodifiable map
     */
    public Map<String, Node> getChildren(){
        return children;
    }

    public Stream<Node> getAllNodes() {
        return allNodes.values().stream();
    }

    public Stream<Node> getAllChildParts(){
        return allChildren.values().stream();
    }
}