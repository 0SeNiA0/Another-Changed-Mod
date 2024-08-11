package net.zaharenko424.a_changed.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public enum GrabMode {
    ASSIMILATE(LivingEntity.class, true, true),
    REPLICATE(LivingEntity.class, true, true),
    FRIENDLY(Player.class, false, false),
    NONE(LivingEntity.class, false, true);

    private final Class<? extends LivingEntity> targetClass;
    public final boolean givesDebuffToTarget;
    public final boolean givesDebuffToSelf;

    GrabMode(Class<? extends LivingEntity> targetClass, boolean givesDebuffToTarget, boolean givesDebuffToSelf){
        this.targetClass = targetClass;
        this.givesDebuffToTarget = givesDebuffToTarget;
        this.givesDebuffToSelf = givesDebuffToSelf;
    }

    public boolean checkTarget(@NotNull LivingEntity target){
        return targetClass.isAssignableFrom(target.getClass());
    }
}