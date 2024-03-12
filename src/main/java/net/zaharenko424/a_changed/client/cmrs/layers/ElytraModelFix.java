package net.zaharenko424.a_changed.client.cmrs.layers;

import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ElytraModelFix<E extends LivingEntity> extends ElytraModel<E> {
    public ElytraModelFix(ModelPart p_170538_) {
        super(p_170538_);
        rightWing.x = 5;
        rightWing.z = -1;
        leftWing.x = -5;
        leftWing.z = -1;
    }

    @Override
    public void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        float f = (float) (Math.PI / 12);
        float f1 = (float) (-Math.PI / 12);
        float f2 = 24;
        float f3 = 0.0F;
        if (entity.isFallFlying()) {
            float f4 = 1.0F;
            Vec3 vec3 = entity.getDeltaMovement();
            if (vec3.y < 0.0) {
                Vec3 vec31 = vec3.normalize();
                f4 = 1.0F - (float)Math.pow(-vec31.y, 1.5);
            }

            f = f4 * (float) (Math.PI / 9) + (1.0F - f4) * f;
            f1 = f4 * (float) (-Math.PI / 2) + (1.0F - f4) * f1;
        } else if (entity.isCrouching()) {
            f = (float) (Math.PI * 2.0 / 9.0);
            f1 = (float) (-Math.PI / 4);
            f2 -= 4;
            f3 = 0.08726646F;
        }

        rightWing.y = f2;
        if (entity instanceof AbstractClientPlayer abstractclientplayer) {
            abstractclientplayer.elytraRotX += (f - abstractclientplayer.elytraRotX) * 0.1F;
            abstractclientplayer.elytraRotY += (f3 - abstractclientplayer.elytraRotY) * 0.1F;
            abstractclientplayer.elytraRotZ += (f1 - abstractclientplayer.elytraRotZ) * 0.1F;
            leftWing.xRot = abstractclientplayer.elytraRotX;
            leftWing.yRot = abstractclientplayer.elytraRotY;
            leftWing.zRot = abstractclientplayer.elytraRotZ;
        } else {
            leftWing.xRot = f;
            leftWing.yRot = f3;
            leftWing.zRot = f1;
        }

        leftWing.zRot += Mth.PI;
        leftWing.y = rightWing.y;
        rightWing.xRot = leftWing.xRot;
        rightWing.yRot = -leftWing.yRot;
        rightWing.zRot = -leftWing.zRot;
    }
}