package net.zaharenko424.testmod.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.TestMod;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, TestMod.MODID);

    public static final DeferredHolder<SoundEvent,SoundEvent> TEST_SOUND = SOUNDS.register("test",()-> SoundEvent.createVariableRangeEvent(TestMod.resourceLoc("test")));
    //SoundType ...
}