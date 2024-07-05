package net.zaharenko424.a_changed.mixin.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.entity.AbstractLatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTrader.class)
public abstract class MixinWanderingTrader extends AbstractVillager {

    public MixinWanderingTrader(EntityType<? extends AbstractVillager> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * Makes wandering trader scared of latex.
     */
    @Inject(at = @At("RETURN"), method = "registerGoals")
    private void onRegisterGoals(CallbackInfo ci){
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, AbstractLatexBeast.class, 12, .5, .5, latex ->
                !((AbstractLatexBeast)latex).transfurType.isOrganic()));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 12, .5, .5, entity ->
                entity instanceof Player player && TransfurManager.isTransfurred(player) && !TransfurManager.isOrganic(player)));
    }
}