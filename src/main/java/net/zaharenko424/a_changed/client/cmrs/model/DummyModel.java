package net.zaharenko424.a_changed.client.cmrs.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupBuilder;
import net.zaharenko424.a_changed.client.cmrs.geom.GroupDefinition;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelDefinition;
import org.jetbrains.annotations.NotNull;

public class DummyModel<E extends LivingEntity> extends CustomEntityModel<E> {

    public static final ModelLayerLocation bodyLayer = new ModelLayerLocation(new ResourceLocation(AChanged.MODID,"dummy"),"main");
    public static final ResourceLocation TEXTURE = AChanged.textureLoc("entity/dummy");

    public DummyModel() {
        super(bodyLayer, TEXTURE);
    }

    public static @NotNull ModelDefinition bodyLayer(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();
        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create(), PartPose.offset(0f, 0f, 0f));
        root.addOrReplaceChild("head", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("body", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("right_arm", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("left_arm", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offset(0f, 28f, 0f));
        return ModelDefinition.create(modelBuilder,32,32);
    }
}