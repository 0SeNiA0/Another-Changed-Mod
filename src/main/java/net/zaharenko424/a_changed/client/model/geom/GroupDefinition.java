package net.zaharenko424.a_changed.client.model.geom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.model.geom.PartPose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class GroupDefinition {
    private final List<CubeDefinition> cubes;
    private final PartPose partPose;
    private final Map<String, GroupDefinition> children = Maps.newHashMap();

    GroupDefinition(){
        this.cubes=new ArrayList<>();
        this.partPose=PartPose.ZERO;
    }

    GroupDefinition(List<CubeDefinition> p_171581_, PartPose p_171582_) {
        this.cubes = p_171581_;
        this.partPose = p_171582_;
    }

    public GroupDefinition addOrReplaceChild(String name, GroupBuilder builder){
        return addOrReplaceChild(name,builder,PartPose.ZERO);
    }

    public GroupDefinition addOrReplaceChild(String name, GroupBuilder builder, PartPose pose) {
        GroupDefinition groupDefinition = new GroupDefinition(builder.cubes(), pose);
        GroupDefinition groupDefinition1 = this.children.put(name, groupDefinition);
        if (groupDefinition1 != null) {
            groupDefinition.children.putAll(groupDefinition1.children);
        }
        return groupDefinition;
    }

    public ModelPart bake(int textureWidth, int textureHeight) {
        Object2ObjectArrayMap<String, ModelPart> object2objectarraymap = this.children.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            group -> group.getValue().bake(textureWidth,textureHeight),
            (p_171595_, p_171596_) -> p_171595_,
            Object2ObjectArrayMap::new
        ));
        List<ModelPart.Cube> list = this.cubes.stream().map(cube -> cube.bake(textureWidth,textureHeight)).collect(ImmutableList.toImmutableList());
        ModelPart modelpart = new ModelPart(list, object2objectarraymap);
        modelpart.setInitialPose(this.partPose);
        modelpart.loadPose(this.partPose);
        return modelpart;
    }

    public GroupDefinition getChild(String p_171598_) {
        return this.children.get(p_171598_);
    }
}