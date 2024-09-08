package net.zaharenko424.a_changed.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.a_changed.AChanged.textureLoc;

public enum GrabMode {
    ASSIMILATE(LivingEntity.class, true, true, textureLoc("gui/grab_assimilate")),
    REPLICATE(LivingEntity.class, true, true, textureLoc("gui/grab_replicate")),
    FRIENDLY(Player.class, false, false, textureLoc("gui/grab_friendly")),
    NONE(LivingEntity.class, false, true, textureLoc("gui/grab_none"));

    private final Class<? extends LivingEntity> targetClass;
    public final boolean givesDebuffToTarget;
    public final boolean givesDebuffToSelf;
    public final ResourceLocation texture;

    GrabMode(Class<? extends LivingEntity> targetClass, boolean givesDebuffToTarget, boolean givesDebuffToSelf, ResourceLocation texture){
        this.targetClass = targetClass;
        this.givesDebuffToTarget = givesDebuffToTarget;
        this.givesDebuffToSelf = givesDebuffToSelf;
        this.texture = texture;
    }

    public boolean checkTarget(@NotNull LivingEntity target){
        return targetClass.isAssignableFrom(target.getClass());
    }
}