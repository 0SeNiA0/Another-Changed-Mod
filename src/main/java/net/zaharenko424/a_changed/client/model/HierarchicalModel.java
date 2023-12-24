package net.zaharenko424.a_changed.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.model.animation.AnimationDefinition;
import net.zaharenko424.a_changed.client.model.animation.KeyframeAnimations;
import net.zaharenko424.a_changed.client.model.geom.ModelPart;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Function;
@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public abstract class HierarchicalModel<E extends Entity> extends EntityModel<E> {
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    public HierarchicalModel() {
        this(RenderType::entityCutoutNoCull);
    }

    public HierarchicalModel(Function<ResourceLocation, RenderType> p_170623_) {
        super(p_170623_);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
        this.root().render(poseStack, consumer, light, overlay, r, g, b, alpha);
    }

    public abstract ModelPart root();

    public Optional<ModelPart> getAnyDescendantWithName(String name) {
        return name.equals("root") ? Optional.of(this.root())
                : this.root().getAllParts().filter(p_233400_ -> p_233400_.hasChild(name)).findFirst().map(p_233397_ -> p_233397_.getChild(name));
    }

    protected void animate(AnimationState animState, AnimationDefinition animation, float ageInTicks) {
        this.animate(animState, animation, ageInTicks, 1.0F);
    }

    protected void animateWalk(AnimationDefinition animation, float limbSwing, float limbSwingAmount, float maxSpeed, float scale) {
        long i = (long)(limbSwing * 50.0F * maxSpeed);
        float f = Math.min(limbSwingAmount * scale, 1.0F);
        KeyframeAnimations.animate(this, animation, i, f, ANIMATION_VECTOR_CACHE);
    }

    protected void animate(AnimationState animState, AnimationDefinition animation, float ageInTicks, float speed) {
        animState.updateTime(ageInTicks, speed);
        animState.ifStarted(animState1 -> KeyframeAnimations.animate(this, animation, animState.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE));
    }

    protected void applyStatic(AnimationDefinition p_288996_) {
        KeyframeAnimations.animate(this, p_288996_, 0L, 1.0F, ANIMATION_VECTOR_CACHE);
    }
}