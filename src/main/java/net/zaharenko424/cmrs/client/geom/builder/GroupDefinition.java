package net.zaharenko424.cmrs.client.geom.builder;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.cmrs.client.geom.Cube;
import net.zaharenko424.cmrs.client.geom.Mesh;
import net.zaharenko424.cmrs.client.geom.ModelPart;
import net.zaharenko424.cmrs.client.geom.PartPoseCodec;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class GroupDefinition {

    public static final StreamCodec<FriendlyByteBuf, GroupDefinition> CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, CubeDefinition.CODEC),
            definition -> definition.cubes,
            ByteBufCodecs.collection(ArrayList::new, MeshDefinition.CODEC),
            definition -> definition.meshes,
            PartPoseCodec.POSE_CODEC,
            definition -> definition.partPose,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, GroupDefinition.CODEC),
            definition -> definition.children,
            GroupDefinition::new
    );

    private final List<CubeDefinition> cubes;
    private final List<MeshDefinition> meshes;
    private final PartPose partPose;
    private final Map<String, GroupDefinition> children;

    GroupDefinition(){
        this(List.of(), List.of(), PartPose.ZERO, new HashMap<>());
    }

    GroupDefinition(GroupBuilder builder, PartPose pose) {
        this(builder.cubes(), builder.meshes(), pose, new HashMap<>());
    }

    GroupDefinition(List<CubeDefinition> cubes, List<MeshDefinition> meshes, PartPose pose, Map<String, GroupDefinition> children){
        this.cubes = cubes;
        this.meshes = meshes;
        this.partPose = pose;
        this.children = children;
    }

    public GroupDefinition addOrReplaceChild(String name, GroupBuilder builder){
        return addOrReplaceChild(name, builder, PartPose.ZERO);
    }

    public GroupDefinition addOrReplaceChild(String name, GroupBuilder builder, PartPose pose) {
        GroupDefinition groupDefinition = new GroupDefinition(builder, pose);
        GroupDefinition groupDefinition1 = this.children.put(name, groupDefinition);
        if (groupDefinition1 != null) {
            groupDefinition.children.putAll(groupDefinition1.children);
        }
        return groupDefinition;
    }

    public ModelPart bake(float textureWidth, float textureHeight){
        return bake(textureWidth, textureHeight, new HashMap<>());
    }

    private ModelPart bake(float textureWidth, float textureHeight, Map<String, ModelPart> allParts) {
        Object2ObjectArrayMap<String, ModelPart> children = this.children.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                group ->  group.getValue().bake(textureWidth, textureHeight, allParts),
                (p_171595_, p_171596_) -> p_171595_,
                Object2ObjectArrayMap::new
        ));

        allParts.putAll(children);

        List<Cube> cubes1 = cubes.stream().map(cube -> cube.bake(textureWidth,textureHeight)).toList();
        List<Mesh> meshes1 = meshes.stream().map(meshDef -> {
            Mesh mesh = meshDef.bake(textureWidth, textureHeight);
            return meshDef.groups != null ? mesh.addAnimatedVertices(meshDef.groups, meshDef.vertexInfluence, allParts) : mesh;
        }).toList();

        ModelPart modelpart = new ModelPart(cubes1, meshes1, children, allParts);
        modelpart.setInitialPose(partPose);
        modelpart.loadPose(partPose);
        return modelpart;
    }
}