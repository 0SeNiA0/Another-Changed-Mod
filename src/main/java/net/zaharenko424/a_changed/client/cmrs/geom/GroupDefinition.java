package net.zaharenko424.a_changed.client.cmrs.geom;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.model.geom.PartPose;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class GroupDefinition {
    private final List<CubeDefinition> cubes;
    private final List<MeshDefinition> meshes;
    private final PartPose partPose;
    private final Map<String, GroupDefinition> children = Maps.newHashMap();
    private final boolean armor;
    private final boolean glowing;

    GroupDefinition(){
        cubes = new ArrayList<>();
        meshes = new ArrayList<>();
        partPose = PartPose.ZERO;
        armor = false;
        glowing = false;
    }

    GroupDefinition(GroupBuilder builder, PartPose pose) {
        this.cubes = builder.cubes();
        this.meshes = builder.meshes();
        this.partPose = pose;
        armor = builder.armor;
        glowing = builder.glowing;
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