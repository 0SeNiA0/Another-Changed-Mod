package net.zaharenko424.a_changed.mixin.inventory;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.zaharenko424.a_changed.ArmorSlotAccess;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Slot.class)
public abstract class MixinSlot {

    @Shadow @Final public Container container;

    @Shadow @Final private int slot;

    @ModifyReturnValue(at = @At("RETURN"), method = "isActive")
    private boolean onIsActive(boolean original){
        if(slot == 40 && container instanceof Inventory inventory && AbilityUtils.hasDLPupAbilities(inventory.player)) return false;

        if((Object)this instanceof ArmorSlotAccess armor){
            LivingEntity owner = armor.achanged$owner();
            if(!(owner instanceof Player) || !AbilityUtils.hasDLPupAbilities(owner)) return original;

            return armor.achanged$slot() == EquipmentSlot.FEET;
        }
        return original;
    }
}