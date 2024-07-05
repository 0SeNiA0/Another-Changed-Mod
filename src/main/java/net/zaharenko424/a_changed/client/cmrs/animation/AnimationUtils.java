package net.zaharenko424.a_changed.client.cmrs.animation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AnimationUtils {

    static void animateCrossbowHold(ModelPart p_102098_, ModelPart p_102099_, ModelPart p_102100_, boolean p_102101_) {
        ModelPart modelpart = p_102101_ ? p_102098_ : p_102099_;
        ModelPart modelpart1 = p_102101_ ? p_102099_ : p_102098_;
        modelpart.yRot = (p_102101_ ? 0.3F : -0.3F) + p_102100_.yRot;
        modelpart1.yRot = (p_102101_ ? -0.6F : 0.6F) + p_102100_.yRot;
        modelpart.xRot = (float) (-Math.PI / 2) + p_102100_.xRot + 0.1F;
        modelpart1.xRot = -1.5F + p_102100_.xRot;
    }

    static void animateCrossbowCharge(ModelPart p_102087_, ModelPart p_102088_, LivingEntity p_102089_, boolean p_102090_) {
        ModelPart modelpart = p_102090_ ? p_102087_ : p_102088_;
        ModelPart modelpart1 = p_102090_ ? p_102088_ : p_102087_;
        modelpart.yRot = p_102090_ ? 0.8F : -0.8F;
        modelpart.xRot = 0.97079635F;
        modelpart1.xRot = modelpart.xRot;
        float f = (float) CrossbowItem.getChargeDuration(p_102089_.getUseItem());
        float f1 = Mth.clamp((float)p_102089_.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;
        modelpart1.yRot = Mth.lerp(f2, 0.4F, 0.85F) * (float)(p_102090_ ? -1 : 1);
        modelpart1.xRot = Mth.lerp(f2, modelpart1.xRot, (float) Math.PI / 2);
    }

    static void bobModelPart(ModelPart p_170342_, float p_170343_, float p_170344_) {
        p_170342_.zRot += p_170344_ * (Mth.cos(p_170343_ * 0.09F) * 0.05F + 0.05F);
        p_170342_.xRot += p_170344_ * Mth.sin(p_170343_ * 0.067F) * 0.05F;
    }
}