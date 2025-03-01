package net.zaharenko424.a_changed.client.cmrs.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.a_changed.client.cmrs.geom.MatrixStack;
import net.zaharenko424.a_changed.util.CodecUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record PoseTransform(Vector3f translation, Quaternionf rotationRad, Vector3f scale) {

    public static final StreamCodec<FriendlyByteBuf, PoseTransform> CODEC = StreamCodec.of((buffer, transform) -> {
                CodecUtils.writeOptionally(transform.translation, !transform.translation.equals(0, 0, 0), buffer, ByteBufCodecs.VECTOR3F);
                Quaternionf rotationRad = transform.rotationRad;
                CodecUtils.writeOptionally(rotationRad, rotationRad.x == 0 && rotationRad.y == 0 && rotationRad.z == 0, buffer, ByteBufCodecs.QUATERNIONF);
                CodecUtils.writeOptionally(transform.scale, !transform.scale.equals(1, 1, 1), buffer, ByteBufCodecs.VECTOR3F);
            }, buffer -> new PoseTransform(
                    CodecUtils.readOptionally(buffer, ByteBufCodecs.VECTOR3F),
                    CodecUtils.readOptionally(buffer, ByteBufCodecs.QUATERNIONF),
                    CodecUtils.readOptionally(buffer, ByteBufCodecs.VECTOR3F))
    );

    public PoseTransform(){
        this(null, null, null);
    }

    public PoseTransform(@Nullable Vector3f translation, @Nullable Quaternionf rotationRad, @Nullable Vector3f scale) {
        this.translation = translation == null ? new Vector3f() : translation;
        this.rotationRad = rotationRad == null ? new Quaternionf() : rotationRad;
        this.scale = scale == null ? new Vector3f(1) : scale;
    }

    public void apply(MatrixStack stack) {
        stack.translate(translation.x, translation.y, translation.z);
        if (rotationRad.x != 0 || rotationRad.y != 0 || rotationRad.z != 0) stack.mulPose(rotationRad);
        if (!scale.equals(1, 1, 1)) stack.scale(scale.x, scale.y, scale.z);
    }

    public void apply(PoseStack stack) {
        stack.translate(translation.x, translation.y, translation.z);
        if (rotationRad.x != 0 || rotationRad.y != 0 || rotationRad.z != 0) stack.mulPose(rotationRad);
        if (!scale.equals(1, 1, 1)) stack.scale(scale.x, scale.y, scale.z);
    }

    public boolean isEmpty() {
        return translation.equals(0, 0, 0)
                && rotationRad.x == 0 && rotationRad.y == 0 && rotationRad.z == 0
                && scale.equals(1, 1, 1);
    }
}