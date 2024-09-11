package net.zaharenko424.a_changed.atest;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.EntityRegistry;

public class TestZombie extends Zombie {
    public TestZombie(Level pLevel) {
        super(EntityRegistry.TEST.get(), pLevel);
    }
}
