package net.zaharenko424.a_changed.mixin.entityai;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractLatexCat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(Phantom.PhantomSweepAttackGoal.class)
public abstract class MixinPhantomSweepAttackGoal extends Goal {

    @Shadow @Final
    Phantom this$0;

    /**
     * Players with cat transfurs scare off phantoms.
     */
    @ModifyReturnValue(at = @At("TAIL"), method = "canContinueToUse")
    private boolean onNotScaredCheck(boolean isNotScaredOfCats){
        List<Player> list = this$0.level()
                .getEntitiesOfClass(Player.class, this$0.getBoundingBox().inflate(16.0), EntitySelector.ENTITY_STILL_ALIVE);
        boolean latexCat = false;
        for(Player player : list) {
            if(TransfurManager.isTransfurred(player) && TransfurManager.getTransfurType(player) instanceof AbstractLatexCat){
                latexCat = true;
                player.level().playSound(null, player, SoundEvents.CAT_HISS, SoundSource.PLAYERS, 1, player.getVoicePitch());
            }
        }

        return isNotScaredOfCats && !latexCat;
    }
}