package net.zaharenko424.a_changed.client.model.animation;

import com.google.common.collect.Maps;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public record AnimationDefinition(float lengthInSeconds, boolean looping, Map<String, List<AnimationChannel>> boneAnimations) {
    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final float length;
        private final Map<String, List<AnimationChannel>> animationByBone = Maps.newHashMap();
        private boolean looping;

        @Contract("_ -> new")
        public static AnimationDefinition.@NotNull Builder withLength(float lengthSeconds) {
            return new AnimationDefinition.Builder(lengthSeconds);
        }

        private Builder(float lengthSeconds) {
            this.length = lengthSeconds;
        }

        public AnimationDefinition.Builder looping() {
            this.looping = true;
            return this;
        }

        public AnimationDefinition.Builder addAnimation(String group, AnimationChannel channel) {
            this.animationByBone.computeIfAbsent(group, p_232278_ -> Lists.newArrayList()).add(channel);
            return this;
        }

        public AnimationDefinition build() {
            return new AnimationDefinition(this.length, this.looping, this.animationByBone);
        }
    }
}