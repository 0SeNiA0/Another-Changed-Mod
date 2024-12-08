package net.zaharenko424.a_changed.client.cmrs.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class PoseTransform {//TODO rewrite

    public static final StreamCodec<FriendlyByteBuf, PoseTransform> CODEC = StreamCodec.of((buffer, transform) -> {
        boolean write = !transform.translation.equals(0, 0, 0);
        buffer.writeBoolean(write);
        if(write) buffer.writeVector3f(transform.translation);

        write = transform.rotationRad.x() != 0 || transform.rotationRad.y() != 0 || transform.rotationRad.z() != 0;
        buffer.writeBoolean(write);
        if(write) buffer.writeQuaternion(transform.rotationRad);

        write = !transform.scale.equals(1, 1, 1);
        buffer.writeBoolean(write);
        if(write) buffer.writeVector3f(transform.scale);
    }, buffer -> new PoseTransform(
                    buffer.readBoolean() ? buffer.readVector3f() : null,
                    buffer.readBoolean() ? buffer.readQuaternion() : null,
                    buffer.readBoolean() ? buffer.readVector3f() : null)
    );

    public final Vector3f translation;
    public final Quaternionf rotationRad;
    public final Vector3f scale;

    public PoseTransform(@Nullable Vector3f translation, @Nullable Quaternionf rotationRad, @Nullable Vector3f scale){
        this.translation = translation == null ? new Vector3f() : translation;
        this.rotationRad = rotationRad == null ? new Quaternionf() : rotationRad;
        this.scale = scale == null ? new Vector3f(1) : scale;
    }

    public void apply(PoseStack stack) {
        stack.translate(translation.x, translation.y, translation.z);
        if(rotationRad.x != 0 || rotationRad.y != 0 || rotationRad.z != 0) stack.mulPose(rotationRad);
        if(!scale.equals(1, 1, 1)) stack.scale(scale.x, scale.y, scale.z);
    }
}
