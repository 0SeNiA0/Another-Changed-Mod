package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;

public class ActivityRegistry {

    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(BuiltInRegistries.ACTIVITY, AChanged.MODID);

    public static final DeferredHolder<Activity, Activity> TRANSFUR_GRAB_ESCAPE_STUN = registerSimple("transfur_grab_escape");
    public static final DeferredHolder<Activity, Activity> TRANSFUR_HOLD = registerSimple("transfur_hold");
    public static final DeferredHolder<Activity, Activity> TRANSFUR_ATTACK = registerSimple("transfur_attack");

    private static DeferredHolder<Activity, Activity> registerSimple(String name){
        return ACTIVITIES.register(name, () -> new Activity(name));
    }
}
