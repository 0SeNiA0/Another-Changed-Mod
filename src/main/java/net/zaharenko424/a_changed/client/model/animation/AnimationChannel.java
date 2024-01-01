package net.zaharenko424.a_changed.client.model.animation;

import net.minecraft.client.animation.Keyframe;
import net.zaharenko424.a_changed.client.model.geom.ModelPart;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record AnimationChannel(AnimationChannel.Target target, Keyframe... keyframes) {
    public AnimationChannel(AnimationChannel.Target target, Keyframe... keyframes) {
        this.target = target;
        this.keyframes = keyframes;
    }

    public AnimationChannel.Target target() {
        return this.target;
    }

    public Keyframe[] keyframes() {
        return this.keyframes;
    }

    public interface Target {
        void apply(ModelPart var1, Vector3f var2);
    }

    public interface Targets {
        AnimationChannel.Target POSITION = ModelPart::offsetPos;
        AnimationChannel.Target ROTATION = ModelPart::offsetRotation;
        AnimationChannel.Target SCALE = ModelPart::offsetScale;
    }
}