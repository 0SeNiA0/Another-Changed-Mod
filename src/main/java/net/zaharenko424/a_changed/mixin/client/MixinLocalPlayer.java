package net.zaharenko424.a_changed.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractFlyingLatex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player {

    public MixinLocalPlayer(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    /**
     * Allows player to fly when transfurred as flying latex.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"),
            method = "aiStep")
    private boolean onFlyElytraCheck(boolean original) {
        ITransfurHandler handler = TransfurCapability.of(this);
        if(handler != null && handler.isTransfurred() && handler.getTransfurType() instanceof AbstractFlyingLatex) return true;
        return original;
    }
}