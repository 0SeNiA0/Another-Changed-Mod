package net.zaharenko424.a_changed.client.model.animation;


import net.minecraft.client.animation.Keyframe;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zaharenko424.a_changed.client.model.HierarchicalModel;
import net.zaharenko424.a_changed.client.model.geom.ModelPart;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class KeyframeAnimations {

    public static void animate(HierarchicalModel<?> p_232320_, AnimationDefinition animation, long time, float scale, Vector3f cache) {
        float f = getElapsedSeconds(animation, time);

        for(Map.Entry<String, List<AnimationChannel>> entry : animation.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = p_232320_.getAnyDescendantWithName(entry.getKey());
            List<AnimationChannel> list = entry.getValue();
            optional.ifPresent(p_232330_ -> list.forEach(p_288241_ -> {
                Keyframe[] akeyframe = p_288241_.keyframes();
                int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, p_232315_ -> f <= akeyframe[p_232315_].timestamp()) - 1);
                int j = Math.min(akeyframe.length - 1, i + 1);
                Keyframe keyframe = akeyframe[i];
                Keyframe keyframe1 = akeyframe[j];
                float f1 = f - keyframe.timestamp();
                float f2;
                if (j != i) {
                    f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                } else {
                    f2 = 0.0F;
                }

                keyframe1.interpolation().apply(cache, f2, akeyframe, i, j, scale);
                p_288241_.target().apply(p_232330_, cache);
            }));
        }
    }

    private static float getElapsedSeconds(AnimationDefinition animation, long time) {
        float f = (float)time / 1000.0F;
        return animation.looping() ? f % animation.lengthInSeconds() : f;
    }
}