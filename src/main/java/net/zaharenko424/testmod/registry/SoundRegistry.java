package net.zaharenko424.testmod.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.TestMod;
import org.jetbrains.annotations.NotNull;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, TestMod.MODID);

    public static final DeferredHolder<SoundEvent,SoundEvent> BUTTON_PRESSED = registerVariableRange("button_pressed");
    public static final DeferredHolder<SoundEvent,SoundEvent> DOOR_LOCKED = registerVariableRange("door_locked");
    public static final DeferredHolder<SoundEvent,SoundEvent> DOOR_OPEN = registerVariableRange("door_open");
    public static final DeferredHolder<SoundEvent,SoundEvent> KEYPAD_UNLOCKED = registerVariableRange("keypad_unlocked");
    public static final DeferredHolder<SoundEvent,SoundEvent> KEYPAD_WRONG_PASSWORD = registerVariableRange("keypad_wrong_password");
    public static final DeferredHolder<SoundEvent,SoundEvent> SAVE = registerVariableRange("save");
    public static final DeferredHolder<SoundEvent,SoundEvent> SMART_SEWAGE_CONSUME = registerVariableRange("smart_sewage_consume");
    //SoundType ...

    private static @NotNull DeferredHolder<SoundEvent,SoundEvent> registerVariableRange(String id){
        return SOUNDS.register(id,()-> SoundEvent.createVariableRangeEvent(TestMod.resourceLoc(id)));
    }
}