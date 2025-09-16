package net.zaharenko424.a_changed;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public interface ArmorSlotAccess {

    LivingEntity achanged$owner();

    EquipmentSlot achanged$slot();
}
