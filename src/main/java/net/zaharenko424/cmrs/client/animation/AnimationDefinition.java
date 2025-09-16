package net.zaharenko424.cmrs.client.animation;

import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public record AnimationDefinition(float lengthInSeconds, boolean looping, Map<String, List<AnimationChannel>> boneAnimations) {

    public static final StreamCodec<FriendlyByteBuf, AnimationDefinition> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            AnimationDefinition::lengthInSeconds,
            ByteBufCodecs.BOOL,
            AnimationDefinition::looping,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.collection(ArrayList::new, AnimationChannel.CODEC)),
            AnimationDefinition::boneAnimations,
            AnimationDefinition::new
    );

    public static class Builder {
        private final float length;
        private final Map<String, List<AnimationChannel>> animationByBone = Maps.newHashMap();
        private boolean looping;

        @Contract("_ -> new")
        public static AnimationDefinition.@NotNull Builder withLength(float lengthSeconds) {
            return new AnimationDefinition.Builder(lengthSeconds);
        }

        private Builder(float lengthSeconds) {
            length = lengthSeconds;
        }

        public AnimationDefinition.Builder looping() {
            looping = true;
            return this;
        }

        public AnimationDefinition.Builder addAnimation(String group, AnimationChannel channel) {
            animationByBone.computeIfAbsent(group, p_232278_ -> Lists.newArrayList()).add(channel);
            return this;
        }

        public AnimationDefinition build() {
            return new AnimationDefinition(length, looping, animationByBone);
        }
    }
}