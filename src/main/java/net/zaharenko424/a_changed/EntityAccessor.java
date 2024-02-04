package net.zaharenko424.a_changed;

import net.minecraft.world.entity.Entity;

public interface EntityAccessor {
    default Entity getSelf() {
        return (Entity) this;
    }
}