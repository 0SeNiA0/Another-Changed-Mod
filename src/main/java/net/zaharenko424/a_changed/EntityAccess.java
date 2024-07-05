package net.zaharenko424.a_changed;

import net.minecraft.world.entity.Entity;

public interface EntityAccess {
    default Entity getSelf() {
        return (Entity) this;
    }
}