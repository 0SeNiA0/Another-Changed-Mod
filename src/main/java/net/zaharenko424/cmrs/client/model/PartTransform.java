package net.zaharenko424.cmrs.client.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.zaharenko424.cmrs.client.geom.Node;
import net.zaharenko424.cmrs.util.Utils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;

public final class PartTransform {

    public static final Codec<PartTransform> CODEC_ = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("relative_translation").forGetter(transform -> transform.relativeT),
            ExtraCodecs.VECTOR3F.optionalFieldOf("translation").xmap(optional -> optional.orElse(null), Optional::of).forGetter(transform -> transform.translate),
            Codec.BOOL.fieldOf("relative_rotation").forGetter(transform -> transform.relativeR),
            ExtraCodecs.VECTOR3F.optionalFieldOf("rotation").xmap(optional -> optional.orElse(null), Optional::of).forGetter(transform -> transform.rotate),
            Codec.BOOL.fieldOf("relative_scale").forGetter(transform -> transform.relativeS),
            ExtraCodecs.VECTOR3F.optionalFieldOf("scale").xmap(optional -> optional.orElse(null), Optional::of).forGetter(transform -> transform.scale)
    ).apply(instance, PartTransform::new));

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

    public void apply(Node node){
        if(relativeT){
            if(Utils.isNonOne(translate)) node.translation().add(translate);
        } else {
            Vector3f nodeTranslation = node.translation();
            if(translate.x() != Float.POSITIVE_INFINITY) nodeTranslation.x = translate.x();
            if(translate.y() != Float.POSITIVE_INFINITY) nodeTranslation.y = translate.y();
            if(translate.z() != Float.POSITIVE_INFINITY) nodeTranslation.z = translate.z();
        }

        if(relativeR){
            if(Utils.isNonZero(rotate)) node.rotation().add(rotate);
        } else {
            Vector3f nodeRotation = node.rotation();
            if(rotate.x() != Float.POSITIVE_INFINITY) nodeRotation.x = rotate.x();
            if(rotate.y() != Float.POSITIVE_INFINITY) nodeRotation.y = rotate.y();
            if(rotate.z() != Float.POSITIVE_INFINITY) nodeRotation.z = rotate.z();
        }

        if(relativeS){
            if(Utils.isNonZero(scale)) node.scale().add(scale);
        } else {
            Vector3f nodeScale = node.scale();
            if(scale.x() != Float.POSITIVE_INFINITY) nodeScale.x = scale.x();
            if(scale.y() != Float.POSITIVE_INFINITY) nodeScale.y = scale.y();
            if(scale.z() != Float.POSITIVE_INFINITY) nodeScale.z = scale.z();
        }
    }
}