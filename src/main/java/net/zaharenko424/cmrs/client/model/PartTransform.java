package net.zaharenko424.cmrs.client.model;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.cmrs.client.geom.ModelPart;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public final class PartTransform {

    public static final StreamCodec<FriendlyByteBuf, PartTransform> CODEC = StreamCodec.of((buffer, transform) -> {
        boolean write = transform.relativeT ? !transform.translate.equals(0, 0, 0) : transform.translate.isFinite();
        buffer.writeBoolean(transform.relativeT);
        buffer.writeBoolean(write);
        if(write) buffer.writeVector3f(transform.translate);

        write = transform.relativeR ? !transform.rotate.equals(0, 0, 0) : transform.rotate.isFinite();
        buffer.writeBoolean(transform.relativeR);
        buffer.writeBoolean(write);
        if(write) buffer.writeVector3f(transform.rotate);

        write = transform.relativeS ? !transform.scale.equals(0, 0, 0) : !transform.scale.equals(1, 1, 1);
        buffer.writeBoolean(transform.relativeS);
        buffer.writeBoolean(write);
        if(write) buffer.writeVector3f(transform.scale);
    }, buffer -> {
        boolean relativeT = buffer.readBoolean();
        boolean read = buffer.readBoolean();
        Vector3f translate = read ? buffer.readVector3f() : null;

        boolean relativeR = buffer.readBoolean();
        read = buffer.readBoolean();
        Vector3f rotate = read ? buffer.readVector3f() : null;

        boolean relativeS = buffer.readBoolean();
        read = buffer.readBoolean();
        return new PartTransform(relativeT, translate, relativeR, rotate, relativeS, read ? buffer.readVector3f() : null);
    });

    public boolean relativeT;
    public final Vector3f translate;
    public boolean relativeR;
    public final Vector3f rotate;
    public boolean relativeS;
    public final Vector3f scale;

    public PartTransform(){
        this(false, null, false, null, false, null);
    }

    public PartTransform(boolean relativeT, @Nullable Vector3f translate, boolean relativeR, @Nullable Vector3f rotate, boolean relativeS, @Nullable Vector3f scale){
        this.relativeT = relativeT;
        this.translate = translate == null ? (relativeT ? new Vector3f() : new Vector3f(Float.POSITIVE_INFINITY)) : translate;
        this.relativeR = relativeR;
        this.rotate = rotate == null ? (relativeR ? new Vector3f() : new Vector3f(Float.POSITIVE_INFINITY)) : rotate;
        this.relativeS = relativeS;
        this.scale = scale == null ? (relativeS ? new Vector3f() : new Vector3f(1)) : scale;
    }

    public void apply(ModelPart part){
        if(relativeT){
            if(!translate.equals(0, 0, 0)) part.offsetPos(translate);
        } else {
            if(translate.x() != Float.POSITIVE_INFINITY) part.x = translate.x();
            if(translate.y() != Float.POSITIVE_INFINITY) part.y = translate.y();
            if(translate.z() != Float.POSITIVE_INFINITY) part.z = translate.z();
        }

        if(relativeR){
            if(!rotate.equals(0, 0, 0)) part.offsetRotation(rotate);
        } else {
            if(rotate.x() != Float.POSITIVE_INFINITY) part.xRot = rotate.x();
            if(rotate.y() != Float.POSITIVE_INFINITY) part.yRot = rotate.y();
            if(rotate.z() != Float.POSITIVE_INFINITY) part.zRot = rotate.z();
        }

        if(relativeS){
            if(!scale.equals(0, 0, 0)) part.offsetScale(scale);
        } else {
            if(scale.x() != Float.POSITIVE_INFINITY) part.zScale = scale.x();
            if(scale.y() != Float.POSITIVE_INFINITY) part.zScale = scale.y();
            if(scale.z() != Float.POSITIVE_INFINITY) part.zScale = scale.z();
        }
    }
}