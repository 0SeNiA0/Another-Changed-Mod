package net.zaharenko424.a_changed.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.model.geom.ModelPart;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public abstract class AbstractLatexEntityModel<E extends LivingEntity> extends HierarchicalHumanoidModel<E> {

    public AbstractLatexEntityModel(ModelLayerLocation location) {
        super(ModelCache.INSTANCE.bake(location));
    }

    public void setAllVisible(boolean b){
        root().getAllParts().filter((part)->part!=root()).forEach((child->child.visible=b));
    }

    public void setAllVisible(ModelPart part, boolean b){
        part.getAllParts().forEach((child)->child.visible=b);
    }

    @Override
    public void translateToHand(HumanoidArm p_102108_, PoseStack p_102109_) {
        if(p_102108_==HumanoidArm.RIGHT) {
            rightArm.y=2;
            rightArm.x+=1;
            rightArm.translateAndRotate(p_102109_);
        } else {
            leftArm.y=2;
            leftArm.x-=1;
            leftArm.translateAndRotate(p_102109_);
        }
    }
}