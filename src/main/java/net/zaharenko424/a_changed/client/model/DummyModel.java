package net.zaharenko424.a_changed.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.geom.*;
import org.jetbrains.annotations.NotNull;

public class DummyModel<E extends LivingEntity> extends AbstractLatexEntityModel<E> {
    public DummyModel() {
        super(bodyLayer);
    }

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(new ResourceLocation(AChanged.MODID,"dummy"),"main");

    public static @NotNull ModelDefinition bodyLayer(){
        MeshDefinition meshDefinition = new MeshDefinition();
        GroupDefinition groupDefinition = meshDefinition.getRoot();
        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        root.addOrReplaceChild("head", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("body", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("right_arm", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("left_arm", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        return ModelDefinition.create(meshDefinition,32,32);
    }
}