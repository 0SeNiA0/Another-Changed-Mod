package net.zaharenko424.a_changed.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.AChangedTags;
import net.zaharenko424.a_changed.transfurSystem.LatexBeast;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static net.zaharenko424.a_changed.AChanged.textureLoc;

public enum GrabMode {
    ASSIMILATE(entity -> entity.getType().is(AChangedTags.Entity.TRANSFURRABLE_TAG) && !TransfurManager.isTransfurred(entity), true, true, textureLoc("gui/grab_assimilate")),
    REPLICATE(entity -> entity.getType().is(AChangedTags.Entity.TRANSFURRABLE_TAG) && !TransfurManager.isTransfurred(entity), true, true, textureLoc("gui/grab_replicate")),
    FRIENDLY(entity -> entity instanceof Player, false, false, textureLoc("gui/grab_friendly")),
    NONE(entity -> entity.getType().is(AChangedTags.Entity.TRANSFURRABLE_TAG) || entity instanceof LatexBeast, false, true, textureLoc("gui/grab_none"));

    private final Predicate<LivingEntity> checkTarget;
    private final boolean givesDebuffToTarget;
    public final boolean givesDebuffToSelf;
    public final ResourceLocation texture;

    GrabMode(Predicate<LivingEntity> checkTarget, boolean givesDebuffToTarget, boolean givesDebuffToSelf, ResourceLocation texture){
        this.checkTarget = checkTarget;
        this.givesDebuffToTarget = givesDebuffToTarget;
        this.givesDebuffToSelf = givesDebuffToSelf;
        this.texture = texture;
    }

    public boolean isOffensive(){
        return givesDebuffToTarget;
    }

    public boolean checkTarget(@NotNull LivingEntity target){
        return checkTarget.test(target);
    }
}