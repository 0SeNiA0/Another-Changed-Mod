package net.zaharenko424.a_changed.client.cmrs.geom;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.a_changed.util.CodecUtils;

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
            CodecUtils.POSE_CODEC,
            definition -> definition.partPose,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, GroupDefinition.CODEC),
            definition -> definition.children,
            ByteBufCodecs.BOOL,
            definition -> definition.armor,
            ByteBufCodecs.BOOL,
            definition -> definition.glowing,
            GroupDefinition::new
    );

    private final List<CubeDefinition> cubes;
    private final List<MeshDefinition> meshes;
    private final PartPose partPose;
    private final Map<String, GroupDefinition> children;
    private final boolean armor;
    private final boolean glowing;

    GroupDefinition(){
        this(List.of(), List.of(), PartPose.ZERO, new HashMap<>(), false, false);
    }

    GroupDefinition(GroupBuilder builder, PartPose pose) {
        this(builder.cubes(), builder.meshes(), pose, new HashMap<>(), builder.armor, builder.glowing);
    }

    GroupDefinition(List<CubeDefinition> cubes, List<MeshDefinition> meshes, PartPose pose, Map<String, GroupDefinition> children, boolean armor, boolean glowing){
        this.cubes = cubes;
        this.meshes = meshes;
        this.partPose = pose;
        this.children = children;
        this.armor = armor;
        this.glowing = glowing;
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
                group -> group.getValue().armor ? group.getValue().bake(64, 32, allParts) : group.getValue().bake(textureWidth, textureHeight, allParts),
                (p_171595_, p_171596_) -> p_171595_,
                Object2ObjectArrayMap::new
        ));

        allParts.putAll(children);

        List<ModelPart.Cube> cubes1 = this.cubes.stream().map(cube -> cube.bake(textureWidth,textureHeight)).toList();
        List<ModelPart.Mesh> meshes1 = meshes.stream().map(meshDef -> {
            ModelPart.Mesh mesh = meshDef.bake(textureWidth, textureHeight);
            return meshDef.groups != null ? mesh.addAnimatedVertices(meshDef.groups, meshDef.vertexInfluence, allParts) : mesh;
        }).toList();

        ModelPart modelpart = new ModelPart(cubes1, meshes1, armor, glowing, children, allParts);
        modelpart.setInitialPose(this.partPose);
        modelpart.loadPose(this.partPose);
        return modelpart;
    }
}