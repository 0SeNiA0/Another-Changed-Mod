package net.zaharenko424.a_changed.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.LocalPlayerExtension;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player implements LocalPlayerExtension {

    @Unique
    int achanged$ticks;
    @Unique
    float achanged$targetYRot, achanged$targetXRot, achanged$speed;

    public MixinLocalPlayer(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    /**
     * Allows player to fly when transfurred as flying latex.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"),
            method = "aiStep")
    private boolean onFlyElytraCheck(boolean original) {
        if(TransfurManager.hasFallFlyingAbility(this)) return true;
        return  original;
    }

    /**
     * Makes viewYRot smoothly transition between old and new value
     */
    @ModifyReturnValue(at = @At("TAIL"), method = "getViewYRot")
    private float onGetViewYRot(float original, float partialTicks){
        return achanged$ticks > 0 && !isPassenger() ? super.getViewYRot(partialTicks) : original;
    }

    /**
     * Makes viewXRot smoothly transition between old and new value
     */
    @ModifyReturnValue(at = @At("TAIL"), method = "getViewXRot")
    private float onGetViewXRot(float original, float partialTicks){
        return achanged$ticks > 0 ? super.getViewXRot(partialTicks) : original;
    }

    /**
     * Makes player smoothly transition to the specified x, y rotation.
     * @param xRot target x rotation.
     * @param yRot target y rotation.
     * @param ticks amount of ticks designated for this transition.
     */
    @Override
    public void achanged$lerpLookAt(float xRot, float yRot, float speed, int ticks) {
        if(speed == 0){
            achanged$ticks = 0;
            return;
        }
        achanged$ticks = ticks;
        this.achanged$speed = speed;
        achanged$targetXRot = xRot;
        achanged$targetYRot = yRot;
    }

    @Inject(at = @At("TAIL"), method = "aiStep")
    private void onAiStep(CallbackInfo ci){
        if(achanged$ticks > 1) {
            float f = Mth.rotLerp(achanged$speed, this.getYRot(), achanged$targetYRot);
            float f1 = Mth.lerp(achanged$speed, this.getXRot(), achanged$targetXRot);

            yRotO = getYRot();
            xRotO = getXRot();
            setRot(f, f1);
        }
        if(achanged$ticks > 0) achanged$ticks--;
    }
}