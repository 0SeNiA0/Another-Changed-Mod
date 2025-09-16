package net.zaharenko424.cmrs.client.animation;

import net.minecraft.client.animation.Keyframe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.cmrs.client.geom.ModelPart;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record AnimationChannel(AnimationChannel.Target target, Keyframe... keyframes) {

    public static final StreamCodec<FriendlyByteBuf, Keyframe> KEYFRAME_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            Keyframe::timestamp,
            ByteBufCodecs.VECTOR3F,
            Keyframe::target,
            StreamCodec.of((buffer, interp)->
                    buffer.writeByte(interp == net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR ? 0 : 1),
                    buffer -> buffer.readByte() == 0 ? net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR : net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
            Keyframe::interpolation,
            Keyframe::new
    );

    public static final StreamCodec<FriendlyByteBuf, AnimationChannel> CODEC = StreamCodec.composite(
            StreamCodec.of((buffer, target) ->
                buffer.writeByte(target == Targets.POSITION ? 0 : target == Targets.ROTATION ? 1 : 2),
            buffer -> switch(buffer.readByte()){
                case 0 -> Targets.POSITION;
                case 1 -> Targets.ROTATION;
                default -> Targets.SCALE;
            }),
            AnimationChannel::target,
            StreamCodec.of((buffer, array) -> buffer.writeArray(array, KEYFRAME_CODEC),
                    buffer -> buffer.readArray(Keyframe[]::new, KEYFRAME_CODEC)),
            AnimationChannel::keyframes,
            AnimationChannel::new
    );

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