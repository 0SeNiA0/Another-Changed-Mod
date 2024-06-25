package net.zaharenko424.a_changed;

import net.minecraft.world.level.Level;

public interface LevelAccess {

    default Level getSelf(){
        return (Level) this;
    }
}