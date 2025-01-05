package net.zaharenko424.a_changed.client.cmrs.properties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.HumanoidArm;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.model.PartTransform;
import net.zaharenko424.a_changed.util.CodecUtils;
import org.jetbrains.annotations.Nullable;

public final class FPArms {

    public static final StreamCodec<FriendlyByteBuf, FPArms> CODEC = StreamCodec.of((buffer, fparms) -> {
        boolean write = fparms.armR != null;
        CodecUtils.writeOptionally(fparms.armR, write, buffer, ByteBufCodecs.STRING_UTF8);
        if(write) PartTransform.CODEC.encode(buffer, fparms.transformR);

        write = fparms.armL != null;
        CodecUtils.writeOptionally(fparms.armL, write, buffer, ByteBufCodecs.STRING_UTF8);
        if(write) PartTransform.CODEC.encode(buffer, fparms.transformL);
    }, buffer -> {
        String armR = CodecUtils.readOptionally(buffer, ByteBufCodecs.STRING_UTF8);
        PartTransform transformR = armR != null ? PartTransform.CODEC.decode(buffer) : null;
        String armL = CodecUtils.readOptionally(buffer, ByteBufCodecs.STRING_UTF8);
        return new FPArms(armR, transformR, armL, armL != null ? PartTransform.CODEC.decode(buffer) : null);
    });

    private String armR;
    private final PartTransform transformR;
    private String armL;
    private final PartTransform transformL;

    public FPArms(@Nullable String armR, @Nullable PartTransform transformR, @Nullable String armL, @Nullable PartTransform transformL){
        this.armR = armR;
        this.transformR = transformR == null ? new PartTransform() : transformR;
        this.armL = armL;
        this.transformL = transformL == null ? new PartTransform() : transformL;
    }

    public ModelPart getTransformed(CustomModel<?> model, HumanoidArm arm){
        String target = arm == HumanoidArm.RIGHT ? armR : armL;
        if(target == null) return null;
        ModelPart part = model.getPart(target);
        if(part == null) return null;
        part.resetPose();
        if(arm == HumanoidArm.RIGHT){
            transformR.apply(part);
        } else transformL.apply(part);
        return part;
    }
}