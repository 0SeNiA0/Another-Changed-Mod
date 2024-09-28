package net.zaharenko424.a_changed.client.cmrs.animation;

import net.minecraft.client.animation.Keyframe;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KeyframeAnimator {

    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    public static void animate(AnimationState animState, ModelPart root, AnimationDefinition animation, float ageInTicks) {
        animate(animState, root, animation, ageInTicks, 1.0F);
    }

    public static void animate(AnimationState animState, ModelPart root, AnimationDefinition animation, float ageInTicks, float speed) {
        animState.updateTime(ageInTicks, speed);
        animState.ifStarted(animState1 ->
                animate(root, animation, animState.getAccumulatedTime(), 1.0F));
    }

    public static void animateWalk(ModelPart root, AnimationDefinition animation, float limbSwing, float limbSwingAmount, float maxSpeed, float scale) {
        long i = (long)(limbSwing * 50.0F * maxSpeed);
        float f = Math.min(limbSwingAmount * scale, 1.0F);
        animate(root, animation, i, f);
    }

    public static void applyStatic(ModelPart root, AnimationDefinition definition){
        animate(root, definition, 0L, 1.0F);
    }

    public static void animate(ModelPart root, AnimationDefinition definition, long time, float scale){
        float elapsedSeconds = getElapsedSeconds(definition, time);

        for(Map.Entry<String, List<AnimationChannel>> entry : definition.boneAnimations().entrySet()) {

            Optional<ModelPart> optional = getAnyDescendantWithName(root, entry.getKey());
            if(optional.isEmpty()) continue;
            List<AnimationChannel> list = entry.getValue();
            ModelPart part = optional.get();
            list.forEach(channel -> {
                Keyframe[] akeyframe = channel.keyframes();
                int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, num -> elapsedSeconds <= akeyframe[num].timestamp()) - 1);
                int j = Math.min(akeyframe.length - 1, i + 1);
                Keyframe keyframe = akeyframe[i];
                Keyframe keyframe1 = akeyframe[j];
                float f1 = elapsedSeconds - keyframe.timestamp();
                float f2;
                if (j != i) {
                    f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                } else {
                    f2 = 0.0F;
                }

                keyframe1.interpolation().apply(ANIMATION_VECTOR_CACHE, f2, akeyframe, i, j, scale);
                channel.target().apply(part, ANIMATION_VECTOR_CACHE);
            });
        }
    }

    public static Optional<ModelPart> getAnyDescendantWithName(ModelPart root, String name) {
        return Optional.ofNullable(root.partByName(name));
    }

    public static float getElapsedSeconds(AnimationDefinition animation, long time) {
        float f = (float)time / 1000.0F;
        return animation.looping() ? f % animation.lengthInSeconds() : f;
    }
}