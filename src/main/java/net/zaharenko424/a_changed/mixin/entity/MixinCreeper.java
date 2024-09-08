package net.zaharenko424.a_changed.mixin.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class MixinCreeper extends Monster {

    protected MixinCreeper(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * Players with cat transfurs scare off creepers.
     */
    @Inject(at = @At("TAIL"), method = "registerGoals")
    private void onRegisterGoals(CallbackInfo ci){
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class,
                entity -> !(entity instanceof Player player) || !TransfurManager.isTransfurred(player) || !TransfurManager.hasCatAbility(player),
                6, 1, 1.2,
                entity -> !(entity instanceof Player) || !entity.isSpectator() && !((Player)entity).isCreative()));
    }
}