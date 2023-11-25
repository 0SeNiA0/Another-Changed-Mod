package net.zaharenko424.testmod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLatexEntityModel<T extends LivingEntity> extends HierarchicalHumanoidModel<T> implements ArmedModel, HeadedModel {

	public boolean isCrouching;
	public float swimAmount;
	public float attackTime;

	public AbstractLatexEntityModel(@NotNull ModelPart root) {
		super(root,0,0);
	}

	@Override
	public void setupAnim(@NotNull T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
		super.setupAnim(p_102618_,p_102619_,p_102620_,p_102621_,p_102622_,p_102623_);
	}

	@Override
	public void prepareMobModel(@NotNull T p_102614_, float p_102615_, float p_102616_, float p_102617_) {
		swimAmount=p_102614_.getSwimAmount(p_102617_);
		super.prepareMobModel(p_102614_, p_102615_, p_102616_, p_102617_);
	}

	public void setAllVisible(boolean b){
		root().getAllParts().filter((part)->part!=root()).forEach((child->child.visible=b));
	}

	public void setAllVisible(@NotNull ModelPart part, boolean b){
		part.getAllParts().forEach((child)->child.visible=b);
	}

    @Override
	public void translateToHand(@NotNull HumanoidArm p_102108_, @NotNull PoseStack p_102109_) {
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