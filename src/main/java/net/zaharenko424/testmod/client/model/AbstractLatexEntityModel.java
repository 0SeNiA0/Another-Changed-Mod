package net.zaharenko424.testmod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLatexEntityModel<T extends LivingEntity> extends HierarchicalModel<T> {

	protected final ModelPart body;

	public AbstractLatexEntityModel(@NotNull ModelPart root) {
		this.body = root.getChild("body");
	}

	@Override
	public @NotNull ModelPart root() {
		return body;
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}