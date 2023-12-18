package net.zaharenko424.a_changed.client.test.geom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.model.geom.PartPose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class PartDefinition {
    private final List<CubeDefinition> cubes;
    private final PartPose partPose;
    private final Map<String, PartDefinition> children = Maps.newHashMap();

    PartDefinition(){
        this.cubes=new ArrayList<>();
        this.partPose=PartPose.ZERO;
    }

    PartDefinition(List<CubeDefinition> p_171581_, PartPose p_171582_) {
        this.cubes = p_171581_;
        this.partPose = p_171582_;
    }

    public PartDefinition addOrReplaceChild(String p_171600_, CubeListBuilder p_171601_, PartPose p_171602_) {
        PartDefinition partdefinition = new PartDefinition(p_171601_.cubes(), p_171602_);
        PartDefinition partdefinition1 = this.children.put(p_171600_, partdefinition);
        if (partdefinition1 != null) {
            partdefinition.children.putAll(partdefinition1.children);
        }

        return partdefinition;
    }

    public ModelPart bake(int textureWidth, int textureHeight) {
        Object2ObjectArrayMap<String, ModelPart> object2objectarraymap = this.children
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                p_171593_ -> p_171593_.getValue().bake(textureWidth,textureHeight),
                                (p_171595_, p_171596_) -> p_171595_,
                                Object2ObjectArrayMap::new
                        )
                );
        List<ModelPart.Cube> list = this.cubes.stream().map(p_171589_ -> p_171589_.bake(textureWidth,textureHeight)).collect(ImmutableList.toImmutableList());
        ModelPart modelpart = new ModelPart(list, object2objectarraymap);
        modelpart.setInitialPose(this.partPose);
        modelpart.loadPose(this.partPose);
        return modelpart;
    }

    public PartDefinition getChild(String p_171598_) {
        return this.children.get(p_171598_);
    }
}
