package net.zaharenko424.a_changed.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractLatexEntityModel<E extends LivingEntity> extends HierarchicalHumanoidModel<E> {

    public AbstractLatexEntityModel(ModelLayerLocation location) {
        super(ModelCache.INSTANCE.bake(location));
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        if(arm == HumanoidArm.RIGHT) rightArm.x += 1;else leftArm.x -= 1;
        super.translateToHand(arm, poseStack);
    }

    public boolean hasGlowParts(){
        return false;
    }
}